// generated with ast extension for cup
// version 0.8
// 25/5/2021 21:0:8


package rs.ac.bg.etf.pp1.ast;

public class SwitchCase implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    private SwitchCondition SwitchCondition;
    private Statements Statements;

    public SwitchCase (SwitchCondition SwitchCondition, Statements Statements) {
        this.SwitchCondition=SwitchCondition;
        if(SwitchCondition!=null) SwitchCondition.setParent(this);
        this.Statements=Statements;
        if(Statements!=null) Statements.setParent(this);
    }

    public SwitchCondition getSwitchCondition() {
        return SwitchCondition;
    }

    public void setSwitchCondition(SwitchCondition SwitchCondition) {
        this.SwitchCondition=SwitchCondition;
    }

    public Statements getStatements() {
        return Statements;
    }

    public void setStatements(Statements Statements) {
        this.Statements=Statements;
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
        if(SwitchCondition!=null) SwitchCondition.accept(visitor);
        if(Statements!=null) Statements.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(SwitchCondition!=null) SwitchCondition.traverseTopDown(visitor);
        if(Statements!=null) Statements.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(SwitchCondition!=null) SwitchCondition.traverseBottomUp(visitor);
        if(Statements!=null) Statements.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("SwitchCase(\n");

        if(SwitchCondition!=null)
            buffer.append(SwitchCondition.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Statements!=null)
            buffer.append(Statements.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [SwitchCase]");
        return buffer.toString();
    }
}
