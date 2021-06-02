// generated with ast extension for cup
// version 0.8
// 3/5/2021 1:2:57


package rs.ac.bg.etf.pp1.ast;

public class EmptyVarDeclList extends VarDecls {

    public EmptyVarDeclList () {
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("EmptyVarDeclList(\n");

        buffer.append(tab);
        buffer.append(") [EmptyVarDeclList]");
        return buffer.toString();
    }
}
