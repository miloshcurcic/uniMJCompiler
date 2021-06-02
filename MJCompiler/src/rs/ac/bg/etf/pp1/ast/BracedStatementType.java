// generated with ast extension for cup
// version 0.8
// 3/5/2021 1:2:58


package rs.ac.bg.etf.pp1.ast;

public class BracedStatementType extends Statement {

    private BracedStatement BracedStatement;

    public BracedStatementType (BracedStatement BracedStatement) {
        this.BracedStatement=BracedStatement;
        if(BracedStatement!=null) BracedStatement.setParent(this);
    }

    public BracedStatement getBracedStatement() {
        return BracedStatement;
    }

    public void setBracedStatement(BracedStatement BracedStatement) {
        this.BracedStatement=BracedStatement;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(BracedStatement!=null) BracedStatement.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(BracedStatement!=null) BracedStatement.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(BracedStatement!=null) BracedStatement.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("BracedStatementType(\n");

        if(BracedStatement!=null)
            buffer.append(BracedStatement.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [BracedStatementType]");
        return buffer.toString();
    }
}
