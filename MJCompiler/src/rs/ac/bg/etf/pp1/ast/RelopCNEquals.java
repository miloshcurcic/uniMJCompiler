// generated with ast extension for cup
// version 0.8
// 14/0/2021 0:51:49


package rs.ac.bg.etf.pp1.ast;

public class RelopCNEquals extends Relop {

    public RelopCNEquals () {
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
        buffer.append("RelopCNEquals(\n");

        buffer.append(tab);
        buffer.append(") [RelopCNEquals]");
        return buffer.toString();
    }
}
