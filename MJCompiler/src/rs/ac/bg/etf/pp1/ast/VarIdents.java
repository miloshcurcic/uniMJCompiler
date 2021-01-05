// generated with ast extension for cup
// version 0.8
// 5/0/2021 3:41:37


package rs.ac.bg.etf.pp1.ast;

public class VarIdents implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    private VarIdents VarIdents;
    private VarIdent VarIdent;

    public VarIdents (VarIdents VarIdents, VarIdent VarIdent) {
        this.VarIdents=VarIdents;
        if(VarIdents!=null) VarIdents.setParent(this);
        this.VarIdent=VarIdent;
        if(VarIdent!=null) VarIdent.setParent(this);
    }

    public VarIdents getVarIdents() {
        return VarIdents;
    }

    public void setVarIdents(VarIdents VarIdents) {
        this.VarIdents=VarIdents;
    }

    public VarIdent getVarIdent() {
        return VarIdent;
    }

    public void setVarIdent(VarIdent VarIdent) {
        this.VarIdent=VarIdent;
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
        if(VarIdents!=null) VarIdents.accept(visitor);
        if(VarIdent!=null) VarIdent.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(VarIdents!=null) VarIdents.traverseTopDown(visitor);
        if(VarIdent!=null) VarIdent.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(VarIdents!=null) VarIdents.traverseBottomUp(visitor);
        if(VarIdent!=null) VarIdent.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("VarIdents(\n");

        if(VarIdents!=null)
            buffer.append(VarIdents.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(VarIdent!=null)
            buffer.append(VarIdent.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [VarIdents]");
        return buffer.toString();
    }
}
