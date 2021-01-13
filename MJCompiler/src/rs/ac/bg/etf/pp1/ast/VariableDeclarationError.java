// generated with ast extension for cup
// version 0.8
// 14/0/2021 0:51:48


package rs.ac.bg.etf.pp1.ast;

public class VariableDeclarationError extends VarDecl {

    private VarIdents VarIdents;

    public VariableDeclarationError (VarIdents VarIdents) {
        this.VarIdents=VarIdents;
        if(VarIdents!=null) VarIdents.setParent(this);
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
        if(VarIdents!=null) VarIdents.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(VarIdents!=null) VarIdents.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(VarIdents!=null) VarIdents.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("VariableDeclarationError(\n");

        if(VarIdents!=null)
            buffer.append(VarIdents.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [VariableDeclarationError]");
        return buffer.toString();
    }
}
