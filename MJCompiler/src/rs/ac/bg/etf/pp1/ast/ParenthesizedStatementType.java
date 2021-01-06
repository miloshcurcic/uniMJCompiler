// generated with ast extension for cup
// version 0.8
// 6/0/2021 3:41:52


package rs.ac.bg.etf.pp1.ast;

public class ParenthesizedStatementType extends Statement {

    private ParenthesizedStatement ParenthesizedStatement;

    public ParenthesizedStatementType (ParenthesizedStatement ParenthesizedStatement) {
        this.ParenthesizedStatement=ParenthesizedStatement;
        if(ParenthesizedStatement!=null) ParenthesizedStatement.setParent(this);
    }

    public ParenthesizedStatement getParenthesizedStatement() {
        return ParenthesizedStatement;
    }

    public void setParenthesizedStatement(ParenthesizedStatement ParenthesizedStatement) {
        this.ParenthesizedStatement=ParenthesizedStatement;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ParenthesizedStatement!=null) ParenthesizedStatement.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ParenthesizedStatement!=null) ParenthesizedStatement.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ParenthesizedStatement!=null) ParenthesizedStatement.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ParenthesizedStatementType(\n");

        if(ParenthesizedStatement!=null)
            buffer.append(ParenthesizedStatement.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ParenthesizedStatementType]");
        return buffer.toString();
    }
}
