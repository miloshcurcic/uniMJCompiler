// generated with ast extension for cup
// version 0.8
// 5/0/2021 3:41:37


package rs.ac.bg.etf.pp1.ast;

public class ParenthesizedStatement implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    private ParenthesizedStatements ParenthesizedStatements;

    public ParenthesizedStatement (ParenthesizedStatements ParenthesizedStatements) {
        this.ParenthesizedStatements=ParenthesizedStatements;
        if(ParenthesizedStatements!=null) ParenthesizedStatements.setParent(this);
    }

    public ParenthesizedStatements getParenthesizedStatements() {
        return ParenthesizedStatements;
    }

    public void setParenthesizedStatements(ParenthesizedStatements ParenthesizedStatements) {
        this.ParenthesizedStatements=ParenthesizedStatements;
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
        if(ParenthesizedStatements!=null) ParenthesizedStatements.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ParenthesizedStatements!=null) ParenthesizedStatements.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ParenthesizedStatements!=null) ParenthesizedStatements.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ParenthesizedStatement(\n");

        if(ParenthesizedStatements!=null)
            buffer.append(ParenthesizedStatements.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ParenthesizedStatement]");
        return buffer.toString();
    }
}
