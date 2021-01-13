// generated with ast extension for cup
// version 0.8
// 14/0/2021 0:51:48


package rs.ac.bg.etf.pp1.ast;

public class SwitchCase implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    private Integer number;
    private SwitchCaseStatementStart SwitchCaseStatementStart;
    private Statements Statements;
    private SwitchCaseStatementEnd SwitchCaseStatementEnd;

    public SwitchCase (Integer number, SwitchCaseStatementStart SwitchCaseStatementStart, Statements Statements, SwitchCaseStatementEnd SwitchCaseStatementEnd) {
        this.number=number;
        this.SwitchCaseStatementStart=SwitchCaseStatementStart;
        if(SwitchCaseStatementStart!=null) SwitchCaseStatementStart.setParent(this);
        this.Statements=Statements;
        if(Statements!=null) Statements.setParent(this);
        this.SwitchCaseStatementEnd=SwitchCaseStatementEnd;
        if(SwitchCaseStatementEnd!=null) SwitchCaseStatementEnd.setParent(this);
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number=number;
    }

    public SwitchCaseStatementStart getSwitchCaseStatementStart() {
        return SwitchCaseStatementStart;
    }

    public void setSwitchCaseStatementStart(SwitchCaseStatementStart SwitchCaseStatementStart) {
        this.SwitchCaseStatementStart=SwitchCaseStatementStart;
    }

    public Statements getStatements() {
        return Statements;
    }

    public void setStatements(Statements Statements) {
        this.Statements=Statements;
    }

    public SwitchCaseStatementEnd getSwitchCaseStatementEnd() {
        return SwitchCaseStatementEnd;
    }

    public void setSwitchCaseStatementEnd(SwitchCaseStatementEnd SwitchCaseStatementEnd) {
        this.SwitchCaseStatementEnd=SwitchCaseStatementEnd;
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
        if(SwitchCaseStatementStart!=null) SwitchCaseStatementStart.accept(visitor);
        if(Statements!=null) Statements.accept(visitor);
        if(SwitchCaseStatementEnd!=null) SwitchCaseStatementEnd.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(SwitchCaseStatementStart!=null) SwitchCaseStatementStart.traverseTopDown(visitor);
        if(Statements!=null) Statements.traverseTopDown(visitor);
        if(SwitchCaseStatementEnd!=null) SwitchCaseStatementEnd.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(SwitchCaseStatementStart!=null) SwitchCaseStatementStart.traverseBottomUp(visitor);
        if(Statements!=null) Statements.traverseBottomUp(visitor);
        if(SwitchCaseStatementEnd!=null) SwitchCaseStatementEnd.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("SwitchCase(\n");

        buffer.append(" "+tab+number);
        buffer.append("\n");

        if(SwitchCaseStatementStart!=null)
            buffer.append(SwitchCaseStatementStart.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Statements!=null)
            buffer.append(Statements.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(SwitchCaseStatementEnd!=null)
            buffer.append(SwitchCaseStatementEnd.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [SwitchCase]");
        return buffer.toString();
    }
}
