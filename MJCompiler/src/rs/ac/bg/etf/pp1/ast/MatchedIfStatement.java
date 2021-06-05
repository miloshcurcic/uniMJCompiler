// generated with ast extension for cup
// version 0.8
// 5/5/2021 22:23:5


package rs.ac.bg.etf.pp1.ast;

public class MatchedIfStatement extends MatchedStatement {

    private IfCondition IfCondition;
    private IfStatementStart IfStatementStart;
    private Statement Statement;
    private IfStatementEnd IfStatementEnd;

    public MatchedIfStatement (IfCondition IfCondition, IfStatementStart IfStatementStart, Statement Statement, IfStatementEnd IfStatementEnd) {
        this.IfCondition=IfCondition;
        if(IfCondition!=null) IfCondition.setParent(this);
        this.IfStatementStart=IfStatementStart;
        if(IfStatementStart!=null) IfStatementStart.setParent(this);
        this.Statement=Statement;
        if(Statement!=null) Statement.setParent(this);
        this.IfStatementEnd=IfStatementEnd;
        if(IfStatementEnd!=null) IfStatementEnd.setParent(this);
    }

    public IfCondition getIfCondition() {
        return IfCondition;
    }

    public void setIfCondition(IfCondition IfCondition) {
        this.IfCondition=IfCondition;
    }

    public IfStatementStart getIfStatementStart() {
        return IfStatementStart;
    }

    public void setIfStatementStart(IfStatementStart IfStatementStart) {
        this.IfStatementStart=IfStatementStart;
    }

    public Statement getStatement() {
        return Statement;
    }

    public void setStatement(Statement Statement) {
        this.Statement=Statement;
    }

    public IfStatementEnd getIfStatementEnd() {
        return IfStatementEnd;
    }

    public void setIfStatementEnd(IfStatementEnd IfStatementEnd) {
        this.IfStatementEnd=IfStatementEnd;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(IfCondition!=null) IfCondition.accept(visitor);
        if(IfStatementStart!=null) IfStatementStart.accept(visitor);
        if(Statement!=null) Statement.accept(visitor);
        if(IfStatementEnd!=null) IfStatementEnd.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(IfCondition!=null) IfCondition.traverseTopDown(visitor);
        if(IfStatementStart!=null) IfStatementStart.traverseTopDown(visitor);
        if(Statement!=null) Statement.traverseTopDown(visitor);
        if(IfStatementEnd!=null) IfStatementEnd.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(IfCondition!=null) IfCondition.traverseBottomUp(visitor);
        if(IfStatementStart!=null) IfStatementStart.traverseBottomUp(visitor);
        if(Statement!=null) Statement.traverseBottomUp(visitor);
        if(IfStatementEnd!=null) IfStatementEnd.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("MatchedIfStatement(\n");

        if(IfCondition!=null)
            buffer.append(IfCondition.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(IfStatementStart!=null)
            buffer.append(IfStatementStart.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Statement!=null)
            buffer.append(Statement.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(IfStatementEnd!=null)
            buffer.append(IfStatementEnd.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [MatchedIfStatement]");
        return buffer.toString();
    }
}
