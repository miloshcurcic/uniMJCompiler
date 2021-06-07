// generated with ast extension for cup
// version 0.8
// 7/5/2021 23:25:3


package rs.ac.bg.etf.pp1.ast;

public class Program implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    private ProgramName ProgramName;
    private DataDecls DataDecls;
    private MethodDecls MethodDecls;

    public Program (ProgramName ProgramName, DataDecls DataDecls, MethodDecls MethodDecls) {
        this.ProgramName=ProgramName;
        if(ProgramName!=null) ProgramName.setParent(this);
        this.DataDecls=DataDecls;
        if(DataDecls!=null) DataDecls.setParent(this);
        this.MethodDecls=MethodDecls;
        if(MethodDecls!=null) MethodDecls.setParent(this);
    }

    public ProgramName getProgramName() {
        return ProgramName;
    }

    public void setProgramName(ProgramName ProgramName) {
        this.ProgramName=ProgramName;
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
        if(ProgramName!=null) ProgramName.accept(visitor);
        if(DataDecls!=null) DataDecls.accept(visitor);
        if(MethodDecls!=null) MethodDecls.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ProgramName!=null) ProgramName.traverseTopDown(visitor);
        if(DataDecls!=null) DataDecls.traverseTopDown(visitor);
        if(MethodDecls!=null) MethodDecls.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ProgramName!=null) ProgramName.traverseBottomUp(visitor);
        if(DataDecls!=null) DataDecls.traverseBottomUp(visitor);
        if(MethodDecls!=null) MethodDecls.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("Program(\n");

        if(ProgramName!=null)
            buffer.append(ProgramName.toString("  "+tab));
        else
            buffer.append(tab+"  null");
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
