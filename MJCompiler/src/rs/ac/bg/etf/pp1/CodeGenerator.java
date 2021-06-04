package rs.ac.bg.etf.pp1;

import org.apache.log4j.Logger;
import rs.ac.bg.etf.pp1.ast.*;
import rs.ac.bg.etf.pp1.ast.VisitorAdaptor;
import rs.etf.pp1.mj.runtime.*;
import rs.etf.pp1.symboltable.*;
import rs.etf.pp1.symboltable.concepts.*;

import java.util.*;

public class CodeGenerator extends VisitorAdaptor {
    private int mainPc;
    private Stack<List<Integer>> ConditionStack = new Stack();
    private Stack<List<Integer>> NegConditionStack = new Stack();
    private Stack<List<Integer>> UnconditionalSkipStack = new Stack();
    private Stack<List<Integer>> UnconditionalRepeatStack = new Stack();
    private Stack<Integer> AddressStack = new Stack();

    public CodeGenerator() {
        Obj chr = Tab.find("chr");
        Obj ord = Tab.find("ord");
        chr.setAdr(Code.pc);
        ord.setAdr(Code.pc);
        Code.put(Code.enter);
        Code.put(1);
        Code.put(1);
        Code.put(Code.load_n);
        Code.put(Code.exit);
        Code.put(Code.return_);

        Obj len = Tab.find("len");
        len.setAdr(Code.pc);
        Code.put(Code.enter);
        Code.put(1);
        Code.put(1);
        Code.put(Code.load_n);
        Code.put(Code.arraylength);
        Code.put(Code.exit);
        Code.put(Code.return_);
    }

    public int getMainPc() {
        return mainPc;
    }

    public void visit(MatchedPrintStatement matchedPrintStatement) {
        // ToDo: semantic check
        if (matchedPrintStatement.getExpr().struct.equals(Tab.charType)) {
            Code.loadConst(1);
            Code.put(Code.bprint);
        }
        else {
            Code.loadConst(5);
            Code.put(Code.print);
        }
    }

    public void visit(MatchedReadStatement matchedReadStatement) {
        if (matchedReadStatement.getDesignator().obj.getType().equals(Tab.charType)) {
            Code.put(Code.bread);
        }
        else {
            Code.put(Code.read);
        }

        Code.store(matchedReadStatement.getDesignator().obj);
    }

    public void visit(MatchedContinueStatement matchedContinueStatement) {
        Code.putJump(0);
        UnconditionalRepeatStack.peek().add(Code.pc - 2);
    }

    public void visit(MatchedBreakStatement matchedContinueStatement) {
        Code.putJump(0);
        UnconditionalSkipStack.peek().add(Code.pc - 2);
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

        // Generate the entry
        Code.put(Code.enter);
        Code.put(voidMethodTypeNamePair.obj.getLevel());
        Code.put(voidMethodTypeNamePair.obj.getLocalSymbols().size());
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

    public void visit(TermExpression termExpression) {
        if (NegativeExpressionPrefix.class == termExpression.getExprPrefix().getClass()) {
            Code.put(Code.neg);
        }
    }

    public int getOperation(Relop relop) {
        if (RelopCEquals.class == relop.getClass()) {
            return Code.eq;
        } else if (RelopCNEquals.class == relop.getClass()) {
            return Code.ne;
        } else if (RelopGreater.class == relop.getClass()) {
            return Code.gt;
        } else if (RelopGEquals.class == relop.getClass()) {
            return Code.ge;
        } else if (RelopLess.class == relop.getClass()) {
            return Code.lt;
        } else {
            return Code.le;
        }
    }

    public void visit(RelationalCondFact relationalCondFact) {
        Class p0 = relationalCondFact.getParent().getClass();
        Class p1 = relationalCondFact.getParent().getParent().getClass();
        Class p2 = relationalCondFact.getParent().getParent().getParent().getClass();

        if ((CondTermListElement.class == p1) || (IfCondition.class == p2)) {
            // one of [1..n) expr elements
            Code.put(Code.jcc + Code.inverse[getOperation(relationalCondFact.getRelop())]);
            Code.put2(0);

            NegConditionStack.peek().add(Code.pc - 2);
        } else {
            if (ConditionStack.peek() != null) {
                Code.put(Code.jcc + getOperation(relationalCondFact.getRelop()));
                Code.put2(0);

                ConditionStack.peek().add(Code.pc - 2);
            }
            else {
                Code.put(Code.jcc + getOperation(relationalCondFact.getRelop()));
                Code.put2(AddressStack.peek() - Code.pc + 1);
            }
        }
    }

    public void visit(SingleCondFact singleCondFact) {
        Class p0 = singleCondFact.getParent().getClass();
        Class p1 = singleCondFact.getParent().getParent().getClass();
        Class p2 = singleCondFact.getParent().getParent().getParent().getClass();

        if ((CondTermListElement.class == p1) || (IfCondition.class == p2)) {
            // one of [1..n) expr elements
            Code.loadConst(0);
            Code.put(Code.jcc + Code.eq);
            Code.put2(0);

            NegConditionStack.peek().add(Code.pc - 2);
        } else {
            if (ConditionStack.peek() != null) {
                Code.loadConst(0);
                Code.put(Code.jcc + Code.ne);
                Code.put2(0);

                ConditionStack.peek().add(Code.pc - 2);
            }
            else {
                Code.loadConst(0);
                Code.put(Code.jcc + Code.ne);
                Code.put2(AddressStack.peek() - Code.pc + 1);
            }
        }
    }

    public void visit(OrConditionStart orConditionStart) {
        List<Integer> list = NegConditionStack.pop();

        for (int addr : list) {
            Code.fixup(addr);
        }

        NegConditionStack.push(new ArrayList<>());
    }

    public void visit(IfConditionStart ifConditionStart) {
        ConditionStack.push(new ArrayList<>());
        NegConditionStack.push(new ArrayList<>());
        if (MatchedIfElseStatement.class == ifConditionStart.getParent().getParent().getClass()) {
            UnconditionalSkipStack.push(new ArrayList<>());
        }
    }

    public void visit(IfConditionEnd ifConditionEnd) {
        List<Integer> list = ConditionStack.pop();

        for (int addr : list) {
            Code.fixup(addr);
        }
    }

    public void visit(IfStatementEnd ifStatementEnd) {
        List<Integer> list = NegConditionStack.pop();

        System.err.println(Code.pc + "bbb");
        System.err.println(ifStatementEnd.getParent().getClass());
        if (MatchedIfElseStatement.class == ifStatementEnd.getParent().getClass()) {
            Code.putJump(0);
            System.err.println(Code.pc - 2 + "bbb");
            UnconditionalSkipStack.peek().add(Code.pc - 2);
        }

        for (int addr : list) {
            Code.fixup(addr);
        }
    }

    public void visit(ElseStatementEnd elseStatementEnd) {
        System.err.println(Code.pc + "aaa" + UnconditionalSkipStack);
        List<Integer> list = UnconditionalSkipStack.pop();
        System.err.println(Code.pc + "aaa" + UnconditionalSkipStack);

        for (int addr : list) {
            System.err.println("aaa" + addr);
            Code.fixup(addr);
        }
    }

    public void visit(SwitchCondition switchCondition) {
        // generate
    }

    public void visit(SwitchExpressionStart switchExpressionStart) {

    }

    public void visit(SwitchCase switchCase) {
        // jmp to next switch case
    }

    public void visit(SwitchExpression switchExpression) {

    }

    public void visit(DoWhileConditionStart doWhileConditionStart) {
        List<Integer> list = UnconditionalRepeatStack.pop();

        for (int addr : list) {
            Code.fixup(addr);
        }
    }

    public void visit(DoWhileStatementStart doWhileStatementStart) {
        AddressStack.push(Code.pc);
        ConditionStack.push(null);
        NegConditionStack.push(new ArrayList<>());
        UnconditionalSkipStack.push(new ArrayList<>());
        UnconditionalRepeatStack.push(new ArrayList<>());
    }

    public void visit(DoWhileStatementEnd doWhileStatementEnd) {
        AddressStack.pop();
        ConditionStack.pop();
        List<Integer> list = NegConditionStack.pop();

        for (int addr : list) {
            Code.fixup(addr);
        }

        list = UnconditionalSkipStack.pop();

        for (int addr : list) {
            Code.fixup(addr);
        }
    }

    public void visit(TypeMethodTypeNamePair typeMethodTypeNamePair){
        if("main".equalsIgnoreCase(typeMethodTypeNamePair.getName())){
            mainPc = Code.pc;
        }

        typeMethodTypeNamePair.obj.setAdr(Code.pc);
        // Collect arguments and local variables
        SyntaxNode methodNode = typeMethodTypeNamePair.getParent().getParent();

        // Generate the entry
        Code.put(Code.enter);
        Code.put(typeMethodTypeNamePair.obj.getLevel());
        Code.put(typeMethodTypeNamePair.obj.getLocalSymbols().size());
    }

    public void visit(MethodDeclaration methodDecl){
        Code.put(Code.exit);
        Code.put(Code.return_);
    }

    public void visit(AssignmentDesignatorStatement assignment){
        Code.store(assignment.getDesignator().obj);
    }
    static int cnt = 0;
    public void visit(ScalarDesignator designator){
        SyntaxNode parent = designator.getParent();

        if(AssignmentDesignatorStatement.class != parent.getClass() &&
                MatchedReadStatement.class != parent.getClass() &&
                FunctionCallDesignator.class != parent.getClass() &&
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
                FunctionCallDesignator.class != parent.getClass() &&
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
