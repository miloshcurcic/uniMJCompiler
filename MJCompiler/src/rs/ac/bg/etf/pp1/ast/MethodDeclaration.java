// generated with ast extension for cup
// version 0.8
// 7/0/2021 23:41:32


package rs.ac.bg.etf.pp1.ast;

public class MethodDeclaration extends MethodDecl {

    private MethodTypeNamePair MethodTypeNamePair;
    private MethodFormPars MethodFormPars;
    private VarDecls VarDecls;
    private Statements Statements;

    public MethodDeclaration (MethodTypeNamePair MethodTypeNamePair, MethodFormPars MethodFormPars, VarDecls VarDecls, Statements Statements) {
        this.MethodTypeNamePair=MethodTypeNamePair;
        if(MethodTypeNamePair!=null) MethodTypeNamePair.setParent(this);
        this.MethodFormPars=MethodFormPars;
        if(MethodFormPars!=null) MethodFormPars.setParent(this);
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

    public MethodFormPars getMethodFormPars() {
        return MethodFormPars;
    }

    public void setMethodFormPars(MethodFormPars MethodFormPars) {
        this.MethodFormPars=MethodFormPars;
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
        if(MethodFormPars!=null) MethodFormPars.accept(visitor);
        if(VarDecls!=null) VarDecls.accept(visitor);
        if(Statements!=null) Statements.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(MethodTypeNamePair!=null) MethodTypeNamePair.traverseTopDown(visitor);
        if(MethodFormPars!=null) MethodFormPars.traverseTopDown(visitor);
        if(VarDecls!=null) VarDecls.traverseTopDown(visitor);
        if(Statements!=null) Statements.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(MethodTypeNamePair!=null) MethodTypeNamePair.traverseBottomUp(visitor);
        if(MethodFormPars!=null) MethodFormPars.traverseBottomUp(visitor);
        if(VarDecls!=null) VarDecls.traverseBottomUp(visitor);
        if(Statements!=null) Statements.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("MethodDeclaration(\n");

        if(MethodTypeNamePair!=null)
            buffer.append(MethodTypeNamePair.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(MethodFormPars!=null)
            buffer.append(MethodFormPars.toString("  "+tab));
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
        buffer.append(") [MethodDeclaration]");
        return buffer.toString();
    }
}
