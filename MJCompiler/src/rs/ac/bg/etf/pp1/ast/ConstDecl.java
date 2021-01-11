// generated with ast extension for cup
// version 0.8
// 11/0/2021 2:21:35


package rs.ac.bg.etf.pp1.ast;

public class ConstDecl implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    private ConstDeclType ConstDeclType;
    private ConstNameValuePairs ConstNameValuePairs;

    public ConstDecl (ConstDeclType ConstDeclType, ConstNameValuePairs ConstNameValuePairs) {
        this.ConstDeclType=ConstDeclType;
        if(ConstDeclType!=null) ConstDeclType.setParent(this);
        this.ConstNameValuePairs=ConstNameValuePairs;
        if(ConstNameValuePairs!=null) ConstNameValuePairs.setParent(this);
    }

    public ConstDeclType getConstDeclType() {
        return ConstDeclType;
    }

    public void setConstDeclType(ConstDeclType ConstDeclType) {
        this.ConstDeclType=ConstDeclType;
    }

    public ConstNameValuePairs getConstNameValuePairs() {
        return ConstNameValuePairs;
    }

    public void setConstNameValuePairs(ConstNameValuePairs ConstNameValuePairs) {
        this.ConstNameValuePairs=ConstNameValuePairs;
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
        if(ConstDeclType!=null) ConstDeclType.accept(visitor);
        if(ConstNameValuePairs!=null) ConstNameValuePairs.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ConstDeclType!=null) ConstDeclType.traverseTopDown(visitor);
        if(ConstNameValuePairs!=null) ConstNameValuePairs.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ConstDeclType!=null) ConstDeclType.traverseBottomUp(visitor);
        if(ConstNameValuePairs!=null) ConstNameValuePairs.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ConstDecl(\n");

        if(ConstDeclType!=null)
            buffer.append(ConstDeclType.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ConstNameValuePairs!=null)
            buffer.append(ConstNameValuePairs.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ConstDecl]");
        return buffer.toString();
    }
}
