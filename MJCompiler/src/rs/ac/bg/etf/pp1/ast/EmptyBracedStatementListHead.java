// generated with ast extension for cup
// version 0.8
// 5/5/2021 22:23:5


package rs.ac.bg.etf.pp1.ast;

public class EmptyBracedStatementListHead extends BracedStatements {

    public EmptyBracedStatementListHead () {
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
        buffer.append("EmptyBracedStatementListHead(\n");

        buffer.append(tab);
        buffer.append(") [EmptyBracedStatementListHead]");
        return buffer.toString();
    }
}
