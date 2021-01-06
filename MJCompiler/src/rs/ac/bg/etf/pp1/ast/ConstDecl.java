// generated with ast extension for cup
// version 0.8
// 6/0/2021 3:41:52


package rs.ac.bg.etf.pp1.ast;

public class ConstDecl implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    private Type Type;
    private ConstNameValuePairs ConstNameValuePairs;

    public ConstDecl (Type Type, ConstNameValuePairs ConstNameValuePairs) {
        this.Type=Type;
        if(Type!=null) Type.setParent(this);
        this.ConstNameValuePairs=ConstNameValuePairs;
        if(ConstNameValuePairs!=null) ConstNameValuePairs.setParent(this);
    }

    public Type getType() {
        return Type;
    }

    public void setType(Type Type) {
        this.Type=Type;
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
        if(Type!=null) Type.accept(visitor);
        if(ConstNameValuePairs!=null) ConstNameValuePairs.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Type!=null) Type.traverseTopDown(visitor);
        if(ConstNameValuePairs!=null) ConstNameValuePairs.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Type!=null) Type.traverseBottomUp(visitor);
        if(ConstNameValuePairs!=null) ConstNameValuePairs.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ConstDecl(\n");

        if(Type!=null)
            buffer.append(Type.toString("  "+tab));
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
