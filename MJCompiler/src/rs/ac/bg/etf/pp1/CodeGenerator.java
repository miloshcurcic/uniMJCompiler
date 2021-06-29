package rs.ac.bg.etf.pp1;

import rs.ac.bg.etf.pp1.ast.*;
import rs.ac.bg.etf.pp1.ast.VisitorAdaptor;
import rs.ac.bg.etf.pp1.error.Errorable;
import rs.ac.bg.etf.pp1.test.CompilerError;
import rs.ac.bg.etf.pp1.util.CustomDumpSymbolTableVisitor;
import rs.etf.pp1.mj.runtime.*;
import rs.etf.pp1.symboltable.*;
import rs.etf.pp1.symboltable.concepts.*;

import java.util.*;
import java.util.stream.Collectors;

public class CodeGenerator extends VisitorAdaptor {
    private static class Pair <T, U> {
        private final T key;
        private final U value;

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
    private static final int numInternal = 1;
    private final int objectMethodCall;

    private final Stack<List<Integer>> ConditionStack = new Stack<>();
    private final Stack<List<Integer>> NegConditionStack = new Stack<>();
    private final Stack<List<Integer>> UnconditionalSkipStack = new Stack<>();
    private final Stack<List<Integer>> UnconditionalRepeatStack = new Stack<>();
    private final Stack<Integer> AddressStack = new Stack<>();
    private final Stack<List<Pair<Integer, Integer>>> SwitchMapStack = new Stack<>();
    private final Map<String, List<Integer>> ClassMap = new HashMap<>();
    private final Map<String, Integer> Classes = new HashMap<>();

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

        this.objectMethodCall = nVars;

        this.dataSize = nVars + numInternal;
    }

    public int getMainPc() {
        return mainPc;
    }
    public int getDataSize() {
        return dataSize;
    }

    public void visit(ClassName className) {
        Classes.put(className.getClassType().getName(), null);
        SyntaxNode parent = className.getParent();

        if (ExtendedClassDeclaration.class == parent.getClass()) {
            ExtendedClassDeclaration extendedClassDeclaration = (ExtendedClassDeclaration) parent;

            Struct baseClass = ((BaseClassName)extendedClassDeclaration.getBaseClassType()).getType().struct;
            Struct newClass = extendedClassDeclaration.getClassName().getClassType().struct;

            List<Obj> baseClassMethods = baseClass.getMembers().stream().filter(obj -> obj.getKind() == Obj.Meth).collect(Collectors.toList());
            List<Obj> newClassMethods = newClass.getMembers().stream().filter(obj -> obj.getKind() == Obj.Meth).collect(Collectors.toList());

            for (int i=0; i < baseClassMethods.size(); i++) {
                newClassMethods.get(i).setAdr(baseClassMethods.get(i).getAdr());
            }
        }
    }

    public void visit(MatchedPrintStatement matchedPrintStatement) {
        if (matchedPrintStatement.getPrintStatementAdditionalParam().getClass().equals(PrintStatementAdditionalParameter.class)) {
            Code.loadConst(((PrintStatementAdditionalParameter)matchedPrintStatement.getPrintStatementAdditionalParam()).getN1());

            if (matchedPrintStatement.getExpr().struct.equals(Tab.charType)) {
                Code.put(Code.bprint);
            } else {
                Code.put(Code.print);
            }
        }
        else {
            if (matchedPrintStatement.getExpr().struct.equals(Tab.charType)) {
                Code.loadConst(1);
                Code.put(Code.bprint);
            } else {
                Code.loadConst(5);
                Code.put(Code.print);
            }
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

    public void visit(MethodStatements methodStatements) {
        MethodDeclaration methodDeclaration = (MethodDeclaration)methodStatements.getParent();
        MethodTypeNamePair methodTypeNamePair = methodDeclaration.getMethodTypeNamePair();

        if (methodTypeNamePair.getClass().equals(TypeMethodTypeNamePair.class)) {
            Code.put(Code.trap);
            Code.put(0);
        }
        else {
            Code.put(Code.exit);
            Code.put(Code.return_);
        }
    }

    public void visit(MatchedReturnStatement methodStatements) {
        Code.put(Code.exit);
        Code.put(Code.return_);
    }

    public void visit(SwitchDefaultCase switchDefaultCase) {
        Code.put(Code.trap);
        Code.put(1);
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

    public void visit(NegTermExprListHead negTermExprListHead) {
        Code.put(Code.neg);
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
        Class<? extends SyntaxNode> p1 = relationalCondFact.getParent().getParent().getClass();
        Class<? extends SyntaxNode> p2 = relationalCondFact.getParent().getParent().getParent().getClass();

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
        Class<? extends SyntaxNode> p1 = singleCondFact.getParent().getParent().getClass();
        Class<? extends SyntaxNode> p2 = singleCondFact.getParent().getParent().getParent().getClass();

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

        for (int address : list) {
            Code.fixup(address);
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

        for (int address : list) {
            Code.fixup(address);
        }
    }

    public void visit(IfStatementEnd ifStatementEnd) {
        List<Integer> list = NegConditionStack.pop();

        if (MatchedIfElseStatement.class == ifStatementEnd.getParent().getClass()) {
            Code.putJump(0);

            UnconditionalSkipStack.peek().add(Code.pc - 2);
        }

        for (int address : list) {
            Code.fixup(address);
        }
    }

    public void visit(ElseStatementEnd elseStatementEnd) {
        List<Integer> list = UnconditionalSkipStack.pop();

        for (int address : list) {
            Code.fixup(address);
        }
    }

    public void visit(SwitchCondition switchCondition) {
        SwitchMapStack.peek().add(new Pair<>(switchCondition.getNumber(), Code.pc));
    }

    public void visit(SwitchStatementExpression switchStatementExpression) {
        Code.putJump(0);
        UnconditionalRepeatStack.peek().add(Code.pc - 2);
    }

    public void visit(SwitchDefaultCaseStart switchDefaultCaseStart) {
        SwitchMapStack.peek().add(new Pair<>(Integer.MAX_VALUE, Code.pc));
    }

    public void visit(SwitchExpression switchExpression) {
        List<Integer> list0 = UnconditionalRepeatStack.pop();

        for (Integer address : list0) {
            Code.fixup(address);
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

        for (Integer address : list2) {
            Code.fixup(address);
        }
    }

    public void visit(SwitchExpressionStart switchExpressionStart) {
        UnconditionalRepeatStack.push(new ArrayList<>());
        UnconditionalSkipStack.push(new ArrayList<>());
        SwitchMapStack.push(new ArrayList<>());
    }

    public void visit(MatchedYieldStatement matchedYieldStatement) {
        Code.putJump(0);

        UnconditionalSkipStack.peek().add(Code.pc - 2);
    }

    public void visit(DoWhileConditionStart doWhileConditionStart) {
        List<Integer> list = UnconditionalRepeatStack.pop();

        for (int address : list) {
            Code.fixup(address);
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

        for (int address : list) {
            Code.fixup(address);
        }

        list = UnconditionalSkipStack.pop();

        for (int address : list) {
            Code.fixup(address);
        }
    }

    public void visit(TypeMethodTypeNamePair typeMethodTypeNamePair){
        if("main".equalsIgnoreCase(typeMethodTypeNamePair.getName())){
            mainPc = Code.pc;
        }

        typeMethodTypeNamePair.obj.setAdr(Code.pc);

        // Generate the entry
        Code.put(Code.enter);
        Code.put(typeMethodTypeNamePair.obj.getLevel());
        Code.put(typeMethodTypeNamePair.obj.getLocalSymbols().size());
    }

    public void visit(Program program) {
        for(Map.Entry<String, List<Integer>> classEntry : ClassMap.entrySet()) {
            for (Integer address : classEntry.getValue()) {
                Code.put2(address, Classes.get(classEntry.getKey()) >>16);
                Code.put2(address + 2, Classes.get(classEntry.getKey()));
            }
        }
    }

    public void visit(AssignmentStatement assignment){
        Code.store(assignment.getDesignator().obj);
    }

    public void visit(ScalarDesignator designator){
        SyntaxNode parent = designator.getParent();

        if(AssignmentStatement.class != parent.getClass() &&
                MatchedReadStatement.class != parent.getClass() &&
                FunctionCallDesignator.class != parent.getClass() &&
                PostIncDesignatorStatement.class  != parent.getClass() &&
                PostDecDesignatorStatement.class  != parent.getClass()
        ) {
            if (designator.obj.getKind() == Obj.Fld) {
                Code.put(Code.load_n /* + 0 */);
            }

            Code.load(designator.obj);

            if (ObjectAccessDesignator.class == parent.getClass() && FunctionCallDesignator.class == parent.getParent().getClass()) {
                Code.put(Code.dup);
                Code.put(Code.putstatic);
                Code.put2(objectMethodCall);
            }
        }

        Optional<Obj> firstLocalSymbol = designator.obj.getLocalSymbols().stream().findFirst();

        if (FunctionCallDesignator.class == parent.getClass() && firstLocalSymbol.isPresent() && firstLocalSymbol.get().getName().equals("this")) {
            Code.put(Code.load_n /* + 0 */);

            Code.put(Code.dup);
            Code.put(Code.putstatic);
            Code.put2(objectMethodCall);
        }

        if ((designator.obj.getKind() == Obj.Fld) && (AssignmentStatement.class == parent.getClass())) {
            Code.put(Code.load_n /* + 0 */);
        }
    }

    public void visit(ArrayAccessDesignator designator){
        SyntaxNode parent = designator.getParent();

        if(AssignmentStatement.class != parent.getClass() &&
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

        if(AssignmentStatement.class != parent.getClass() &&
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

        Optional<Obj> firstLocalSymbol = functionObj.getLocalSymbols().stream().findFirst();

        if (ObjectAccessDesignator.class == designator.getDesignator().getClass() || firstLocalSymbol.isPresent() && firstLocalSymbol.get().getName().equals("this")) {
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
        Optional<Obj> firstLocalSymbol = functionObj.getLocalSymbols().stream().findFirst();

        if (ObjectAccessDesignator.class == designator.getDesignator().getClass() || firstLocalSymbol.isPresent() && firstLocalSymbol.get().getName().equals("this")) {
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

        if (newObjectFactor.getType().struct.getKind() == Struct.Class) {
            ClassMap.computeIfAbsent(newObjectFactor.getType().getName(), k -> new ArrayList<>());

            Code.put(Code.dup);
            Code.put(Code.const_);
            Code.put4(0);
            ClassMap.get(newObjectFactor.getType().getName()).add(Code.pc - 4);

            Code.put(Code.putfield);
            Code.put2(0);
        }
    }

    public void visit(NewArrayObjectFactor newArrayObjectFactor) {
        Code.put(Code.newarray);

        if (newArrayObjectFactor.struct.getElemType().equals(Tab.charType)) {
            Code.put(0);
        } else {
            Code.put(1);
        }
    }
}
