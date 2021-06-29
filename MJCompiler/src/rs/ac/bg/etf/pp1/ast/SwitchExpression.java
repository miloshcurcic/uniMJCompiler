// generated with ast extension for cup
// version 0.8
// 29/5/2021 22:10:5


package rs.ac.bg.etf.pp1.ast;

public class SwitchExpression extends Expr {

    private SwitchExpressionStart SwitchExpressionStart;
    private SwitchStatementExpression SwitchStatementExpression;
    private SwitchCases SwitchCases;
    private SwitchDefaultCase SwitchDefaultCase;

    public SwitchExpression (SwitchExpressionStart SwitchExpressionStart, SwitchStatementExpression SwitchStatementExpression, SwitchCases SwitchCases, SwitchDefaultCase SwitchDefaultCase) {
        this.SwitchExpressionStart=SwitchExpressionStart;
        if(SwitchExpressionStart!=null) SwitchExpressionStart.setParent(this);
        this.SwitchStatementExpression=SwitchStatementExpression;
        if(SwitchStatementExpression!=null) SwitchStatementExpression.setParent(this);
        this.SwitchCases=SwitchCases;
        if(SwitchCases!=null) SwitchCases.setParent(this);
        this.SwitchDefaultCase=SwitchDefaultCase;
        if(SwitchDefaultCase!=null) SwitchDefaultCase.setParent(this);
    }

    public SwitchExpressionStart getSwitchExpressionStart() {
        return SwitchExpressionStart;
    }

    public void setSwitchExpressionStart(SwitchExpressionStart SwitchExpressionStart) {
        this.SwitchExpressionStart=SwitchExpressionStart;
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

    public SwitchDefaultCase getSwitchDefaultCase() {
        return SwitchDefaultCase;
    }

    public void setSwitchDefaultCase(SwitchDefaultCase SwitchDefaultCase) {
        this.SwitchDefaultCase=SwitchDefaultCase;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(SwitchExpressionStart!=null) SwitchExpressionStart.accept(visitor);
        if(SwitchStatementExpression!=null) SwitchStatementExpression.accept(visitor);
        if(SwitchCases!=null) SwitchCases.accept(visitor);
        if(SwitchDefaultCase!=null) SwitchDefaultCase.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(SwitchExpressionStart!=null) SwitchExpressionStart.traverseTopDown(visitor);
        if(SwitchStatementExpression!=null) SwitchStatementExpression.traverseTopDown(visitor);
        if(SwitchCases!=null) SwitchCases.traverseTopDown(visitor);
        if(SwitchDefaultCase!=null) SwitchDefaultCase.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(SwitchExpressionStart!=null) SwitchExpressionStart.traverseBottomUp(visitor);
        if(SwitchStatementExpression!=null) SwitchStatementExpression.traverseBottomUp(visitor);
        if(SwitchCases!=null) SwitchCases.traverseBottomUp(visitor);
        if(SwitchDefaultCase!=null) SwitchDefaultCase.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("SwitchExpression(\n");

        if(SwitchExpressionStart!=null)
            buffer.append(SwitchExpressionStart.toString("  "+tab));
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

        if(SwitchDefaultCase!=null)
            buffer.append(SwitchDefaultCase.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [SwitchExpression]");
        return buffer.toString();
    }
}
