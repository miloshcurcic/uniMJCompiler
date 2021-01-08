package rs.ac.bg.etf.pp1;

import org.apache.log4j.Logger;
import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.symboltable.*;
import rs.etf.pp1.symboltable.concepts.*;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

public class SemanticAnalyzer extends VisitorAdaptor {
    private final Map<String, Obj> objTemporaries = new HashMap<>();
    private final Map<String, Struct> structTemporaries = new HashMap<>();

    Logger log = Logger.getLogger(getClass());

    private static class ObjConstants {
        public static final String CurrentProgramName = "CurrentProgram";
        public static final String CurrentMethodName = "CurrentMethod";
    }

    private static class StructConstants {
        public static final String CurrentVarDeclTypeName = "CurrentVarDeclType";
        public static final String CurrentConstDeclTypeName = "CurrentConstDeclType";
        public static final String CurrentClassTypeName = "CurrentClassType";
        public static final String CurrentMethodReturnTypeName = "CurrentMethodReturnTypeName";
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

        if (typeNode == Tab.noObj) {
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

    public void visit(VarDecl varDecl) {
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

        Tab.insert(Obj.Con, numberConstNameValuePair.getName(), constType);
    }

    public void visit(CharConstNameValuePair charConstNameValuePair) {
        Struct constType = structTemporaries.get(StructConstants.CurrentConstDeclTypeName);

        Tab.insert(Obj.Con, charConstNameValuePair.getName(), constType);
    }

    public void visit(BoolConstNameValuePair boolConstNameValuePair) {
        Struct constType = structTemporaries.get(StructConstants.CurrentConstDeclTypeName);

        Tab.insert(Obj.Con, boolConstNameValuePair.getName(), constType);
    }

    // Method

    public void visit(TypeMethodTypeNamePair typeMethodTypeNamePair) {
        Struct returnType = typeMethodTypeNamePair.getType().struct;

        Obj currentMethod = Tab.insert(Obj.Meth, typeMethodTypeNamePair.getName(), returnType);
        Tab.openScope();

        structTemporaries.put(StructConstants.CurrentMethodReturnTypeName, returnType);
        objTemporaries.put(ObjConstants.CurrentMethodName, currentMethod);
    }

    public void visit(VoidMethodTypeNamePair voidMethodTypeNamePair) {
        Struct returnType = Tab.noType;

        Obj currentMethod = Tab.insert(Obj.Meth, voidMethodTypeNamePair.getName(), Tab.noType);
        Tab.openScope();

        structTemporaries.put(StructConstants.CurrentMethodReturnTypeName, returnType);
        objTemporaries.put(ObjConstants.CurrentMethodName, currentMethod);
    }

    public void visit(ScalarFormalParameter scalarFormalParameter) {
        structTemporaries.put(StructConstants.CurrentVarDeclTypeName, scalarFormalParameter.getType().struct);
    }

    public void visit(ArrayFormalParameter arrayFormalParameter) {
        // ToDo: Does this need fixing?
        Struct arrayFormalParameterStruct = new Struct(Struct.Array, arrayFormalParameter.getType().struct);

        structTemporaries.put(StructConstants.CurrentVarDeclTypeName, arrayFormalParameterStruct);
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
    }

    public void visit(ClassDeclaration classDeclaration) {
        Struct currentClass = structTemporaries.get(StructConstants.CurrentClassTypeName);

        Tab.chainLocalSymbols(currentClass);
        Tab.closeScope();

        structTemporaries.remove(StructConstants.CurrentClassTypeName);
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
        Struct extendedClass = structTemporaries.get(StructConstants.CurrentClassTypeName);

        for (Obj baseClassMember : baseClass.getMembers()) {
            // ToDo: should I split this to two? Fields and Methods?
            Obj inheritedClassMember = Tab.insert(baseClassMember.getKind(), baseClassMember.getName(), baseClassMember.getType());

            if (baseClassMember.getKind() == Obj.Meth) {
                Tab.openScope();

                for (Obj baseClassMethodLocal : baseClassMember.getLocalSymbols()) {
                    // ToDo: fix this
                    Tab.insert(baseClassMethodLocal.getKind(), baseClassMethodLocal.getName(), baseClassMethodLocal.getType());
                }

                Tab.chainLocalSymbols(inheritedClassMember);
                Tab.closeScope();
            }
        }
    }

    // checks

    public void visit(ScalarDesignator scalarDesignator) {
        Obj scalarDesignatorObj = Tab.find(scalarDesignator.getName());

        if (scalarDesignatorObj == Tab.noObj) {
            report_error("Undeclared symbol \"" + scalarDesignator.getName() + "\"!", scalarDesignator);
        }
        else
        {
            if (scalarDesignatorObj.getKind() != Obj.Var) {
                report_error("The symbol \"" + scalarDesignator.getName() + "\" is not a variable!", scalarDesignator);
            } else {
                scalarDesignator.obj = scalarDesignatorObj;
            }
        }
    }

    public void visit(ObjectAccessDesignator objectAccessDesignator) {
        Struct objectDesignatorType = objectAccessDesignator.obj.getType();

        if (objectDesignatorType.getKind() != Struct.Class) {
            report_error("Invalid access, accessed designator \"" + objectAccessDesignator.obj.getName() + "\"is not a Class reference!", objectAccessDesignator);

            return;
        }

        Obj fieldNameObj = objectDesignatorType.getMembersTable().searchKey(objectAccessDesignator.getFieldName());

        if (fieldNameObj == Tab.noObj) {
            report_error("Non existent Class member \"" + objectAccessDesignator.getFieldName() + "\"!", objectAccessDesignator);

            return;
        }

        objectAccessDesignator.obj = fieldNameObj;
    }

    public void visit(ArrayAccessDesignator arrayAccessDesignator) {
        Struct arrayDesignatorType = arrayAccessDesignator.obj.getType();

        if (arrayDesignatorType.getKind() != Struct.Array) {
            report_error("Invalid access, accessed designator \"" + arrayAccessDesignator.obj.getName() + "\" is not an Array reference!", arrayAccessDesignator);

            return;
        }

        // ToDo: Check expression
    }

    public void visit(VarFactor varFactor) {

    }

    public  void visit(FunctionCallResultFactor functionCallResultFactor) {
        Obj functionCallRDesignatorObj = functionCallResultFactor.getDesignator().obj;

        if (functionCallRDesignatorObj.getKind() != Obj.Meth) {
            report_error("Invalid access, accessed designator \"" + functionCallRDesignatorObj.getName() + "\" is not a method or a global function!", functionCallResultFactor);

            return;
        }

        functionCallResultFactor.struct = functionCallRDesignatorObj.getType();
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
        if (newArrayObjectFactor.getExpr().struct != Tab.intType) {
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
        if (termListElement.struct != Tab.intType || termListElement.getTerm().struct != Tab.intType) {
            report_error("Invalid statement, expression result is not an integer!", termListElement);

            return;
        }

        termListElement.struct = Tab.intType;
    }

    public void visit(TermListHead termListHead) {
        termListHead.struct = termListHead.getFactor().struct;
    }

    public void visit(TermExprListElement termExprListElement) {
        if (termExprListElement.struct != Tab.intType || termExprListElement.getTerm().struct != Tab.intType) {
            report_error("Invalid statement, expression result is not an integer!", termExprListElement);

            return;
        }

        termExprListElement.struct = Tab.intType;
    }

    public void visit(TermExprListHead termExprListHead) {
        termExprListHead.struct = termExprListHead.getTerm().struct;
    }

    public void visit(Expr1 expr1) {
        if (expr1.struct != Tab.intType || expr1.getTermExpr().struct != Tab.intType) {
            report_error("Invalid statement, expression result is not an integer!", expr1);

            return;
        }

        expr1.struct = Tab.intType;
    }

    public void visit(Expression expression) {
        expression.struct = expression.getExpr1().struct;
    }

    public void visit(TernaryExpression ternaryExpression) {
        Struct resultTypeIfTrue = ternaryExpression.getExpr11().struct;
        Struct resultTypeIfFalse = ternaryExpression.getExpr12().struct;

        if (resultTypeIfTrue != resultTypeIfFalse) {
            report_error("Ternary expression result types must be same!", ternaryExpression);

            return;
        }
    }



    // Helper methods

    private int getVarKind() {
        int varKind = Obj.Var;

        // If we are processing a class and we're not processing a method class member this is a class field
        if ((structTemporaries.get(StructConstants.CurrentClassTypeName) != null) && (objTemporaries.get(ObjConstants.CurrentMethodName) == null)) {
            varKind = Obj.Fld;
        }

        return varKind;
    }

    public void report_error(String message, SyntaxNode info) {
        StringBuilder msg = new StringBuilder(message);
        int line = (info == null) ? 0: info.getLine();
        if (line != 0)
            msg.append (" na liniji ").append(line);
        log.error(msg.toString());
    }

    public void report_info(String message, SyntaxNode info) {
        StringBuilder msg = new StringBuilder(message);
        int line = (info == null) ? 0: info.getLine();
        if (line != 0)
            msg.append (" na liniji ").append(line);
        log.info(msg.toString());
    }

}
