// generated with ast extension for cup
// version 0.8
// 6/0/2021 3:41:52


package rs.ac.bg.etf.pp1.ast;

public class ParenthesizedStatementListElement extends ParenthesizedStatements {

    private ParenthesizedStatements ParenthesizedStatements;
    private SingleStatement SingleStatement;

    public ParenthesizedStatementListElement (ParenthesizedStatements ParenthesizedStatements, SingleStatement SingleStatement) {
        this.ParenthesizedStatements=ParenthesizedStatements;
        if(ParenthesizedStatements!=null) ParenthesizedStatements.setParent(this);
        this.SingleStatement=SingleStatement;
        if(SingleStatement!=null) SingleStatement.setParent(this);
    }

    public ParenthesizedStatements getParenthesizedStatements() {
        return ParenthesizedStatements;
    }

    public void setParenthesizedStatements(ParenthesizedStatements ParenthesizedStatements) {
        this.ParenthesizedStatements=ParenthesizedStatements;
    }

    public SingleStatement getSingleStatement() {
        return SingleStatement;
    }

    public void setSingleStatement(SingleStatement SingleStatement) {
        this.SingleStatement=SingleStatement;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ParenthesizedStatements!=null) ParenthesizedStatements.accept(visitor);
        if(SingleStatement!=null) SingleStatement.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ParenthesizedStatements!=null) ParenthesizedStatements.traverseTopDown(visitor);
        if(SingleStatement!=null) SingleStatement.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ParenthesizedStatements!=null) ParenthesizedStatements.traverseBottomUp(visitor);
        if(SingleStatement!=null) SingleStatement.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ParenthesizedStatementListElement(\n");

        if(ParenthesizedStatements!=null)
            buffer.append(ParenthesizedStatements.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(SingleStatement!=null)
            buffer.append(SingleStatement.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ParenthesizedStatementListElement]");
        return buffer.toString();
    }
}
