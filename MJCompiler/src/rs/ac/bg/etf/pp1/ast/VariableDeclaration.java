// generated with ast extension for cup
// version 0.8
// 29/5/2021 22:10:5


package rs.ac.bg.etf.pp1.ast;

public class VariableDeclaration extends VarDecl {

    private VarDeclType VarDeclType;
    private VarIdents VarIdents;

    public VariableDeclaration (VarDeclType VarDeclType, VarIdents VarIdents) {
        this.VarDeclType=VarDeclType;
        if(VarDeclType!=null) VarDeclType.setParent(this);
        this.VarIdents=VarIdents;
        if(VarIdents!=null) VarIdents.setParent(this);
    }

    public VarDeclType getVarDeclType() {
        return VarDeclType;
    }

    public void setVarDeclType(VarDeclType VarDeclType) {
        this.VarDeclType=VarDeclType;
    }

    public VarIdents getVarIdents() {
        return VarIdents;
    }

    public void setVarIdents(VarIdents VarIdents) {
        this.VarIdents=VarIdents;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(VarDeclType!=null) VarDeclType.accept(visitor);
        if(VarIdents!=null) VarIdents.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(VarDeclType!=null) VarDeclType.traverseTopDown(visitor);
        if(VarIdents!=null) VarIdents.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(VarDeclType!=null) VarDeclType.traverseBottomUp(visitor);
        if(VarIdents!=null) VarIdents.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("VariableDeclaration(\n");

        if(VarDeclType!=null)
            buffer.append(VarDeclType.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(VarIdents!=null)
            buffer.append(VarIdents.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [VariableDeclaration]");
        return buffer.toString();
    }
}
