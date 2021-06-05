// generated with ast extension for cup
// version 0.8
// 6/5/2021 0:30:53


package rs.ac.bg.etf.pp1.ast;

public class MatchedPrintStatement extends MatchedStatement {

    private Expr Expr;
    private PrintStatementAdditionalParam PrintStatementAdditionalParam;

    public MatchedPrintStatement (Expr Expr, PrintStatementAdditionalParam PrintStatementAdditionalParam) {
        this.Expr=Expr;
        if(Expr!=null) Expr.setParent(this);
        this.PrintStatementAdditionalParam=PrintStatementAdditionalParam;
        if(PrintStatementAdditionalParam!=null) PrintStatementAdditionalParam.setParent(this);
    }

    public Expr getExpr() {
        return Expr;
    }

    public void setExpr(Expr Expr) {
        this.Expr=Expr;
    }

    public PrintStatementAdditionalParam getPrintStatementAdditionalParam() {
        return PrintStatementAdditionalParam;
    }

    public void setPrintStatementAdditionalParam(PrintStatementAdditionalParam PrintStatementAdditionalParam) {
        this.PrintStatementAdditionalParam=PrintStatementAdditionalParam;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Expr!=null) Expr.accept(visitor);
        if(PrintStatementAdditionalParam!=null) PrintStatementAdditionalParam.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Expr!=null) Expr.traverseTopDown(visitor);
        if(PrintStatementAdditionalParam!=null) PrintStatementAdditionalParam.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Expr!=null) Expr.traverseBottomUp(visitor);
        if(PrintStatementAdditionalParam!=null) PrintStatementAdditionalParam.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("MatchedPrintStatement(\n");

        if(Expr!=null)
            buffer.append(Expr.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(PrintStatementAdditionalParam!=null)
            buffer.append(PrintStatementAdditionalParam.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [MatchedPrintStatement]");
        return buffer.toString();
    }
}
