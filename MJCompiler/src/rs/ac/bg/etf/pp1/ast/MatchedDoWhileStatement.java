// generated with ast extension for cup
// version 0.8
// 27/5/2021 20:16:11


package rs.ac.bg.etf.pp1.ast;

public class MatchedDoWhileStatement extends MatchedStatement {

    private DoWhileStatementStart DoWhileStatementStart;
    private Statement Statement;
    private DoWhileCondition DoWhileCondition;
    private DoWhileStatementEnd DoWhileStatementEnd;

    public MatchedDoWhileStatement (DoWhileStatementStart DoWhileStatementStart, Statement Statement, DoWhileCondition DoWhileCondition, DoWhileStatementEnd DoWhileStatementEnd) {
        this.DoWhileStatementStart=DoWhileStatementStart;
        if(DoWhileStatementStart!=null) DoWhileStatementStart.setParent(this);
        this.Statement=Statement;
        if(Statement!=null) Statement.setParent(this);
        this.DoWhileCondition=DoWhileCondition;
        if(DoWhileCondition!=null) DoWhileCondition.setParent(this);
        this.DoWhileStatementEnd=DoWhileStatementEnd;
        if(DoWhileStatementEnd!=null) DoWhileStatementEnd.setParent(this);
    }

    public DoWhileStatementStart getDoWhileStatementStart() {
        return DoWhileStatementStart;
    }

    public void setDoWhileStatementStart(DoWhileStatementStart DoWhileStatementStart) {
        this.DoWhileStatementStart=DoWhileStatementStart;
    }

    public Statement getStatement() {
        return Statement;
    }

    public void setStatement(Statement Statement) {
        this.Statement=Statement;
    }

    public DoWhileCondition getDoWhileCondition() {
        return DoWhileCondition;
    }

    public void setDoWhileCondition(DoWhileCondition DoWhileCondition) {
        this.DoWhileCondition=DoWhileCondition;
    }

    public DoWhileStatementEnd getDoWhileStatementEnd() {
        return DoWhileStatementEnd;
    }

    public void setDoWhileStatementEnd(DoWhileStatementEnd DoWhileStatementEnd) {
        this.DoWhileStatementEnd=DoWhileStatementEnd;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(DoWhileStatementStart!=null) DoWhileStatementStart.accept(visitor);
        if(Statement!=null) Statement.accept(visitor);
        if(DoWhileCondition!=null) DoWhileCondition.accept(visitor);
        if(DoWhileStatementEnd!=null) DoWhileStatementEnd.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(DoWhileStatementStart!=null) DoWhileStatementStart.traverseTopDown(visitor);
        if(Statement!=null) Statement.traverseTopDown(visitor);
        if(DoWhileCondition!=null) DoWhileCondition.traverseTopDown(visitor);
        if(DoWhileStatementEnd!=null) DoWhileStatementEnd.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(DoWhileStatementStart!=null) DoWhileStatementStart.traverseBottomUp(visitor);
        if(Statement!=null) Statement.traverseBottomUp(visitor);
        if(DoWhileCondition!=null) DoWhileCondition.traverseBottomUp(visitor);
        if(DoWhileStatementEnd!=null) DoWhileStatementEnd.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("MatchedDoWhileStatement(\n");

        if(DoWhileStatementStart!=null)
            buffer.append(DoWhileStatementStart.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Statement!=null)
            buffer.append(Statement.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(DoWhileCondition!=null)
            buffer.append(DoWhileCondition.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(DoWhileStatementEnd!=null)
            buffer.append(DoWhileStatementEnd.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [MatchedDoWhileStatement]");
        return buffer.toString();
    }
}
