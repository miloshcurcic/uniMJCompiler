// generated with ast extension for cup
// version 0.8
// 10/0/2021 3:13:34


package rs.ac.bg.etf.pp1.ast;

public class VarIdentError extends VarIdents {

    public VarIdentError () {
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
        buffer.append("VarIdentError(\n");

        buffer.append(tab);
        buffer.append(") [VarIdentError]");
        return buffer.toString();
    }
}