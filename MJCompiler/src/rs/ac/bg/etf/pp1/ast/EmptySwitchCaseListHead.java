// generated with ast extension for cup
// version 0.8
// 29/5/2021 22:10:5


package rs.ac.bg.etf.pp1.ast;

public class EmptySwitchCaseListHead extends SwitchCases {

    public EmptySwitchCaseListHead () {
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
        buffer.append("EmptySwitchCaseListHead(\n");

        buffer.append(tab);
        buffer.append(") [EmptySwitchCaseListHead]");
        return buffer.toString();
    }
}
