package rs.ac.bg.etf.pp1;

import org.apache.log4j.Logger;
import rs.ac.bg.etf.pp1.ast.*;
import rs.ac.bg.etf.pp1.error.Errorable;
import rs.ac.bg.etf.pp1.error.SemanticError;
import rs.ac.bg.etf.pp1.test.CompilerError;
import rs.ac.bg.etf.pp1.util.CustomDumpSymbolTableVisitor;
import rs.etf.pp1.symboltable.*;
import rs.etf.pp1.symboltable.concepts.*;

import java.util.*;

import static rs.ac.bg.etf.pp1.error.SemanticError.*;

public class SemanticAnalyzer extends VisitorAdaptor implements Errorable {
    public static final Struct boolType = new Struct(Struct.Bool);

    private final Map<String, Obj> objTemporaries = new HashMap<>();
    private final Map<String, Struct> structTemporaries = new HashMap<>();
    private static final String ThisReferenceName = "this";
    private static final String MainMethodName = "main";
    private final List<CompilerError> errorList = new ArrayList<>();

    Logger log = Logger.getLogger(getClass());
    private int nVars;

    public int getNVars() {
        return nVars;
    }

    @Override
    public List<CompilerError> getErrorList() {
        return errorList;
    }

    private static class ObjConstants {
        public static final String CurrentProgramName = "CurrentProgram";
        public static final String CurrentMethodName = "CurrentMethod";
    }

    private static class StructConstants {
        public static final String CurrentVarDeclTypeName = "CurrentVarDeclType";
        public static final String CurrentConstDeclTypeName = "CurrentConstDeclType";
        public static final String CurrentClassTypeName = "CurrentClassType";
        public static final String CurrentBaseClassTypeName = "CurrentBaseClassType";
        public static final String CurrentMethodReturnTypeName = "CurrentMethodReturnTypeName";
    }

    private static class Nesting {
        public static final Integer SWITCH_STATEMENT = 0;
        public static final Integer SWITCH_EXPRESSION = 1;
        public static final Integer DO_WHILE_LOOP = 2;
    }

    private final Stack<Set<Integer>> switchCurrentCasesStack = new Stack<>();
    private final Stack<Integer> nestingTypeStack = new Stack<>();
    private final Stack<Struct> switchExpressionTypeStack = new Stack<>();
    private final Stack<Obj> methodInvocationStack = new Stack<>();
    private int functionNumFormalParameters = 0;
    private final Stack<Integer> functionCallNumFormalParametersStack = new Stack<>();
    private final Stack<Integer> functionCallNumActualParametersStack = new Stack<>();

    public static void initUniverseScope() {
        Tab.init();
        Tab.insert(Obj.Type, "bool", boolType);
    }

    public void visit(Program program) {
        Obj programObj = objTemporaries.get(ObjConstants.CurrentProgramName);

        Obj mainMethod = Tab.currentScope().findSymbol(MainMethodName);

        if ((mainMethod == null) || (mainMethod.getKind() != Obj.Meth) || !(mainMethod.getType().equals(Tab.noType)) || (mainMethod.getLevel() != 0)) {
            reportError(MAIN_MISSING, program);

            return;
        }

        nVars = Tab.currentScope.getnVars();
        Tab.chainLocalSymbols(programObj);
        Tab.closeScope();
    }

    public void visit(ProgramName programName) {
        Obj programObj = Tab.insert(Obj.Prog, programName.getName(), Tab.noType);

        Tab.openScope();

        objTemporaries.put(ObjConstants.CurrentProgramName, programObj);
    }

    public void visit(Type type) {
        Obj typeNode = Tab.find(type.getName());

        if (typeNode.equals(Tab.noObj)) {
            type.struct = Tab.noType;
            reportError(INVALID_TYPE_UNKNOWN_NAME, type, type.getName());
        }
        else
        {
            if (typeNode.getKind() == Obj.Type) {
                type.struct = typeNode.getType();
            }
            else
            {
                type.struct = Tab.noType;
                reportError(INVALID_TYPE_NOT_A_TYPE, type, type.getName());
            }
        }
    }

    public void visit(VarDeclType varDeclType) {
        structTemporaries.put(StructConstants.CurrentVarDeclTypeName, varDeclType.getType().struct);
    }

    public void visit(VariableDeclaration variableDeclaration) {
        structTemporaries.remove(StructConstants.CurrentVarDeclTypeName);
    }

    public void visit(ScalarVarIdent scalarVarIdent) {
        Obj varObj = Tab.currentScope().findSymbol(scalarVarIdent.getName());

        if (varObj != null) {
            reportError(DUPLICATE_NAME_DECLARATION_TEMPLATE, scalarVarIdent, varObj.getName());

            return;
        }

        Obj res = Tab.insert(getVarKind(), scalarVarIdent.getName(), structTemporaries.get(StructConstants.CurrentVarDeclTypeName));
        res.setFpPos(Obj.NO_VALUE);
    }

    public void visit(ArrayVarIdent arrayVarIdent) {
        Obj varObj = Tab.currentScope().findSymbol(arrayVarIdent.getName());

        if (varObj != null) {
            reportError(DUPLICATE_NAME_DECLARATION_TEMPLATE, arrayVarIdent, varObj.getName());

            return;
        }

        // ToDo: Does this need fixing?
        Struct varArrayStruct = new Struct(Struct.Array, structTemporaries.get(StructConstants.CurrentVarDeclTypeName));
        Obj res = Tab.insert(getVarKind(), arrayVarIdent.getName(), varArrayStruct);
        res.setFpPos(Obj.NO_VALUE);
    }

    // Constant

    public void visit(ConstDeclType constDeclType) {
        structTemporaries.put(StructConstants.CurrentConstDeclTypeName, constDeclType.getType().struct);
    }

    public void visit(ConstDecl constDecl) {
        structTemporaries.remove(StructConstants.CurrentConstDeclTypeName);
    }

    // ToDo: Move
    private void createConstant(String name, int value, SyntaxNode info) {
        Obj constObj = Tab.currentScope().findSymbol(name);

        if (constObj != null) {
            reportError(DUPLICATE_NAME_DECLARATION_TEMPLATE, info, constObj.getName());

            return;
        }

        Struct constType = structTemporaries.get(StructConstants.CurrentConstDeclTypeName);

        Obj constant = Tab.insert(Obj.Con, name, constType);
        constant.setAdr(value);
    }

    public void visit(NumberConstNameValuePair numberConstNameValuePair) {
        createConstant(numberConstNameValuePair.getName(), numberConstNameValuePair.getValue(), numberConstNameValuePair);
    }

    public void visit(CharConstNameValuePair charConstNameValuePair) {
        createConstant(charConstNameValuePair.getName(), charConstNameValuePair.getValue(), charConstNameValuePair);
    }

    public void visit(BoolConstNameValuePair boolConstNameValuePair) {
        createConstant(boolConstNameValuePair.getName(), boolConstNameValuePair.getValue()  ? 1 : 0, boolConstNameValuePair);
    }

    // Method

    public void visit(TypeMethodTypeNamePair typeMethodTypeNamePair) {
        Obj methodObj = Tab.currentScope().findSymbol(typeMethodTypeNamePair.getName());

        if (methodObj != null) {
            if (currentlyProcessingExtendedClass()) {
                Struct baseClass = structTemporaries.get(StructConstants.CurrentBaseClassTypeName);
                Obj baseMethodObj = baseClass.getMembersTable().searchKey(typeMethodTypeNamePair.getName());

                if (baseMethodObj == null) {
                    reportError(DUPLICATE_NAME_DECLARATION_TEMPLATE, typeMethodTypeNamePair, methodObj.getName());

                    return;
                }
            } else {
                reportError(DUPLICATE_NAME_DECLARATION_TEMPLATE, typeMethodTypeNamePair, methodObj.getName());

                return;
            }
        }

        Struct returnType = typeMethodTypeNamePair.getType().struct;

        Obj currentMethod = null;

        if (currentlyProcessingExtendedClass()) {
            currentMethod = Tab.currentScope().findSymbol(typeMethodTypeNamePair.getName());
        }

        // Check if we're overriding a method
        if ((currentMethod == null) || currentMethod.equals(Tab.noObj)) {
            currentMethod = Tab.insert(Obj.Meth, typeMethodTypeNamePair.getName(), returnType);
        }
        else
        {
            currentMethod.setLocals(null);
        }

        Tab.openScope();

        // Insert a reference to "this"
        if (currentlyProcessingClass()) {
            Tab.insert(Obj.Var, ThisReferenceName, structTemporaries.get(StructConstants.CurrentClassTypeName));
            functionNumFormalParameters = 1;
        }
        else {
            functionNumFormalParameters = 0;
        }

        structTemporaries.put(StructConstants.CurrentMethodReturnTypeName, returnType);
        objTemporaries.put(ObjConstants.CurrentMethodName, currentMethod);

        typeMethodTypeNamePair.obj = currentMethod;
    }

    public void visit(VoidMethodTypeNamePair voidMethodTypeNamePair) {
        Obj methodObj = Tab.currentScope().findSymbol(voidMethodTypeNamePair.getName());

        if (methodObj != null) {
            if (currentlyProcessingExtendedClass()) {
                Struct baseClass = structTemporaries.get(StructConstants.CurrentBaseClassTypeName);
                Obj baseMethodObj = baseClass.getMembersTable().searchKey(voidMethodTypeNamePair.getName());

                if (baseMethodObj == null) {
                    reportError(DUPLICATE_NAME_DECLARATION_TEMPLATE, voidMethodTypeNamePair, methodObj.getName());

                    return;
                }
            } else {
                reportError(DUPLICATE_NAME_DECLARATION_TEMPLATE, voidMethodTypeNamePair, methodObj.getName());

                return;
            }
        }

        Struct returnType = Tab.noType;

        Obj currentMethod = null;

        if (currentlyProcessingExtendedClass()) {
            currentMethod = Tab.currentScope().findSymbol(voidMethodTypeNamePair.getName());
        }

        // Check if we're overriding a method
        if ((currentMethod == null) || currentMethod.equals(Tab.noObj)) {
            currentMethod = Tab.insert(Obj.Meth, voidMethodTypeNamePair.getName(), returnType);
        }
        else
        {
            currentMethod.setLocals(null);
        }

        Tab.openScope();

        // Insert a reference to "this"
        if (currentlyProcessingClass()) {
            Tab.insert(Obj.Var, ThisReferenceName, structTemporaries.get(StructConstants.CurrentClassTypeName));
            functionNumFormalParameters = 1;
        }
        else {
            functionNumFormalParameters = 0;
        }

        structTemporaries.put(StructConstants.CurrentMethodReturnTypeName, returnType);
        objTemporaries.put(ObjConstants.CurrentMethodName, currentMethod);

        voidMethodTypeNamePair.obj = currentMethod;
    }

    public void visit(ScalarFormalParameter scalarFormalParameter) {
        Obj res = Tab.insert(Obj.Var, scalarFormalParameter.getName(), scalarFormalParameter.getType().struct);
        res.setFpPos(functionNumFormalParameters++);
    }

    public void visit(ArrayFormalParameter arrayFormalParameter) {
        // ToDo: Does this need fixing?
        Struct arrayFormalParameterStruct = new Struct(Struct.Array, arrayFormalParameter.getType().struct);

        Obj res = Tab.insert(Obj.Var, arrayFormalParameter.getName(), arrayFormalParameterStruct);
        res.setFpPos(functionNumFormalParameters++);
    }

    public void visit(MethodFormalParameters methodFormPars) {
        Obj currentMethod = objTemporaries.get(ObjConstants.CurrentMethodName);

        currentMethod.setLevel(functionNumFormalParameters);

        Tab.chainLocalSymbols(currentMethod);
    }

    public void visit(MethodDeclaration methodDeclaration) {
        Obj currentMethod = objTemporaries.get(ObjConstants.CurrentMethodName);

        Tab.chainLocalSymbols(currentMethod);
        Tab.closeScope();

        objTemporaries.remove(ObjConstants.CurrentMethodName);
    }

    public void visit(ExtendedClassDeclaration extendedClassDeclaration) {
        Struct currentClass = structTemporaries.get(StructConstants.CurrentClassTypeName);

        Tab.chainLocalSymbols(currentClass);
        Tab.closeScope();

        structTemporaries.remove(StructConstants.CurrentClassTypeName);
        structTemporaries.remove(StructConstants.CurrentBaseClassTypeName);
    }

    public void visit(ClassDeclaration extendedClassDeclaration) {
        Struct currentClass = structTemporaries.get(StructConstants.CurrentClassTypeName);

        Tab.chainLocalSymbols(currentClass);
        Tab.closeScope();

        structTemporaries.remove(StructConstants.CurrentClassTypeName);
    }

    public void visit(ClassType classType) {
        Obj classObj = Tab.currentScope().findSymbol(classType.getName());

        if (classObj != null) {
            reportError(DUPLICATE_NAME_DECLARATION_TEMPLATE, classType, classObj.getName());

            return;
        }

        Struct classStruct = new Struct(Struct.Class);
        Tab.insert(Obj.Type, classType.getName(), classStruct);
        Tab.openScope();
        // mvt_ptr + class name to differentiate for assignability comparison
        Tab.insert(Obj.Fld, "$mvt_ptr_" + classType.getName(), Tab.intType);

        classType.struct = classStruct;

        structTemporaries.put(StructConstants.CurrentClassTypeName, classStruct);
    }

    public void visit(BaseClassName baseClassName) {
        Struct baseClass = baseClassName.getType().struct;
        Struct extendedClass = structTemporaries.get(StructConstants.CurrentClassTypeName);

        if (baseClass.getKind() != Struct.Class) {
            reportError(INVALID_BASE_CLASS_TYPE, baseClassName);

            return;
        }

        extendedClass.setElementType(baseClass);

        for (Obj baseClassMember : baseClass.getMembers()) {
            if (baseClassMember.getKind() == Obj.Fld) {
                // skip mvt_ptr
                if (baseClassMember.getName().startsWith("$mvt_ptr_")) {
                    continue;
                }

                Tab.insert(baseClassMember.getKind(), baseClassMember.getName(), baseClassMember.getType());
            }
        }

        structTemporaries.put(StructConstants.CurrentBaseClassTypeName, baseClass);
    }

    public void visit(ExtendedClassSplitter extendedClassSplitter) {
        Struct baseClass = structTemporaries.get(StructConstants.CurrentBaseClassTypeName);
        Struct currentClass = structTemporaries.get(StructConstants.CurrentClassTypeName);

        for (Obj baseClassMember : baseClass.getMembers()) {
            if (baseClassMember.getKind() == Obj.Meth) {
                Obj currentMethod = Tab.insert(baseClassMember.getKind(), baseClassMember.getName(), baseClassMember.getType());
                currentMethod.setLevel(baseClassMember.getLevel());

                Tab.openScope();

                // Copy method parameters (formal parameters and local symbols)
                for (Obj localParam : baseClassMember.getLocalSymbols()) {
                    // Correct type for "this" reference
                    if (localParam.getName().equals(ThisReferenceName)) {
                        Tab.insert(localParam.getKind(), localParam.getName(), currentClass);
                    }
                    else
                    {
                        Obj res = Tab.insert(localParam.getKind(), localParam.getName(), localParam.getType());
                        res.setFpPos(localParam.getFpPos());
                    }
                }

                Tab.chainLocalSymbols(currentMethod);
                Tab.closeScope();
            }
        }
    }

    // checks
    public void visit(ScalarDesignator scalarDesignator) {
        Obj scalarDesignatorObj = Tab.find(scalarDesignator.getName());

        if (scalarDesignatorObj.equals(Tab.noObj)) {
            scalarDesignator.obj = Tab.noObj;
            reportError(UNDECLARED_SYMBOL_TEMPLATE, scalarDesignator, scalarDesignator.getName());
        }
        else
        {
            if ((scalarDesignatorObj.getKind() != Obj.Var) && (scalarDesignatorObj.getKind() != Obj.Con) && (scalarDesignatorObj.getKind() != Obj.Meth) && (scalarDesignatorObj.getKind() != Obj.Fld)) {
                scalarDesignator.obj = Tab.noObj;
                reportError(INVALID_DESIGNATOR_NOT_ASSIGNABLE, scalarDesignator);
            } else {
                reportInfo("Detected use of " + CustomDumpSymbolTableVisitor.printObjNode(scalarDesignatorObj), scalarDesignator);

                scalarDesignator.obj = scalarDesignatorObj;
            }
        }
    }

    public void visit(ObjectAccessDesignator objectAccessDesignator) {
        Obj objectDesignatorObj = objectAccessDesignator.getDesignator().obj;

        if (objectDesignatorObj.getType().getKind() != Struct.Class) {
            objectAccessDesignator.obj = Tab.noObj;

            if (!objectDesignatorObj.equals(Tab.noObj)) {
                reportError(INVALID_DESIGNATOR_ACCESS_TEMPLATE, objectAccessDesignator, objectDesignatorObj.getName(), CLASS_REFERENCE_STRING);
            }
            else {
                reportError(INVALID_ACCESS_UNKNOWN_COMPLEX_DESIGNATOR_TEMPLATE, objectAccessDesignator, CLASS_REFERENCE_STRING);
            }

            return;
        }
        Obj fieldNameObj;

        // Check if we're accessing class that we're currently processing
        // ToDo: Is this correct
        if (currentlyProcessingClass() && currentlyProcessingMethod() && (objectDesignatorObj.getType().equals(structTemporaries.get(StructConstants.CurrentClassTypeName)))) {
            fieldNameObj = Tab.currentScope().getOuter().getLocals().searchKey(objectAccessDesignator.getFieldName());
        }
        else
        {
            fieldNameObj = objectDesignatorObj.getType().getMembersTable().searchKey(objectAccessDesignator.getFieldName());
        }

        if (fieldNameObj == null) {
            objectAccessDesignator.obj = Tab.noObj;
            reportError(INVALID_ACCESS_UNKNOWN_CLASS_MEMBER_TEMPLATE, objectAccessDesignator, objectAccessDesignator.getFieldName());

            return;
        }

        reportInfo("Detected use of " + CustomDumpSymbolTableVisitor.printObjNode(fieldNameObj), objectAccessDesignator);
        objectAccessDesignator.obj = fieldNameObj;
    }

    public void visit(ArrayAccessDesignator arrayAccessDesignator) {
        Obj arrayAccessDesignatorObj = arrayAccessDesignator.getDesignator().obj;

        if (arrayAccessDesignatorObj.getType().getKind() != Struct.Array) {
            arrayAccessDesignator.obj = Tab.noObj;
            if (!arrayAccessDesignatorObj.equals(Tab.noObj)) {
                reportError(INVALID_DESIGNATOR_ACCESS_TEMPLATE, arrayAccessDesignator, arrayAccessDesignatorObj.getName(), ARRAY_REFERENCE_STRING);
            }
            else {
                reportError(INVALID_ACCESS_UNKNOWN_COMPLEX_DESIGNATOR_TEMPLATE, arrayAccessDesignator, ARRAY_REFERENCE_STRING);
            }
            return;
        }

        if (!arrayAccessDesignator.getExpr().struct.equals(Tab.intType)) {
            arrayAccessDesignator.obj = Tab.noObj;
            reportError(INVALID_EXPRESSION_MUST_BE_INT, arrayAccessDesignator);

            return;
        }

        // ToDo: Does this need fixing, can I use a temporary object like this
        arrayAccessDesignator.obj = new Obj(Obj.Elem, arrayAccessDesignator.getDesignator().obj.getName() + "[]", arrayAccessDesignatorObj.getType().getElemType());

        reportInfo("Detected use of " + CustomDumpSymbolTableVisitor.printObjNode(arrayAccessDesignator.obj), arrayAccessDesignator);
    }

    public void visit(VarFactor varFactor) {
        varFactor.struct = varFactor.getDesignator().obj.getType();
    }

    public  void visit(FunctionCallResultFactor functionCallResultFactor) {
        functionCallResultFactor.struct = functionCallResultFactor.getFunctionCall().struct;
    }

    public void visit(NumConstFactor numConstFactor) {
        numConstFactor.struct = Tab.intType;
    }

    public void visit(CharConstFactor charConstFactor) {
        charConstFactor.struct = Tab.charType;
    }

    public void visit(BoolConstFactor boolConstFactor) {
        // ToDo
        boolConstFactor.struct = Tab.find("bool").getType();
    }

    public void visit(NewObjectFactor newObjectFactor) {
        if (newObjectFactor.getType().struct.getKind() != Struct.Class) {
            newObjectFactor.struct = Tab.noType;
            reportError(INVALID_NEW_OBJECT_EXPRESSION_NOT_CLASS_TYPE_TEMPLATE, newObjectFactor, newObjectFactor.getType().getName());

            return;
        }

        newObjectFactor.struct = newObjectFactor.getType().struct;
    }

    public void visit(NewArrayObjectFactor newArrayObjectFactor) {
        if (!newArrayObjectFactor.getExpr().struct.equals(Tab.intType)) {
            newArrayObjectFactor.struct = Tab.noType;
            reportError(INVALID_EXPRESSION_MUST_BE_INT, newArrayObjectFactor);

            return;
        }

        // ToDo: New array?
        newArrayObjectFactor.struct = new Struct(Struct.Array, newArrayObjectFactor.getType().struct);
    }

    public void visit(ExprResultFactor exprResultFactor) {
        exprResultFactor.struct = exprResultFactor.getExpr().struct;
    }

    public void visit(TermListElement termListElement) {
        if ((!termListElement.getFactor().struct.equals(Tab.intType)) || (!termListElement.getTerm().struct.equals(Tab.intType))) {
            termListElement.struct = Tab.noType;
            reportError(INVALID_EXPRESSION_MUST_BE_INT, termListElement);

            return;
        }

        termListElement.struct = Tab.intType;
    }

    public void visit(TermListHead termListHead) {
        termListHead.struct = termListHead.getFactor().struct;
    }

    public void visit(TermExprListElement termExprListElement) {
        if ((!termExprListElement.getTermExpr().struct.equals(Tab.intType)) || (!termExprListElement.getTerm().struct.equals(Tab.intType))) {
            termExprListElement.struct = Tab.noType;
            reportError(INVALID_EXPRESSION_MUST_BE_INT, termExprListElement);

            return;
        }

        termExprListElement.struct = Tab.intType;
    }

    public void visit(TermExprListHead termExprListHead) {
        termExprListHead.struct = termExprListHead.getTerm().struct;
    }

    public void visit(TermExpression expr) {
        if (!expr.getTermExpr().struct.equals(Tab.intType) && (expr.getExprPrefix().getClass() != NoExpressionPrefix.class)) {
            expr.struct = Tab.noType;
            reportError(INVALID_EXPRESSION_MUST_BE_INT, expr);

            return;
        }

        expr.struct = expr.getTermExpr().struct;
    }


    public void visit(RelationalCondFact relationalCondFact) {
        Struct exprResultType0 = relationalCondFact.getExpr().struct;
        Struct exprResultType1 = relationalCondFact.getExpr1().struct;

        if (!exprResultType0.compatibleWith(exprResultType1)) {
            reportError(INCOMPATIBLE_TYPES_FOR_RELATIONAL_OPERATIONS, relationalCondFact);

            return;
        }

        if (((exprResultType0.getKind() == Struct.Class) || (exprResultType0.getKind() == Struct.Array)) && (!(relationalCondFact.getRelop() instanceof RelopCEquals) && !(relationalCondFact.getRelop() instanceof RelopCNEquals))) {
            reportError(INVALID_RELATIONAL_OPERATION_FOR_CLASS_OR_ARRAY, relationalCondFact);

            return;
        }
    }

    public void visit(SwitchStatementExpression switchStatementExpression) {
        if (!switchStatementExpression.getExpr().struct.equals(Tab.intType)) {
            reportError(INVALID_EXPRESSION_MUST_BE_INT, switchStatementExpression);

            return;
        }
    }

    public void visit(SwitchExpression switchExpression) {
        switchCurrentCasesStack.pop();
        nestingTypeStack.pop();

        Struct switchExpressionType = switchExpressionTypeStack.pop();

        // ToDo: Potentially add null control and have null as error type
        if (switchExpressionType == null) {
            switchExpressionType = Tab.noType;
        }

        switchExpression.struct = switchExpressionType;
    }

    public void visit(SwitchCondition switchCase) {
        Set<Integer> switchCurrentCases = switchCurrentCasesStack.peek();

        if (switchCurrentCases.contains(switchCase.getNumber())) {
            reportError(INVALID_SWITCH_DUPLICATE_CASES, switchCase);

            return;
        }

        switchCurrentCases.add(switchCase.getNumber());
    }

    public void visit(SwitchExpressionStart switchExpressionStart) {
        nestingTypeStack.push(Nesting.SWITCH_EXPRESSION);
        switchCurrentCasesStack.push(new HashSet<>());
        switchExpressionTypeStack.push(null);
    }

    public void visit(DoWhileStatementStart doWhileStatementStart) {
        nestingTypeStack.push(Nesting.DO_WHILE_LOOP);
    }

    public void visit(DoWhileStatementEnd doWhileStatementEnd) {
        nestingTypeStack.pop();
    }

    public void visit(MatchedContinueStatement matchedContinueStatement) {
        if (!currentlyProcessingDoWhile()) {
            reportError(INVALID_CONTINUE_OUTSIDE_LOOP, matchedContinueStatement);

            return;
        }
    }

    public void visit(MatchedBreakStatement matchedBreakStatement) {
        if (!(currentlyProcessingDoWhile() || currentlyProcessingSwitchStatement())) {
            reportError("Break statement can only be used within a do-while or switch-case statement blocks!", matchedBreakStatement);

            return;
        }
    }

    public void visit(ActualParameter actualParameter) {
        Obj currentInvokedFunction = methodInvocationStack.peek();
        int functionCallCurrentParameter = functionCallNumActualParametersStack.peek();
        int functionCallNumFormalParameters = functionCallNumFormalParametersStack.peek();

        if (functionCallCurrentParameter == functionCallNumFormalParameters) {
            reportError(INVALID_FUNCTION_CALL_EXCESS_PARAMETERS, actualParameter);

            return;
        }

        Struct currentInvokedFunctionParameterType = actualParameter.getExpr().struct;
        Struct currentInvokedFunctionFormalParameterType = ((Obj)currentInvokedFunction.getLocalSymbols().toArray()[functionCallCurrentParameter]).getType();

        if (!checkAssignCompatibility(currentInvokedFunctionParameterType, currentInvokedFunctionFormalParameterType)) {
            reportError(INCOMPATIBLE_FUNCTION_PARAMETERS, actualParameter);
        }

        functionCallNumActualParametersStack.set(functionCallNumActualParametersStack.size() - 1, functionCallCurrentParameter + 1);
    }

    public void visit(FunctionCall functionCall) {
        int functionCallCurrentParameter = functionCallNumActualParametersStack.peek();
        int functionCallNumFormalParameters = functionCallNumFormalParametersStack.peek();

        functionCall.struct = functionCall.getFunctionCallDesignator().obj.getType();

        if (functionCallCurrentParameter != functionCallNumFormalParameters) {
            reportError(INVALID_FUNCTION_CALL_MISSING_PARAMETERS, functionCall);

            return;
        }

        functionCallNumFormalParametersStack.pop();
        functionCallNumActualParametersStack.pop();
        methodInvocationStack.pop();
    }

    public void visit(FunctionCallDesignator functionCallDesignator) {
        Obj functionCallDesignatorObj = functionCallDesignator.getDesignator().obj;

        if (functionCallDesignatorObj.getKind() != Obj.Meth) {
            functionCallDesignator.obj = Tab.noObj;
            reportError(INVALID_DESIGNATOR_ACCESS_TEMPLATE, functionCallDesignator, functionCallDesignatorObj.getName(), METHOD_OR_FUNCTION_STRING);

            return;
        }

        int functionCallNumFormalParameters = functionCallDesignatorObj.getLevel();

        functionCallNumFormalParametersStack.push(functionCallDesignatorObj.getLevel());
        functionCallNumActualParametersStack.push(0);
        methodInvocationStack.push(functionCallDesignatorObj);

        // Skip implicit 'this' reference
        if (functionCallNumFormalParameters != 0) {
            // ToDo: check if array access here is correct
            Obj currentInvokedFunctionFormalParameterObj = (Obj) functionCallDesignatorObj.getLocalSymbols().toArray()[0];

            if (currentInvokedFunctionFormalParameterObj.getName().equals(ThisReferenceName)) {
                functionCallNumActualParametersStack.set(functionCallNumActualParametersStack.size() - 1, 1);
            }
        }

        functionCallDesignator.obj = functionCallDesignatorObj;
    }

    public void visit(ReturnExpression returnExpression) {
        Obj currentMethod = objTemporaries.get(ObjConstants.CurrentMethodName);

        if (!currentMethod.getType().equals(returnExpression.getExpr().struct)) {
            reportError(INVALID_RETURN_TYPE_MISMATCH, returnExpression);

            return;
        }

        if (currentlyProcessingSwitchExpression()) {
            reportError(INVALID_RETURN_WITHIN_SWITCH, returnExpression);

            return;
        }
    }

    public void visit(YieldExpression yieldExpression) {
        if (!currentlyProcessingSwitchExpression()) {
            reportError(INVALID_YIELD_OUTSIDE_SWITCH, yieldExpression);

            return;
        }

        if (switchExpressionTypeStack.peek() == null) {
            switchExpressionTypeStack.pop();
            switchExpressionTypeStack.push(yieldExpression.getExpr().struct);
        } else if (!switchExpressionTypeStack.peek().equals(Tab.noType) && !switchExpressionTypeStack.peek().equals(yieldExpression.getExpr().struct)) {
            Struct current = switchExpressionTypeStack.pop();
            Struct closerToRoot = getCloserToRoot(current, yieldExpression.getExpr().struct);

            if (closerToRoot.equals(Tab.noType)) {
                reportError(SWITCH_YIELD_EXPRESSION_TYPE_MISMATCH, yieldExpression);
            }

            switchExpressionTypeStack.push(closerToRoot);
        }

    }

    public void visit(NoReturnExpression noReturnExpression) {
        Obj currentMethod = objTemporaries.get(ObjConstants.CurrentMethodName);

        if (!currentMethod.getType().equals(Tab.noType)) {
            reportError(INVALID_RETURN_NO_EXPRESSION, noReturnExpression);

            return;
        }
    }

    public void visit(AssignmentDesignatorStatement assignmentDesignatorStatement) {
        Obj designatorObj = assignmentDesignatorStatement.getDesignator().obj;
        int designatorKind = designatorObj.getKind();

        if ((designatorKind != Obj.Fld) && (designatorKind != Obj.Var) && (designatorKind != Obj.Elem)) {
            reportError(INVALID_DESIGNATOR_NOT_ASSIGNABLE, assignmentDesignatorStatement);

            return;
        }

        if (!checkAssignCompatibility(assignmentDesignatorStatement.getExpr().struct, designatorObj.getType())) {
            reportError(INCOMPATIBLE_ASSIGNMENT, assignmentDesignatorStatement);

            return;
        }
    }

    public void visit(PostDecDesignatorStatement postDecDesignatorStatement) {
        Obj designatorObj = postDecDesignatorStatement.getDesignator().obj;
        int designatorKind = designatorObj.getKind();

        if ((designatorKind != Obj.Fld) && (designatorKind != Obj.Var) && (designatorKind != Obj.Elem)) {
            reportError(INVALID_DESIGNATOR_NOT_ASSIGNABLE, postDecDesignatorStatement);

            return;
        }

        if (!designatorObj.getType().equals(Tab.intType)) {
            reportError(INVALID_DESIGNATOR_MUST_BE_INT, postDecDesignatorStatement);

            return;
        }
    }

    public void visit(PostIncDesignatorStatement postIncDesignatorStatement) {
        Obj designatorObj = postIncDesignatorStatement.getDesignator().obj;
        int designatorKind = designatorObj.getKind();

        if ((designatorKind != Obj.Fld) && (designatorKind != Obj.Var) && (designatorKind != Obj.Elem)) {
            reportError(INVALID_DESIGNATOR_NOT_ASSIGNABLE, postIncDesignatorStatement);

            return;
        }

        if (!designatorObj.getType().equals(Tab.intType)) {
            reportError(INVALID_DESIGNATOR_MUST_BE_INT, postIncDesignatorStatement);

            return;
        }
    }

    public void visit(MatchedPrintStatement matchedPrintStatement) {
        Struct exprTypeStruct = matchedPrintStatement.getExpr().struct;

        if (!exprTypeStruct.equals(Tab.intType) && !exprTypeStruct.equals(Tab.charType) && !exprTypeStruct.equals(boolType)) {
            reportError("Invalid first parameter for print statement!", matchedPrintStatement);

            return;
        }
    }

    public void visit(MatchedReadStatement matchedReadStatement) {
        Obj designatorObj = matchedReadStatement.getDesignator().obj;
        Struct designatorType = designatorObj.getType();
        int designatorKind = designatorObj.getKind();

        if ((designatorKind != Obj.Fld) && (designatorKind != Obj.Var) && (designatorKind != Obj.Elem)) {
            reportError("Designator is neither Field, Variable or Array Element!", matchedReadStatement);

            return;
        }

        if (!designatorType.equals(Tab.intType) && !designatorType.equals(Tab.charType) && !designatorType.equals(boolType)) {
            reportError("Invalid first parameter for read statement!", matchedReadStatement);

            return;
        }
    }

    // Helper methods

    private int getVarKind() {
        int varKind = Obj.Var;

        // If we are processing a class and we're not processing a method class member this is a class field
        if (currentlyProcessingClass() && !currentlyProcessingMethod()) {
            varKind = Obj.Fld;
        }

        return varKind;
    }

    private boolean currentlyProcessingClass() {
        return structTemporaries.get(StructConstants.CurrentClassTypeName) != null;
    }

    private boolean currentlyProcessingExtendedClass() {
        return structTemporaries.get(StructConstants.CurrentBaseClassTypeName) != null;
    }

    private boolean currentlyProcessingMethod() {
        return (objTemporaries.get(ObjConstants.CurrentMethodName) != null);
    }

    public void reportError(String template, SyntaxNode info, Object... args) {
        String errorMessage = String.format(template, args);

        reportError(errorMessage, info);
    }

    public boolean isErrorDetected() {
        return !errorList.isEmpty();
    }

    public void reportError(String message, SyntaxNode info) {
        StringBuilder msg = new StringBuilder();
        int line = (info == null) ? 0: info.getLine();

        // Add error to list
        errorList.add(new SemanticError(line, message));

        // Format logger output
        if (line != 0) {
            msg.append("Line: ").append(line).append(" - ");
        }

        msg.append(message);

        log.error(msg.toString());
    }

    public void reportInfo(String message, SyntaxNode info) {
        StringBuilder msg = new StringBuilder();
        int line = (info == null) ? 0: info.getLine();

        if (line != 0) {
            msg.append("Line: ").append(line).append(" - ");
        }

        msg.append(message);

        log.info(msg.toString());
    }

    private static Struct getCloserToRoot(Struct first, Struct second) {
        if ((first.getKind() == Struct.Class) && (second.getKind() == Struct.Class)) {
            Struct firstCpy = first;
            Struct secondCpy = second;

            LinkedList<Struct> firstPath = new LinkedList<>();
            LinkedList<Struct> secondPath = new LinkedList<>();

            firstPath.addFirst(firstCpy);
            secondPath.addFirst(secondCpy);

            while (!(firstCpy.getElemType() == null && secondCpy.getElemType() == null)) {
                if(firstCpy.getElemType() != null) {
                    firstCpy = firstCpy.getElemType();
                    firstPath.addFirst(firstCpy);
                }

                if (secondCpy.getElemType() != null) {
                    secondCpy = secondCpy.getElemType();
                    secondPath.addFirst(secondCpy);
                }
            }

            Struct result = Tab.noType;

            do {
                firstCpy = firstPath.removeFirst();
                secondCpy = secondPath.removeFirst();

                if (firstCpy.equals(secondCpy)) {
                    result = firstCpy;
                } else {
                    break;
                }
            } while (!firstPath.isEmpty() && !secondPath.isEmpty());

            return result;
        }

        return Tab.noType;
    }

    private static boolean checkAssignCompatibility(Struct source, Struct destination) {
        boolean defaultCompatible = source.assignableTo(destination);

        if (!defaultCompatible) {
            if ((source.getKind() == Struct.Class) && (destination.getKind() == Struct.Class)) {
                while ((source.getElemType() != null) && !(source.equals(destination))) {
                    source = source.getElemType();
                }

                return source.equals(destination);
            }
        }

        return defaultCompatible;
    }

    private boolean currentlyProcessingSwitchStatement() {
        return !nestingTypeStack.isEmpty() && nestingTypeStack.peek().equals(Nesting.SWITCH_STATEMENT);
    }

    private boolean currentlyProcessingSwitchExpression() {
        return !nestingTypeStack.isEmpty() && nestingTypeStack.peek().equals(Nesting.SWITCH_EXPRESSION);
    }

    private boolean currentlyProcessingDoWhile() {
        return !nestingTypeStack.isEmpty() && nestingTypeStack.peek().equals(Nesting.DO_WHILE_LOOP);
    }
}