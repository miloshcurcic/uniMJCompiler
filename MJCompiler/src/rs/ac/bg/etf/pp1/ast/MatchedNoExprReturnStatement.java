// generated with ast extension for cup
// version 0.8
// 5/0/2021 3:41:37


package rs.ac.bg.etf.pp1.ast;

public class MatchedNoExprReturnStatement extends MatchedStatement {

    public MatchedNoExprReturnStatement () {
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
        buffer.append("MatchedNoExprReturnStatement(\n");

        buffer.append(tab);
        buffer.append(") [MatchedNoExprReturnStatement]");
        return buffer.toString();
    }
}