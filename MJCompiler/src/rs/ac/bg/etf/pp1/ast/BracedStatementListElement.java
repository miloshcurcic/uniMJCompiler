// generated with ast extension for cup
// version 0.8
// 9/0/2021 0:50:22


package rs.ac.bg.etf.pp1.ast;

public class BracedStatementListElement extends BracedStatements {

    private BracedStatements BracedStatements;
    private SingleStatement SingleStatement;

    public BracedStatementListElement (BracedStatements BracedStatements, SingleStatement SingleStatement) {
        this.BracedStatements=BracedStatements;
        if(BracedStatements!=null) BracedStatements.setParent(this);
        this.SingleStatement=SingleStatement;
        if(SingleStatement!=null) SingleStatement.setParent(this);
    }

    public BracedStatements getBracedStatements() {
        return BracedStatements;
    }

    public void setBracedStatements(BracedStatements BracedStatements) {
        this.BracedStatements=BracedStatements;
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
        if(BracedStatements!=null) BracedStatements.accept(visitor);
        if(SingleStatement!=null) SingleStatement.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(BracedStatements!=null) BracedStatements.traverseTopDown(visitor);
        if(SingleStatement!=null) SingleStatement.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(BracedStatements!=null) BracedStatements.traverseBottomUp(visitor);
        if(SingleStatement!=null) SingleStatement.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("BracedStatementListElement(\n");

        if(BracedStatements!=null)
            buffer.append(BracedStatements.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(SingleStatement!=null)
            buffer.append(SingleStatement.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [BracedStatementListElement]");
        return buffer.toString();
    }
}
