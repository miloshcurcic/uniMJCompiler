package rs.ac.bg.etf.pp1;

import org.apache.log4j.Logger;
import rs.ac.bg.etf.pp1.ast.*;
import rs.ac.bg.etf.pp1.ast.VisitorAdaptor;
import rs.etf.pp1.mj.runtime.*;
import rs.etf.pp1.symboltable.*;
import rs.etf.pp1.symboltable.concepts.*;
import rs.ac.bg.etf.pp1.CounterVisitor.FormParamCounter;
import rs.ac.bg.etf.pp1.CounterVisitor.VarCounter;

public class CodeGenerator extends VisitorAdaptor {
    private int mainPc;

    public int getMainPc() {
        return mainPc;
    }

    public void visit(MatchedPrintStatement matchedPrintStatement) {
        // ToDo: semantic check
        if (matchedPrintStatement.getExpr().struct == Tab.intType) {
            Code.loadConst(5);
            Code.put(Code.print);
        }
        else {
            Code.loadConst(1);
            Code.put(Code.bprint);
        }
    }


    public void visit(MatchedReadStatement matchedReadStatement) {
        //Code.put(Code.read);
        // ToDo: Using bread?
        Code.store(matchedReadStatement.getDesignator().obj);
    }

    public void visit(NumConstFactor numConstFactor) {
        Code.loadConst(numConstFactor.getN1());
    }

    public void visit(CharConstFactor charConstFactor) {
        Code.loadConst(charConstFactor.getC1());
    }

    public void visit(BoolConstFactor boolConstFactor) {
        Code.loadConst(boolConstFactor.getB1() ? 1 : 0);
    }

    public void visit(VoidMethodTypeNamePair voidMethodTypeNamePair) {
        if ("main".equalsIgnoreCase(voidMethodTypeNamePair.getName())) {
            mainPc = Code.pc;
        }
        voidMethodTypeNamePair.obj.setAdr(Code.pc);
        // Collect arguments and local variables
        SyntaxNode methodNode = voidMethodTypeNamePair.getParent();

        VarCounter varCnt = new VarCounter();
        methodNode.traverseTopDown(varCnt);

        FormParamCounter fpCnt = new FormParamCounter();
        methodNode.traverseTopDown(fpCnt);

        // Generate the entry
        Code.put(Code.enter);
        Code.put(fpCnt.getCount());
        Code.put(fpCnt.getCount() + varCnt.getCount());
    }

    public void visit(PostIncDesignatorStatement postIncDesignatorStatement) {
        Designator designator = postIncDesignatorStatement.getDesignator();
        if ((ScalarDesignator.class == designator.getClass()) && (designator.obj.getLevel() != 0)) {
            Code.put(Code.inc);
            Code.put(designator.obj.getAdr());
            Code.put(1);
        }
        else {
            Code.load(designator.obj);
            Code.loadConst(1);
            Code.put(Code.add);
            Code.store(designator.obj);
        }
    }

    public void visit(PostDecDesignatorStatement postDecDesignatorStatement) {
        Designator designator = postDecDesignatorStatement.getDesignator();

        if ((ScalarDesignator.class == designator.getClass()) && (designator.obj.getLevel() != 0)) {
            Code.put(Code.inc);
            Code.put(designator.obj.getAdr());
            Code.put(-1);
        }
        else {
            Code.load(designator.obj);
            Code.loadConst(1);
            Code.put(Code.sub);
            Code.store(designator.obj);
        }
    }

    public void visit(TypeMethodTypeNamePair typeMethodTypeNamePair){
        if("main".equalsIgnoreCase(typeMethodTypeNamePair.getName())){
            mainPc = Code.pc;
        }
        typeMethodTypeNamePair.obj.setAdr(Code.pc);
        // Collect arguments and local variables
        SyntaxNode methodNode = typeMethodTypeNamePair.getParent().getParent();

        VarCounter varCnt = new VarCounter();
        methodNode.traverseTopDown(varCnt);

        FormParamCounter fpCnt = new FormParamCounter();
        methodNode.traverseTopDown(fpCnt);

        // Generate the entry
        Code.put(Code.enter);
        Code.put(fpCnt.getCount());
        Code.put(fpCnt.getCount() + varCnt.getCount());
    }

    public void visit(MethodDeclaration methodDecl){
        Code.put(Code.exit);
        Code.put(Code.return_);
    }

    public void visit(AssignmentDesignatorStatement assignment){
        Code.store(assignment.getDesignator().obj);
    }

    public void visit(ScalarDesignator designator){
        SyntaxNode parent = designator.getParent();

        if(AssignmentDesignatorStatement.class != parent.getClass() &&
                MatchedReadStatement.class != parent.getClass() &&
                FunctionCall.class != parent.getClass() &&
                PostIncDesignatorStatement.class  != parent.getClass() &&
                PostDecDesignatorStatement.class  != parent.getClass()
        ) {
            Code.load(designator.obj);
        }
    }

    public void visit(ArrayAccessDesignator designator){
        SyntaxNode parent = designator.getParent();

        if(AssignmentDesignatorStatement.class != parent.getClass() &&
                MatchedReadStatement.class != parent.getClass() &&
                FunctionCall.class != parent.getClass() &&
                PostIncDesignatorStatement.class  != parent.getClass() &&
                PostDecDesignatorStatement.class  != parent.getClass()
        ) {
            Code.load(designator.obj);
        }
    }

    public void visit(ObjectAccessDesignator designator){
        SyntaxNode parent = designator.getParent();

        if(AssignmentDesignatorStatement.class != parent.getClass() &&
                MatchedReadStatement.class != parent.getClass() &&
                FunctionCall.class != parent.getClass() &&
                PostIncDesignatorStatement.class  != parent.getClass() &&
                PostDecDesignatorStatement.class  != parent.getClass()
        ) {
            Code.load(designator.obj);
        }
    }

    public void visit(FunctionCallResultFactor funcCall){
        Obj functionObj = funcCall.getFunctionCall().getFunctionCallDesignator().obj;
        int offset = functionObj.getAdr() - Code.pc;
        Code.put(Code.call);
        Code.put2(offset);
    }

    public void visit(FuncCallDesignatorStatement procCall){
        Obj functionObj = procCall.getFunctionCall().getFunctionCallDesignator().obj;
        int offset = functionObj.getAdr() - Code.pc;
        Code.put(Code.call);
        Code.put2(offset);
        if(procCall.getFunctionCall().getFunctionCallDesignator().obj.getType() != Tab.noType){
            Code.put(Code.pop);
        }
    }

    public void visit(ReturnExpr returnExpr){
        Code.put(Code.exit);
        Code.put(Code.return_);
    }

    public void visit(TermExprListElement termExpr){
        if (AddopPlus.class == termExpr.getAddop().getClass()) {
            Code.put(Code.add);
        } else {
            Code.put(Code.sub);
        }
    }

    public void visit(TermListElement termListElement) {
        if (MulopMultiply.class == termListElement.getMulop().getClass()) {
            Code.put(Code.mul);
        } else if (MulopDivide.class == termListElement.getMulop().getClass()){
            Code.put(Code.div);
        } else {
            Code.put(Code.rem);
        }
    }

    public void visit(NewObjectFactor newObjectFactor) {
        int numFields = newObjectFactor.getType().struct.getNumberOfFields();

        Code.put(Code.new_);
        Code.put(numFields == 0 ? 4 : numFields * 4);
    }

    public void visit(NewArrayObjectFactor newArrayObjectFactor) {
        int numFields = newArrayObjectFactor.getType().struct.getNumberOfFields();

        // bool 1 byte?
        Code.put(Code.newarray);
        Code.put(numFields == 0 ? 4 : numFields * 4);
    }
}
