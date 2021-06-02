package rs.ac.bg.etf.pp1;

import org.apache.log4j.Logger;
import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.symboltable.*;
import rs.etf.pp1.symboltable.concepts.*;

import java.util.*;

public class SemanticAnalyzer extends VisitorAdaptor {
    public static final Struct boolType = new Struct(Struct.Bool);

    private final Map<String, Obj> objTemporaries = new HashMap<>();
    private final Map<String, Struct> structTemporaries = new HashMap<>();
    private static final String ThisReferenceName = "this";
    private static final String MainMethodName = "main";

    Logger log = Logger.getLogger(getClass());
    private int nVars;

    public int getnVars() {
        return nVars;
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

    private static class SwitchType {
        public static final Integer Statement = 0;
        public static final Integer Expression = 1;
    }

    private Stack<Set<Integer>> switchCurrentCasesStack = new Stack<>();
    private Stack<Integer> switchNestingTypeStack = new Stack<>();
    private Stack<Struct> switchExpressionTypeStack = new Stack<>();
    private Stack<Obj> methodInvocationStack = new Stack<>();
    private int functionNumFormalParameters = 0;
    private Stack<Integer> functionCallNumFormalParametersStack = new Stack<>();
    private Stack<Integer> functionCallNumActualParametersStack = new Stack<>();
    private int doWhileNestingLevel = 0;
    private int switchExpressionNestingLevel = 0;

    public static void initUniverseScope() {
        Tab.init();
        Tab.insert(Obj.Type, "bool", boolType);

    }

    public void visit(Program program) {
        Obj programObj = objTemporaries.get(ObjConstants.CurrentProgramName);

        Obj mainMethod = Tab.currentScope().findSymbol(MainMethodName);

        if ((mainMethod == null) || (mainMethod.getKind() != Obj.Meth) || !(mainMethod.getType().equals(Tab.noType)) || (mainMethod.getLevel() != 0)) {
            report_error("No valid main method found!", program);

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
            report_error("Unknown name \"" + type.getName() + "\" used as type!", type);
        }
        else
        {
            if (typeNode.getKind() == Obj.Type) {
                type.struct = typeNode.getType();
            }
            else
            {
                type.struct = Tab.noType;
                report_error("The symbol \"" + type.getName() + "\" is not a type!", type);
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
            report_error("Duplicate name declaration for variable \"" + varObj.getName() + "\"", scalarVarIdent);

            return;
        }

        Tab.insert(getVarKind(), scalarVarIdent.getName(), structTemporaries.get(StructConstants.CurrentVarDeclTypeName));
    }

    public void visit(ArrayVarIdent arrayVarIdent) {
        Obj varObj = Tab.currentScope().findSymbol(arrayVarIdent.getName());

        if (varObj != null) {
            report_error("Duplicate name declaration for variable \"" + varObj.getName() + "\"", arrayVarIdent);

            return;
        }

        // ToDo: Does this need fixing?
        Struct varArrayStruct = new Struct(Struct.Array, structTemporaries.get(StructConstants.CurrentVarDeclTypeName));
        Tab.insert(getVarKind(), arrayVarIdent.getName(), varArrayStruct);
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
            report_error("Duplicate name declaration for constant \"" + constObj.getName() + "\"!", info);

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
                    report_error("Duplicate name declaration for Method \"" + methodObj.getName() + "\"!", typeMethodTypeNamePair);

                    return;
                }
            } else {
                report_error("Duplicate name declaration for Method \"" + methodObj.getName() + "\"!", typeMethodTypeNamePair);

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
                    report_error("Duplicate name declaration for Method \"" + methodObj.getName() + "\"!", voidMethodTypeNamePair);

                    return;
                }
            } else {
                report_error("Duplicate name declaration for Method \"" + methodObj.getName() + "\"!", voidMethodTypeNamePair);

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

        structTemporaries.put(StructConstants.CurrentMethodReturnTypeName, returnType);
        objTemporaries.put(ObjConstants.CurrentMethodName, currentMethod);

        voidMethodTypeNamePair.obj = currentMethod;
    }

    public void visit(ScalarFormalParameter scalarFormalParameter) {
        Tab.insert(Obj.Var, scalarFormalParameter.getName(), scalarFormalParameter.getType().struct);
        functionNumFormalParameters++;
    }

    public void visit(ArrayFormalParameter arrayFormalParameter) {
        // ToDo: Does this need fixing?
        Struct arrayFormalParameterStruct = new Struct(Struct.Array, arrayFormalParameter.getType().struct);

        Tab.insert(Obj.Var, arrayFormalParameter.getName(), arrayFormalParameterStruct);
        functionNumFormalParameters++;
    }

    public void visit(MethodDeclaration methodDeclaration) {
        Obj currentMethod = objTemporaries.get(ObjConstants.CurrentMethodName);
        currentMethod.setLevel(functionNumFormalParameters);
        functionNumFormalParameters = 0;

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

    public void visit(ClassName className) {
        Obj classObj = Tab.currentScope().findSymbol(className.getName());

        if (classObj != null) {
            report_error("Duplicate name declaration for Class \"" + classObj.getName() + "\"", className);

            return;
        }

        Struct classStruct = new Struct(Struct.Class);
        Tab.insert(Obj.Type, className.getName(), classStruct);
        Tab.openScope();

        structTemporaries.put(StructConstants.CurrentClassTypeName, classStruct);
    }

    public void visit(BaseClassName baseClassName) {
        Struct baseClass = baseClassName.getType().struct;
        Struct extendedClass = structTemporaries.get(StructConstants.CurrentClassTypeName);

        if (baseClass.getKind() != Struct.Class) {
            report_error("Base class must be a Class Type!", baseClassName);

            return;
        }

        extendedClass.setElementType(baseClass);

        for (Obj baseClassMember : baseClass.getMembers()) {
            if (baseClassMember.getKind() == Obj.Fld) {
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
                    if (localParam.getName() == ThisReferenceName) {
                        Tab.insert(localParam.getKind(), localParam.getName(), currentClass);
                    }
                    else
                    {
                        Tab.insert(localParam.getKind(), localParam.getName(), localParam.getType());
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
            report_error("Undeclared symbol \"" + scalarDesignator.getName() + "\"!", scalarDesignator);
        }
        else
        {
            if ((scalarDesignatorObj.getKind() != Obj.Var) && (scalarDesignatorObj.getKind() != Obj.Con) && (scalarDesignatorObj.getKind() != Obj.Meth) && (scalarDesignatorObj.getKind() != Obj.Fld)) {
                scalarDesignator.obj = Tab.noObj;
                report_error("The symbol \"" + scalarDesignator.getName() + "\" is not carrying a value!", scalarDesignator);
            } else {
                scalarDesignator.obj = scalarDesignatorObj;
            }
        }
    }

    public void visit(ObjectAccessDesignator objectAccessDesignator) {
        Obj objectDesignatorObj = objectAccessDesignator.getDesignator().obj;

        if (objectDesignatorObj.getType().getKind() != Struct.Class) {
            objectAccessDesignator.obj = Tab.noObj;
            report_error("Invalid access, accessed designator \"" + objectDesignatorObj.getName() + "\"is not a Class reference!", objectAccessDesignator);

            return;
        }
        Obj fieldNameObj = null;

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
            report_error("Non existent Class member \"" + objectAccessDesignator.getFieldName() + "\"!", objectAccessDesignator);

            return;
        }

        objectAccessDesignator.obj = fieldNameObj;
    }

    public void visit(ArrayAccessDesignator arrayAccessDesignator) {
        Obj arrayAccessDesignatorObj = arrayAccessDesignator.getDesignator().obj;

        if (arrayAccessDesignatorObj.getType().getKind() != Struct.Array) {
            arrayAccessDesignator.obj = Tab.noObj;
            report_error("Invalid access, accessed designator \"" + arrayAccessDesignatorObj.getName() + "\" is not an Array reference!", arrayAccessDesignator);

            return;
        }

        if (!arrayAccessDesignator.getExpr().struct.equals(Tab.intType)) {
            arrayAccessDesignator.obj = Tab.noObj;
            report_error("Invalid statement, expression result is not an integer!", arrayAccessDesignator);

            return;
        }

        // ToDo: Does this need fixing, can I use a temporary object like this
        arrayAccessDesignator.obj = new Obj(Obj.Elem, arrayAccessDesignator.getDesignator().obj.getName() + "[]", arrayAccessDesignatorObj.getType().getElemType());
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
            report_error("Invalid statement, \"" + newObjectFactor.getType().getName() +  "\" is not a Class type!", newObjectFactor);

            return;
        }

        newObjectFactor.struct = newObjectFactor.getType().struct;
    }

    public void visit(NewArrayObjectFactor newArrayObjectFactor) {
        if (!newArrayObjectFactor.getExpr().struct.equals(Tab.intType)) {
            newArrayObjectFactor.struct = Tab.noType;
            report_error("Invalid statement, expression result is not an integer!", newArrayObjectFactor);

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
            report_error("Invalid statement, expression result is not an integer!", termListElement);

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
            report_error("Invalid statement, expression result is not an integer!", termExprListElement);

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
            report_error("Invalid statement, expression result is not an integer!", expr);

            return;
        }

        expr.struct = expr.getTermExpr().struct;
    }


    public void visit(RelationalCondFact relationalCondFact) {
        Struct exprResultType0 = relationalCondFact.getExpr().struct;
        Struct exprResultType1 = relationalCondFact.getExpr1().struct;

        if (!exprResultType0.compatibleWith(exprResultType1)) {
            report_error("Expression results are not compatible with each other for relational operations!", relationalCondFact);

            return;
        }

        if (((exprResultType0.getKind() == Struct.Class) || (exprResultType0.getKind() == Struct.Array)) && (!(relationalCondFact.getRelop() instanceof RelopCEquals) && !(relationalCondFact.getRelop() instanceof RelopCNEquals))) {
            report_error("Invalid relational operator for Class or Array type, only '==' and '!=' are allowed!", relationalCondFact);

            return;
        }
    }

    public void visit(SwitchStatementExpression switchStatementExpression) {
        if (!switchStatementExpression.getExpr().struct.equals(Tab.intType)) {
            report_error("Invalid statement, expression result is not an integer!", switchStatementExpression);

            return;
        }
    }

    public void visit(MatchedSwitchStatement matchedSwitchStatement) {
        switchCurrentCasesStack.pop();
        switchNestingTypeStack.pop();
    }

    public void visit(SwitchExpression switchExpression) {
        switchCurrentCasesStack.pop();
        switchNestingTypeStack.pop();
        switchExpressionNestingLevel--;

        Struct switchExpressionType = switchExpressionTypeStack.pop();

        // ToDo: Potentially add null control and have null as error type
        if (switchExpressionType == null) {
            switchExpressionType = Tab.noType;
        }

        switchExpression.struct = switchExpressionType;
    }

    public void visit(SwitchCase switchCase) {
        Set<Integer> switchCurrentCases = switchCurrentCasesStack.peek();

        if (switchCurrentCases.contains(switchCase.getNumber())) {
            report_error("Duplicate switch statement case!", switchCase);

            return;
        }

        switchCurrentCases.add(switchCase.getNumber());
    }

    public void visit(SwitchStatementStart switchStatementStart) {
        switchNestingTypeStack.push(SwitchType.Statement);
        switchCurrentCasesStack.push(new HashSet<>());
    }

    public void visit(SwitchExpressionStart switchExpressionStart) {
        switchNestingTypeStack.push(SwitchType.Expression);
        switchCurrentCasesStack.push(new HashSet<>());
        switchExpressionTypeStack.push(null);
        switchExpressionNestingLevel++;
    }

    public void visit(DoWhileStatementStart doWhileStatementStart) {
        doWhileNestingLevel++;
    }

    public void visit(DoWhileStatementEnd doWhileStatementEnd) {
        doWhileNestingLevel--;
    }

    public void visit(MatchedContinueStatement matchedContinueStatement) {
        if (!currentlyProcessingDoWhile()) {
            report_error("Continue statement can only be used within a do-while block!", matchedContinueStatement);

            return;
        }
    }

    public void visit(MatchedBreakStatement matchedBreakStatement) {
        if (!(currentlyProcessingDoWhile() || currentlyProcessingSwitchStatement())) {
            report_error("Break statement can only be used within a do-while or switch-case statement blocks!", matchedBreakStatement);

            return;
        }
    }

    public void visit(ActualParameter actualParameter) {
        Obj currentInvokedFunction = methodInvocationStack.peek();
        int functionCallCurrentParameter = functionCallNumActualParametersStack.peek();
        int functionCallNumFormalParameters = functionCallNumFormalParametersStack.peek();

        if (functionCallCurrentParameter == functionCallNumFormalParameters) {
            report_error("Invalid function call, too many actual parameters!", actualParameter);

            return;
        }

        Struct currentInvokedFunctionParameterType = actualParameter.getExpr().struct;
        Struct currentInvokedFunctionFormalParameterType = ((Obj)currentInvokedFunction.getLocalSymbols().toArray()[functionCallCurrentParameter]).getType();

        if (!checkAssignCompatibility(currentInvokedFunctionParameterType, currentInvokedFunctionFormalParameterType)) {
            report_error("Incompatable function call parameter types!", actualParameter);

            return;
        }

        functionCallNumActualParametersStack.set(functionCallNumActualParametersStack.size() - 1, functionCallCurrentParameter + 1);
    }

    public void visit(FunctionCall functionCall) {
        int functionCallCurrentParameter = functionCallNumActualParametersStack.peek();
        int functionCallNumFormalParameters = functionCallNumFormalParametersStack.peek();

        functionCall.struct = functionCall.getFunctionCallDesignator().obj.getType();

        if (functionCallCurrentParameter != functionCallNumFormalParameters) {
            report_error("Invalid function call, not enough parameters!", functionCall);

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
            report_error("Invalid access, accessed designator \"" + functionCallDesignatorObj.getName() + "\" is not a method or a global function!", functionCallDesignator);

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
            report_error("Invalid return statement, return expression type differs from the function return type!", returnExpression);

            return;
        }

        if (currentlyProcessingSwitchExpression()) {
            report_error("Invalid return statement, return statement cannot be used within a switch expression!", returnExpression);

            return;
        }
    }

    public void visit(YieldExpression yieldExpression) {
        if (!currentlyProcessingSwitchExpression()) {
            report_error("Invalid yield statement, yield statement can only be used within a switch expression!", yieldExpression);

            return;
        }

        // ToDo: Add diff type control
        if (switchExpressionTypeStack.peek() == null) {
            switchExpressionTypeStack.pop();
            switchExpressionTypeStack.push(yieldExpression.getExpr().struct);
        }
    }

    public void visit(NoReturnExpression noReturnExpression) {
        Obj currentMethod = objTemporaries.get(ObjConstants.CurrentMethodName);

        if (!currentMethod.getType().equals(Tab.noType)) {
            report_error("Invalid return statement, return expression type differs from the function return type!", noReturnExpression);

            return;
        }
    }

    public void visit(AssignmentDesignatorStatement assignmentDesignatorStatement) {
        Obj designatorObj = assignmentDesignatorStatement.getDesignator().obj;
        int designatorKind = designatorObj.getKind();

        if ((designatorKind != Obj.Fld) && (designatorKind != Obj.Var) && (designatorKind != Obj.Elem)) {
            report_error("Designator is neither Field, Variable or Array Element!", assignmentDesignatorStatement);

            return;
        }

        if (!checkAssignCompatibility(assignmentDesignatorStatement.getExpr().struct, designatorObj.getType())) {
            report_error("Designator is not compatible with the given expression!", assignmentDesignatorStatement);

            return;
        }
    }

    public void visit(PostDecDesignatorStatement postDecDesignatorStatement) {
        Obj designatorObj = postDecDesignatorStatement.getDesignator().obj;
        int designatorKind = designatorObj.getKind();

        if ((designatorKind != Obj.Fld) && (designatorKind != Obj.Var) && (designatorKind != Obj.Elem)) {
            report_error("Designator is neither Field, Variable or Array Element!", postDecDesignatorStatement);

            return;
        }

        if (!designatorObj.getType().equals(Tab.intType)) {
            report_error("Designator must be int type!", postDecDesignatorStatement);

            return;
        }
    }

    public void visit(PostIncDesignatorStatement postIncDesignatorStatement) {
        Obj designatorObj = postIncDesignatorStatement.getDesignator().obj;
        int designatorKind = designatorObj.getKind();

        if ((designatorKind != Obj.Fld) && (designatorKind != Obj.Var) && (designatorKind != Obj.Elem)) {
            report_error("Designator is neither Field, Variable or Array Element!", postIncDesignatorStatement);

            return;
        }

        if (!designatorObj.getType().equals(Tab.intType)) {
            report_error("Designator must be int type!", postIncDesignatorStatement);

            return;
        }
    }

    public void visit(MatchedPrintStatement matchedPrintStatement) {
        Struct exprTypeStruct = matchedPrintStatement.getExpr().struct;

        if (!exprTypeStruct.equals(Tab.intType) && !exprTypeStruct.equals(Tab.charType) && !exprTypeStruct.equals(boolType)) {
            report_error("Invalid first parameter for print statement!", matchedPrintStatement);

            return;
        }
    }

    public void visit(MatchedReadStatement matchedReadStatement) {
        Obj designatorObj = matchedReadStatement.getDesignator().obj;
        Struct designatorType = designatorObj.getType();
        int designatorKind = designatorObj.getKind();

        if ((designatorKind != Obj.Fld) && (designatorKind != Obj.Var) && (designatorKind != Obj.Elem)) {
            report_error("Designator is neither Field, Variable or Array Element!", matchedReadStatement);

            return;
        }

        if (!designatorType.equals(Tab.intType) && !designatorType.equals(Tab.charType) && !designatorType.equals(boolType)) {
            report_error("Invalid first parameter for read statement!", matchedReadStatement);

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

    public void report_error(String message, SyntaxNode info) {
        StringBuilder msg = new StringBuilder();
        int line = (info == null) ? 0: info.getLine();

        if (line != 0) {
            msg.append("Line: ").append(line).append(" - ");
        }

        msg.append(message);

        log.error(msg.toString());
    }

    public void report_info(String message, SyntaxNode info) {
        StringBuilder msg = new StringBuilder();
        int line = (info == null) ? 0: info.getLine();

        if (line != 0) {
            msg.append("Line: ").append(line).append(" - ");
        }

        msg.append(message);

        log.info(msg.toString());
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
        return switchNestingTypeStack.peek() == SwitchType.Statement;
    }

    private boolean currentlyProcessingSwitchExpression() {
        return switchExpressionNestingLevel != 0;
    }

    private boolean currentlyProcessingDoWhile() {
        return doWhileNestingLevel != 0;
    }
}