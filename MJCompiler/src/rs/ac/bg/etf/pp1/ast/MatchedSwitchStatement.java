// generated with ast extension for cup
// version 0.8
// 2/5/2021 3:24:42


package rs.ac.bg.etf.pp1.ast;

public class MatchedSwitchStatement extends MatchedStatement {

    private SwitchStatementStart SwitchStatementStart;
    private SwitchStatementExpression SwitchStatementExpression;
    private SwitchCases SwitchCases;

    public MatchedSwitchStatement (SwitchStatementStart SwitchStatementStart, SwitchStatementExpression SwitchStatementExpression, SwitchCases SwitchCases) {
        this.SwitchStatementStart=SwitchStatementStart;
        if(SwitchStatementStart!=null) SwitchStatementStart.setParent(this);
        this.SwitchStatementExpression=SwitchStatementExpression;
        if(SwitchStatementExpression!=null) SwitchStatementExpression.setParent(this);
        this.SwitchCases=SwitchCases;
        if(SwitchCases!=null) SwitchCases.setParent(this);
    }

    public SwitchStatementStart getSwitchStatementStart() {
        return SwitchStatementStart;
    }

    public void setSwitchStatementStart(SwitchStatementStart SwitchStatementStart) {
        this.SwitchStatementStart=SwitchStatementStart;
    }

    public SwitchStatementExpression getSwitchStatementExpression() {
        return SwitchStatementExpression;
    }

    public void setSwitchStatementExpression(SwitchStatementExpression SwitchStatementExpression) {
        this.SwitchStatementExpression=SwitchStatementExpression;
    }

    public SwitchCases getSwitchCases() {
        return SwitchCases;
    }

    public void setSwitchCases(SwitchCases SwitchCases) {
        this.SwitchCases=SwitchCases;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(SwitchStatementStart!=null) SwitchStatementStart.accept(visitor);
        if(SwitchStatementExpression!=null) SwitchStatementExpression.accept(visitor);
        if(SwitchCases!=null) SwitchCases.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(SwitchStatementStart!=null) SwitchStatementStart.traverseTopDown(visitor);
        if(SwitchStatementExpression!=null) SwitchStatementExpression.traverseTopDown(visitor);
        if(SwitchCases!=null) SwitchCases.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(SwitchStatementStart!=null) SwitchStatementStart.traverseBottomUp(visitor);
        if(SwitchStatementExpression!=null) SwitchStatementExpression.traverseBottomUp(visitor);
        if(SwitchCases!=null) SwitchCases.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("MatchedSwitchStatement(\n");

        if(SwitchStatementStart!=null)
            buffer.append(SwitchStatementStart.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(SwitchStatementExpression!=null)
            buffer.append(SwitchStatementExpression.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(SwitchCases!=null)
            buffer.append(SwitchCases.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [MatchedSwitchStatement]");
        return buffer.toString();
    }
}
