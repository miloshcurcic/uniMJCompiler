// generated with ast extension for cup
// version 0.8
// 11/0/2021 2:21:35


package rs.ac.bg.etf.pp1.ast;

public class BracedStatement implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    private BracedStatements BracedStatements;

    public BracedStatement (BracedStatements BracedStatements) {
        this.BracedStatements=BracedStatements;
        if(BracedStatements!=null) BracedStatements.setParent(this);
    }

    public BracedStatements getBracedStatements() {
        return BracedStatements;
    }

    public void setBracedStatements(BracedStatements BracedStatements) {
        this.BracedStatements=BracedStatements;
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
        if(BracedStatements!=null) BracedStatements.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(BracedStatements!=null) BracedStatements.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(BracedStatements!=null) BracedStatements.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("BracedStatement(\n");

        if(BracedStatements!=null)
            buffer.append(BracedStatements.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [BracedStatement]");
        return buffer.toString();
    }
}
