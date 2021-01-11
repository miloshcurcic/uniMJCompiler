// generated with ast extension for cup
// version 0.8
// 11/0/2021 1:27:33


package rs.ac.bg.etf.pp1.ast;

public class DataDeclListElement extends DataDecls {

    private DataDecls DataDecls;
    private DataDecl DataDecl;

    public DataDeclListElement (DataDecls DataDecls, DataDecl DataDecl) {
        this.DataDecls=DataDecls;
        if(DataDecls!=null) DataDecls.setParent(this);
        this.DataDecl=DataDecl;
        if(DataDecl!=null) DataDecl.setParent(this);
    }

    public DataDecls getDataDecls() {
        return DataDecls;
    }

    public void setDataDecls(DataDecls DataDecls) {
        this.DataDecls=DataDecls;
    }

    public DataDecl getDataDecl() {
        return DataDecl;
    }

    public void setDataDecl(DataDecl DataDecl) {
        this.DataDecl=DataDecl;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(DataDecls!=null) DataDecls.accept(visitor);
        if(DataDecl!=null) DataDecl.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(DataDecls!=null) DataDecls.traverseTopDown(visitor);
        if(DataDecl!=null) DataDecl.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(DataDecls!=null) DataDecls.traverseBottomUp(visitor);
        if(DataDecl!=null) DataDecl.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("DataDeclListElement(\n");

        if(DataDecls!=null)
            buffer.append(DataDecls.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(DataDecl!=null)
            buffer.append(DataDecl.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [DataDeclListElement]");
        return buffer.toString();
    }
}
