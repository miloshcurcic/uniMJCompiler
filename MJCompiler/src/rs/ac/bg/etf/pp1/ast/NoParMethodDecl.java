// generated with ast extension for cup
// version 0.8
// 5/0/2021 3:41:37


package rs.ac.bg.etf.pp1.ast;

public class NoParMethodDecl extends MethodDecl {

    private MethodTypeNamePair MethodTypeNamePair;
    private VarDecls VarDecls;
    private Statements Statements;

    public NoParMethodDecl (MethodTypeNamePair MethodTypeNamePair, VarDecls VarDecls, Statements Statements) {
        this.MethodTypeNamePair=MethodTypeNamePair;
        if(MethodTypeNamePair!=null) MethodTypeNamePair.setParent(this);
        this.VarDecls=VarDecls;
        if(VarDecls!=null) VarDecls.setParent(this);
        this.Statements=Statements;
        if(Statements!=null) Statements.setParent(this);
    }

    public MethodTypeNamePair getMethodTypeNamePair() {
        return MethodTypeNamePair;
    }

    public void setMethodTypeNamePair(MethodTypeNamePair MethodTypeNamePair) {
        this.MethodTypeNamePair=MethodTypeNamePair;
    }

    public VarDecls getVarDecls() {
        return VarDecls;
    }

    public void setVarDecls(VarDecls VarDecls) {
        this.VarDecls=VarDecls;
    }

    public Statements getStatements() {
        return Statements;
    }

    public void setStatements(Statements Statements) {
        this.Statements=Statements;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(MethodTypeNamePair!=null) MethodTypeNamePair.accept(visitor);
        if(VarDecls!=null) VarDecls.accept(visitor);
        if(Statements!=null) Statements.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(MethodTypeNamePair!=null) MethodTypeNamePair.traverseTopDown(visitor);
        if(VarDecls!=null) VarDecls.traverseTopDown(visitor);
        if(Statements!=null) Statements.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(MethodTypeNamePair!=null) MethodTypeNamePair.traverseBottomUp(visitor);
        if(VarDecls!=null) VarDecls.traverseBottomUp(visitor);
        if(Statements!=null) Statements.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("NoParMethodDecl(\n");

        if(MethodTypeNamePair!=null)
            buffer.append(MethodTypeNamePair.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(VarDecls!=null)
            buffer.append(VarDecls.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Statements!=null)
            buffer.append(Statements.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [NoParMethodDecl]");
        return buffer.toString();
    }
}
