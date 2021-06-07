// generated with ast extension for cup
// version 0.8
// 7/5/2021 23:25:3


package rs.ac.bg.etf.pp1.ast;

public class NoFunctionCallActualParameters extends FuncCallActPars {

    public NoFunctionCallActualParameters () {
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
        buffer.append("NoFunctionCallActualParameters(\n");

        buffer.append(tab);
        buffer.append(") [NoFunctionCallActualParameters]");
        return buffer.toString();
    }
}
