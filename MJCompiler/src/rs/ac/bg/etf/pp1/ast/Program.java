// generated with ast extension for cup
// version 0.8
// 5/0/2021 3:41:37


package rs.ac.bg.etf.pp1.ast;

public class Program implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    private String I1;
    private DataDecls DataDecls;
    private MethodDecls MethodDecls;

    public Program (String I1, DataDecls DataDecls, MethodDecls MethodDecls) {
        this.I1=I1;
        this.DataDecls=DataDecls;
        if(DataDecls!=null) DataDecls.setParent(this);
        this.MethodDecls=MethodDecls;
        if(MethodDecls!=null) MethodDecls.setParent(this);
    }

    public String getI1() {
        return I1;
    }

    public void setI1(String I1) {
        this.I1=I1;
    }

    public DataDecls getDataDecls() {
        return DataDecls;
    }

    public void setDataDecls(DataDecls DataDecls) {
        this.DataDecls=DataDecls;
    }

    public MethodDecls getMethodDecls() {
        return MethodDecls;
    }

    public void setMethodDecls(MethodDecls MethodDecls) {
        this.MethodDecls=MethodDecls;
    }

    public SyntaxNode getParent() {
        return parent;
    }

    public void setParent(SyntaxNode parent) {
        this.parent=parent;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line=line;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(DataDecls!=null) DataDecls.accept(visitor);
        if(MethodDecls!=null) MethodDecls.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(DataDecls!=null) DataDecls.traverseTopDown(visitor);
        if(MethodDecls!=null) MethodDecls.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(DataDecls!=null) DataDecls.traverseBottomUp(visitor);
        if(MethodDecls!=null) MethodDecls.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("Program(\n");

        buffer.append(" "+tab+I1);
        buffer.append("\n");

        if(DataDecls!=null)
            buffer.append(DataDecls.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(MethodDecls!=null)
            buffer.append(MethodDecls.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [Program]");
        return buffer.toString();
    }
}