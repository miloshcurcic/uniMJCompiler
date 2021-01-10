// generated with ast extension for cup
// version 0.8
// 10/0/2021 3:13:34


package rs.ac.bg.etf.pp1.ast;

public class MatchedSwitchStatement extends MatchedStatement {

    private Expr Expr;
    private SwitchCases SwitchCases;

    public MatchedSwitchStatement (Expr Expr, SwitchCases SwitchCases) {
        this.Expr=Expr;
        if(Expr!=null) Expr.setParent(this);
        this.SwitchCases=SwitchCases;
        if(SwitchCases!=null) SwitchCases.setParent(this);
    }

    public Expr getExpr() {
        return Expr;
    }

    public void setExpr(Expr Expr) {
        this.Expr=Expr;
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
        if(Expr!=null) Expr.accept(visitor);
        if(SwitchCases!=null) SwitchCases.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Expr!=null) Expr.traverseTopDown(visitor);
        if(SwitchCases!=null) SwitchCases.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Expr!=null) Expr.traverseBottomUp(visitor);
        if(SwitchCases!=null) SwitchCases.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("MatchedSwitchStatement(\n");

        if(Expr!=null)
            buffer.append(Expr.toString("  "+tab));
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
