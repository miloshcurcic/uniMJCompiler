// generated with ast extension for cup
// version 0.8
// 31/4/2021 2:50:48


package rs.ac.bg.etf.pp1.ast;

public class RelopLEquals extends Relop {

    public RelopLEquals () {
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
        buffer.append("RelopLEquals(\n");

        buffer.append(tab);
        buffer.append(") [RelopLEquals]");
        return buffer.toString();
    }
}
