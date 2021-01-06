// generated with ast extension for cup
// version 0.8
// 6/0/2021 3:41:52


package rs.ac.bg.etf.pp1.ast;

public class Expr1 implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    private ExprPrefix ExprPrefix;
    private TermExpr TermExpr;

    public Expr1 (ExprPrefix ExprPrefix, TermExpr TermExpr) {
        this.ExprPrefix=ExprPrefix;
        if(ExprPrefix!=null) ExprPrefix.setParent(this);
        this.TermExpr=TermExpr;
        if(TermExpr!=null) TermExpr.setParent(this);
    }

    public ExprPrefix getExprPrefix() {
        return ExprPrefix;
    }

    public void setExprPrefix(ExprPrefix ExprPrefix) {
        this.ExprPrefix=ExprPrefix;
    }

    public TermExpr getTermExpr() {
        return TermExpr;
    }

    public void setTermExpr(TermExpr TermExpr) {
        this.TermExpr=TermExpr;
    }

    public SyntaxNode getParent() {
        return parent;
    }

    public void setParent(SyntaxNode parent) {
        this.parent=parent;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line=line;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ExprPrefix!=null) ExprPrefix.accept(visitor);
        if(TermExpr!=null) TermExpr.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ExprPrefix!=null) ExprPrefix.traverseTopDown(visitor);
        if(TermExpr!=null) TermExpr.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ExprPrefix!=null) ExprPrefix.traverseBottomUp(visitor);
        if(TermExpr!=null) TermExpr.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("Expr1(\n");

        if(ExprPrefix!=null)
            buffer.append(ExprPrefix.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(TermExpr!=null)
            buffer.append(TermExpr.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [Expr1]");
        return buffer.toString();
    }
}
