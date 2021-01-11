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

    Logger log = Logger.getLogger(getClass());

    private static class ObjConstants {
        public static final String CurrentProgramName = "CurrentProgram";
        public static final String CurrentMethodName = "CurrentMethod";
        public static final String CurrentInvokedFunctionName = "CurrentInvokedFunction";
    }

    private static class StructConstants {
        public static final String CurrentVarDeclTypeName = "CurrentVarDeclType";
        public static final String CurrentConstDeclTypeName = "CurrentConstDeclType";
        public static final String CurrentClassTypeName = "CurrentClassType";
        public static final String CurrentBaseClassTypeName = "CurrentBaseClassType";
        public static final String CurrentMethodReturnTypeName = "CurrentMethodReturnTypeName";
    }

    private Set<Integer> switchCurrentCases = new HashSet<>();
    private int functionNumFormalParameters = 0;
    private int functionCallCurrentParameter = 0;
    private int functionCallNumFormalParameters = 0;

    public static void initUniverseScope() {
        Tab.init();
        Tab.insert(Obj.Type, "bool", boolType);

    }

    public void visit(Program program) {
        Obj programObj = objTemporaries.get(ObjConstants.CurrentProgramName);

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
            report_error("Unknown name \"" + type.getName() + "\" used as type!", type);
        }
        else
        {
            if (typeNode.getKind() == Obj.Type) {
                type.struct = typeNode.getType();
            }
            else
            {
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
        Tab.insert(getVarKind(), scalarVarIdent.getName(), structTemporaries.get(StructConstants.CurrentVarDeclTypeName));
    }

    public void visit(ArrayVarIdent arrayVarIdent) {
        // ToDo: Does this need fixing?
        Struct varArrayStruct = new Struct(Struct.Array, structTemporaries.get(StructConstants.CurrentVarDeclTypeName));
        Tab.insert(getVarKind(), arrayVarIdent.getName(), varArrayStruct);
    }

    public void visit(ConstDeclType constDeclType) {
        structTemporaries.put(StructConstants.CurrentConstDeclTypeName, constDeclType.getType().struct);
    }

    public void visit(ConstDecl constDecl) {
        structTemporaries.remove(StructConstants.CurrentConstDeclTypeName);
    }

    public void visit(NumberConstNameValuePair numberConstNameValuePair) {
        Struct constType = structTemporaries.get(StructConstants.CurrentConstDeclTypeName);

        Obj constant = Tab.insert(Obj.Con, numberConstNameValuePair.getName(), constType);
        constant.setAdr(numberConstNameValuePair.getValue());
    }

    public void visit(CharConstNameValuePair charConstNameValuePair) {
        Struct constType = structTemporaries.get(StructConstants.CurrentConstDeclTypeName);

        Obj constant = Tab.insert(Obj.Con, charConstNameValuePair.getName(), constType);
        constant.setAdr(charConstNameValuePair.getValue());
    }

    public void visit(BoolConstNameValuePair boolConstNameValuePair) {
        Struct constType = structTemporaries.get(StructConstants.CurrentConstDeclTypeName);

        Obj constant = Tab.insert(Obj.Con, boolConstNameValuePair.getName(), constType);
        constant.setAdr(boolConstNameValuePair.getValue() ? 1 : 0);
    }

    // Method

    public void visit(TypeMethodTypeNamePair typeMethodTypeNamePair) {
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
    }

    public void visit(VoidMethodTypeNamePair voidMethodTypeNamePair) {
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
    }

    public void visit(ClassDeclaration classDeclaration) {
        Struct currentClass = structTemporaries.get(StructConstants.CurrentClassTypeName);

        Tab.chainLocalSymbols(currentClass);
        Tab.closeScope();

        structTemporaries.remove(StructConstants.CurrentClassTypeName);
        structTemporaries.remove(StructConstants.CurrentBaseClassTypeName);
    }

    public void visit(ClassName className) {
        Struct classStruct = new Struct(Struct.Class);
        Tab.insert(Obj.Type, className.getName(), classStruct);
        Tab.openScope();

        structTemporaries.put(StructConstants.CurrentClassTypeName, classStruct);
    }

    public void visit(BaseClassName baseClassName) {
        // At this point BaseClass surely exist as BaseClassName is Type
        Struct baseClass = baseClassName.getType().struct;

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
            report_error("Undeclared symbol \"" + scalarDesignator.getName() + "\"!", scalarDesignator);
        }
        else
        {
            if ((scalarDesignatorObj.getKind() != Obj.Var) && (scalarDesignatorObj.getKind() != Obj.Con) && (scalarDesignatorObj.getKind() != Obj.Meth) && (scalarDesignatorObj.getKind() != Obj.Fld)) {
                report_error("The symbol \"" + scalarDesignator.getName() + "\" is not carrying a value!", scalarDesignator);
            } else {
                scalarDesignator.obj = scalarDesignatorObj;
            }
        }
    }

    public void visit(ObjectAccessDesignator objectAccessDesignator) {
        Obj objectDesignatorObj = objectAccessDesignator.getDesignator().obj;

        if (objectDesignatorObj.getType().getKind() != Struct.Class) {
            report_error("Invalid access, accessed designator \"" + objectDesignatorObj.getName() + "\"is not a Class reference!", objectAccessDesignator);

            return;
        }
        Obj fieldNameObj = null;

        // Check if we're accessing class that we're currently processing
        if (currentlyProcessingClass() && currentlyProcessingMethod() && (objectDesignatorObj.getType().equals(structTemporaries.get(StructConstants.CurrentClassTypeName)))) {
            fieldNameObj = Tab.currentScope().getOuter().getLocals().searchKey(objectAccessDesignator.getFieldName());
        }
        else
        {
            fieldNameObj = objectDesignatorObj.getType().getMembersTable().searchKey(objectAccessDesignator.getFieldName());
        }


        if (fieldNameObj == null) {
            report_error("Non existent Class member \"" + objectAccessDesignator.getFieldName() + "\"!", objectAccessDesignator);

            return;
        }

        objectAccessDesignator.obj = fieldNameObj;
    }

    public void visit(ArrayAccessDesignator arrayAccessDesignator) {
        Obj arrayAccessDesignatorObj = arrayAccessDesignator.getDesignator().obj;

        if (arrayAccessDesignatorObj.getType().getKind() != Struct.Array) {
            report_error("Invalid access, accessed designator \"" + arrayAccessDesignatorObj.getName() + "\" is not an Array reference!", arrayAccessDesignator);

            return;
        }

        if (!arrayAccessDesignator.getExpr().struct.equals(Tab.intType)) {
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
            report_error("Invalid statement, \"" + newObjectFactor.getType().getName() +  "\" is not a Class type!", newObjectFactor);

            return;
        }

        newObjectFactor.struct = newObjectFactor.getType().struct;
    }

    public void visit(NewArrayObjectFactor newArrayObjectFactor) {
        if (!newArrayObjectFactor.getExpr().struct.equals(Tab.intType)) {
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
            report_error("Invalid statement, expression result is not an integer!", termExprListElement);

            return;
        }

        termExprListElement.struct = Tab.intType;
    }

    public void visit(TermExprListHead termExprListHead) {
        termExprListHead.struct = termExprListHead.getTerm().struct;
    }

    public void visit(Expr1 expr1) {
        if (!expr1.getTermExpr().struct.equals(Tab.intType) && (expr1.getExprPrefix().getClass() != NoExpressionPrefix.class)) {
            report_error("Invalid statement, expression result is not an integer!", expr1);

            return;
        }

        expr1.struct = expr1.getTermExpr().struct;
    }

    public void visit(Expression expression) {
        expression.struct = expression.getExpr1().struct;
    }

    public void visit(TernaryExpression ternaryExpression) {
        Struct resultTypeIfTrue = ternaryExpression.getExpr11().struct;
        Struct resultTypeIfFalse = ternaryExpression.getExpr12().struct;

        if (!resultTypeIfTrue.equals(resultTypeIfFalse)) {
            report_error("Ternary expression result types must be same!", ternaryExpression);

            return;
        }

        ternaryExpression.struct = resultTypeIfTrue;
    }

    public void visit(RelationalCondFact relationalCondFact) {
        Struct exprResultType0 = relationalCondFact.getExpr().struct;
        Struct exprResultType1 = relationalCondFact.getExpr1().struct;

        if (!exprResultType0.compatibleWith(exprResultType1)) {
            report_error("Expression results are not compatible with each other for relational operations!", relationalCondFact);

            return;
        }

        if (((exprResultType0.getKind() == Struct.Class) || (exprResultType0.getKind() == Struct.Array)) && ((relationalCondFact.getRelop().getClass() == RelopCEquals.class) || (relationalCondFact.getRelop().getClass() == RelopCNEquals.class))) {
            report_error("Invalid relational operator for Class or Array type, only '==' and '!=' are allowed!", relationalCondFact);

            return;
        }
    }

    public void visit(SwitchExpression switchExpression) {
        if (!switchExpression.getExpr().struct.equals(Tab.intType)) {
            report_error("Invalid statement, expression result is not an integer!", switchExpression);

            return;
        }
    }

    public void visit(MatchedSwitchStatement matchedSwitchStatement) {
        switchCurrentCases.clear();
    }

    public void visit(SwitchCase switchCase) {
        if (switchCurrentCases.contains(switchCase.getNumber())) {
            report_error("Duplicate switch statement case!", switchCase);

            return;
        }

        switchCurrentCases.add(switchCase.getNumber());
    }

    public void visit(ActualParameter actualParameter) {
        Obj currentInvokedFunction = objTemporaries.get(ObjConstants.CurrentInvokedFunctionName);

        if (functionCallCurrentParameter == functionCallNumFormalParameters) {
            report_error("Invalid function call, too many actual parameters!", actualParameter);

            return;
        }

        Struct currentInvokedFunctionParameterType = actualParameter.getExpr().struct;
        Struct currentInvokedFunctionFormalParameterType = ((Obj)currentInvokedFunction.getLocalSymbols().toArray()[functionCallCurrentParameter]).getType();

        if (!currentInvokedFunctionFormalParameterType.compatibleWith(currentInvokedFunctionParameterType)) {
            report_error("Incompatable function call parameter types!", actualParameter);

            return;
        }

        functionCallCurrentParameter++;
    }

    public void visit(FunctionCall functionCall) {
        if (functionCallCurrentParameter != functionCallNumFormalParameters) {
            report_error("Invalid function call, not enough parameters!", functionCall);

            return;
        }

        functionCallNumFormalParameters = 0;
        functionCallCurrentParameter = 0;
        objTemporaries.remove(ObjConstants.CurrentInvokedFunctionName);

        functionCall.struct = functionCall.getFunctionCallDesignator().obj.getType();
    }

    public void visit(FunctionCallDesignator functionCallDesignator) {
        Obj functionCallDesignatorObj = functionCallDesignator.getDesignator().obj;

        if (functionCallDesignatorObj.getKind() != Obj.Meth) {
            report_error("Invalid access, accessed designator \"" + functionCallDesignatorObj.getName() + "\" is not a method or a global function!", functionCallDesignator);

            return;
        }

        functionCallNumFormalParameters = functionCallDesignatorObj.getLevel();
        objTemporaries.put(ObjConstants.CurrentInvokedFunctionName, functionCallDesignatorObj);

        // Skip implicit 'this' reference
        if (functionCallNumFormalParameters != 0) {
            Obj currentInvokedFunctionFormalParameterObj = (Obj) functionCallDesignatorObj.getLocalSymbols().toArray()[functionCallCurrentParameter];

            if (currentInvokedFunctionFormalParameterObj.getName().equals(ThisReferenceName)) {
                functionCallCurrentParameter++;
            }
        }

        functionCallDesignator.obj = functionCallDesignatorObj;
    }



    // Handler error correction

    public void visit(VariableDeclarationError variableDeclarationError) {
        report_error("Corrected variable declaration error!", variableDeclarationError);
    }

    public void visit(VarIdentError varIdentError) {
        report_error("Corrected variable identifier declaration error!", varIdentError);
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
}