// generated with ast extension for cup
// version 0.8
// 11/0/2021 1:27:33


package rs.ac.bg.etf.pp1.ast;

public class MatchedSwitchStatement extends MatchedStatement {

    private SwitchExpression SwitchExpression;
    private SwitchCases SwitchCases;

    public MatchedSwitchStatement (SwitchExpression SwitchExpression, SwitchCases SwitchCases) {
        this.SwitchExpression=SwitchExpression;
        if(SwitchExpression!=null) SwitchExpression.setParent(this);
        this.SwitchCases=SwitchCases;
        if(SwitchCases!=null) SwitchCases.setParent(this);
    }

    public SwitchExpression getSwitchExpression() {
        return SwitchExpression;
    }

    public void setSwitchExpression(SwitchExpression SwitchExpression) {
        this.SwitchExpression=SwitchExpression;
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
        if(SwitchExpression!=null) SwitchExpression.accept(visitor);
        if(SwitchCases!=null) SwitchCases.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(SwitchExpression!=null) SwitchExpression.traverseTopDown(visitor);
        if(SwitchCases!=null) SwitchCases.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(SwitchExpression!=null) SwitchExpression.traverseBottomUp(visitor);
        if(SwitchCases!=null) SwitchCases.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("MatchedSwitchStatement(\n");

        if(SwitchExpression!=null)
            buffer.append(SwitchExpression.toString("  "+tab));
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
