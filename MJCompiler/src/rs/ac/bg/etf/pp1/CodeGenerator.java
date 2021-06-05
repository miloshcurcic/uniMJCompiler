package rs.ac.bg.etf.pp1;

import org.apache.log4j.Logger;
import rs.ac.bg.etf.pp1.ast.*;
import rs.ac.bg.etf.pp1.ast.VisitorAdaptor;
import rs.etf.pp1.mj.runtime.*;
import rs.etf.pp1.symboltable.*;
import rs.etf.pp1.symboltable.concepts.*;

import java.util.*;
import java.util.stream.Collectors;

public class CodeGenerator extends VisitorAdaptor {
    private class Pair <T, U> {
        private T key;
        private U value;

        public Pair(T key, U value) {
            this.key = key;
            this.value = value;
        }

        public T getKey() {
            return key;
        }

        public U getValue() {
            return value;
        }
    }

    private int mainPc;
    private int dataSize;
    private int internalBase;
    private static final int numInternal = 1;
    private int objectMethodCall = 0;

    private Stack<List<Integer>> ConditionStack = new Stack();
    private Stack<List<Integer>> NegConditionStack = new Stack();
    private Stack<List<Integer>> UnconditionalSkipStack = new Stack();
    private Stack<List<Integer>> UnconditionalRepeatStack = new Stack();
    private Stack<Integer> AddressStack = new Stack();
    private Stack<List<Pair<Integer, Integer>>> SwitchMapStack = new Stack();
    private Map<String, List<Integer>> ClassMap = new HashMap();
    private Map<String, Integer> Classes = new HashMap();

    public CodeGenerator(int nVars) {
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

        this.internalBase = nVars;
        this.objectMethodCall = this.internalBase + 0;

        this.dataSize = nVars + numInternal;
    }

    public int getMainPc() {
        return mainPc;
    }
    public int getDataSize() {
        return dataSize;
    }

    public void visit(ClassName className) {
        Classes.put(className.getName(), null);
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

    public void visit(ExtendedClassDeclaration extendedClassDeclaration) {
        extendedClassDeclaration.getBaseClassName().getType().struct;

    }

    public void visit(VoidMethodTypeNamePair voidMethodTypeNamePair) {
        if ("main".equalsIgnoreCase(voidMethodTypeNamePair.getName())) {
            mainPc = Code.pc;
            // find class

            // for the love of god change this
            Obj program = Tab.currentScope().getLocals().symbols().stream().filter(obj -> obj.getKind() == Obj.Prog).collect(Collectors.toList()).get(0);
            List<Obj> classes = program.getLocalSymbols().stream().filter(obj -> obj.getKind() == Obj.Type && obj.getType().getKind() == Struct.Class).collect(Collectors.toList());

            for(Map.Entry<String, Integer> classEntry : Classes.entrySet()) {
                Collection<Obj> classMethods = classes.stream().filter(obj -> obj.getName().equals(classEntry.getKey())).collect(Collectors.toList()).get(0).getType().getMembers().stream().filter(obj -> obj.getKind() == Obj.Meth).collect(Collectors.toList());
                System.err.println(classEntry.getKey() + dataSize);
                classEntry.setValue(dataSize);

                for (Obj method : classMethods) {
                    for (char letter : method.getName().toCharArray()) {
                        Code.loadConst(letter);
                        Code.put(Code.putstatic);
                        Code.put2(dataSize++);
                    }
                    Code.loadConst(-1);
                    Code.put(Code.putstatic);
                    Code.put2(dataSize++);

                    Code.loadConst(method.getAdr());
                    Code.put(Code.putstatic);
                    Code.put2(dataSize++);
                }

                Code.loadConst(-2);
                Code.put(Code.putstatic);
                Code.put2(dataSize++);
            }
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

        if (MatchedIfElseStatement.class == ifStatementEnd.getParent().getClass()) {
            Code.putJump(0);

            UnconditionalSkipStack.peek().add(Code.pc - 2);
        }

        for (int addr : list) {
            Code.fixup(addr);
        }
    }

    public void visit(ElseStatementEnd elseStatementEnd) {
        List<Integer> list = UnconditionalSkipStack.pop();

        for (int addr : list) {
            Code.fixup(addr);
        }
    }

    public void visit(SwitchCondition switchCondition) {
        SwitchMapStack.peek().add(new Pair(switchCondition.getNumber(), Code.pc));
    }

    public void visit(SwitchStatementExpression switchStatementExpression) {
        Code.putJump(0);
        UnconditionalRepeatStack.peek().add(Code.pc - 2);
    }

    public void visit(SwitchDefaultCaseStart switchDefaultCaseStart) {
        SwitchMapStack.peek().add(new Pair(Integer.MAX_VALUE, Code.pc));
    }

    public void visit(SwitchExpression switchExpression) {
        List<Integer> list0 = UnconditionalRepeatStack.pop();

        for (Integer addr : list0) {
            Code.fixup(addr);
        }

        List<Pair<Integer, Integer>> list1 = SwitchMapStack.pop();

        for (Pair<Integer, Integer> pair : list1) {
            if (pair.getKey() != Integer.MAX_VALUE) {
                Code.put(Code.dup);
                Code.loadConst(pair.getKey());
                Code.put(Code.jcc + Code.ne);
                Code.put2(7);
            }

            Code.put(Code.pop);
            Code.putJump(pair.getValue());
        }

        List<Integer> list2 = UnconditionalSkipStack.pop();

        for (Integer addr : list2) {
            Code.fixup(addr);
        }
    }

    public void visit(SwitchExpressionStart switchExpressionStart) {
        UnconditionalRepeatStack.push(new Stack());
        UnconditionalSkipStack.push(new Stack());
        SwitchMapStack.push(new Stack());
    }

    public void visit(MatchedYieldStatement matchedYieldStatement) {
        Code.putJump(0);

        UnconditionalSkipStack.peek().add(Code.pc - 2);
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

    public void visit(Program program) {
        for(Map.Entry<String, List<Integer>> classEntry : ClassMap.entrySet()) {
            System.err.println(classEntry.getKey() + Classes.get(classEntry.getKey()));
            for (Integer addr : classEntry.getValue()) {
                Code.put2(addr, Classes.get(classEntry.getKey()) >>16);
                Code.put2(addr + 2, Classes.get(classEntry.getKey()));
            }
        }
    }

    public void visit(MethodDeclaration methodDecl){
        Code.put(Code.exit);
        Code.put(Code.return_);
    }

    public void visit(AssignmentDesignatorStatement assignment){
        if ((assignment.getDesignator().obj.getKind() == Obj.Fld) && (ScalarDesignator.class == assignment.getDesignator().getClass())) {
            Code.put(Code.load_n + 0);
        }

        Code.store(assignment.getDesignator().obj);
    }

    public void visit(ScalarDesignator designator){
        SyntaxNode parent = designator.getParent();

        if(AssignmentDesignatorStatement.class != parent.getClass() &&
                MatchedReadStatement.class != parent.getClass() &&
                FunctionCallDesignator.class != parent.getClass() &&
                PostIncDesignatorStatement.class  != parent.getClass() &&
                PostDecDesignatorStatement.class  != parent.getClass()
        ) {
            if (designator.obj.getKind() == Obj.Fld) {
                Code.put(Code.load_n + 0);
            }

            Code.load(designator.obj);

            if (ObjectAccessDesignator.class == parent.getClass() && FunctionCallDesignator.class == parent.getParent().getClass()) {
                Code.put(Code.dup);
                Code.put(Code.putstatic);
                Code.put2(objectMethodCall);
            }
        }

        if (FunctionCallDesignator.class == parent.getClass() && designator.obj.getLevel() != 0) {
            Code.put(Code.load_n + 0);

            Code.put(Code.dup);
            Code.put(Code.putstatic);
            Code.put2(objectMethodCall);
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

            if (ObjectAccessDesignator.class == parent.getClass() && FunctionCallDesignator.class == parent.getParent().getClass()) {
                Code.put(Code.dup);
                Code.put(Code.putstatic);
                Code.put2(objectMethodCall);
            }
        }
    }

    public void visit(ObjectAccessDesignator designator){
        SyntaxNode parent = designator.getParent();

        if(AssignmentDesignatorStatement.class != parent.getClass() &&
                MatchedReadStatement.class != parent.getClass() &&
                FunctionCallDesignator.class != parent.getClass() &&
                PostIncDesignatorStatement.class  != parent.getClass() &&
                PostDecDesignatorStatement.class  != parent.getClass()
        ) {
            Code.load(designator.obj);

            if (ObjectAccessDesignator.class == parent.getClass() && FunctionCallDesignator.class == parent.getParent().getClass()) {
                Code.put(Code.dup);
                Code.put(Code.putstatic);
                Code.put2(objectMethodCall);
            }
        }
    }

    public void visit(FunctionCallResultFactor funcCall){
        FunctionCallDesignator designator = funcCall.getFunctionCall().getFunctionCallDesignator();
        Obj functionObj = designator.obj;

        if (ObjectAccessDesignator.class == designator.getDesignator().getClass() || functionObj.getLevel() != 0) {
            Code.put(Code.getstatic);
            Code.put2(objectMethodCall);
            Code.put(Code.getfield);
            Code.put2(0);

            Code.put(Code.invokevirtual);
            for (char c : functionObj.getName().toCharArray()) {
                Code.put4(c);
            }
            Code.put4(-1);
        } else {
            int offset = functionObj.getAdr() - Code.pc;
            Code.put(Code.call);
            Code.put2(offset);
        }
    }

    public void visit(FuncCallDesignatorStatement procCall){
        FunctionCallDesignator designator = procCall.getFunctionCall().getFunctionCallDesignator();
        Obj functionObj = designator.obj;

        if (ObjectAccessDesignator.class == designator.getDesignator().getClass() || functionObj.getLevel() != 0) {
            Code.put(Code.getstatic);
            Code.put2(objectMethodCall);
            Code.put(Code.getfield);
            Code.put2(0);

            Code.put(Code.invokevirtual);
            for (char c : functionObj.getName().toCharArray()) {
                Code.put4(c);
            }
            Code.put4(-1);
        } else {
            int offset = functionObj.getAdr() - Code.pc;
            Code.put(Code.call);
            Code.put2(offset);
        }

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
        Code.put2(numFields == 0 ? 4 : numFields * 4);

        System.err.println("aaa");
        if (newObjectFactor.getType().struct.getKind() == Struct.Class) {
            System.err.println("aaa");
            if (ClassMap.get(newObjectFactor.getType().getName()) == null) {
                ClassMap.put(newObjectFactor.getType().getName(), new ArrayList<>());
            }

            Code.put(Code.dup);
            Code.put(Code.const_);
            Code.put4(0);
            ClassMap.get(newObjectFactor.getType().getName()).add(Code.pc - 4);

            Code.put(Code.putfield);
            Code.put2(0);
        }
    }

    public void visit(NewArrayObjectFactor newArrayObjectFactor) {
        int numFields = newArrayObjectFactor.getType().struct.getNumberOfFields();

        // bool 1 byte?
        Code.put(Code.newarray);
        Code.put(numFields == 0 ? 4 : numFields * 4);
    }
}
