// generated with ast extension for cup
// version 0.8
// 29/5/2021 3:48:12


package rs.ac.bg.etf.pp1.ast;

public class AssignmentDesignatorStatement extends DesignatorStatement {

    private AssignStmt AssignStmt;

    public AssignmentDesignatorStatement (AssignStmt AssignStmt) {
        this.AssignStmt=AssignStmt;
        if(AssignStmt!=null) AssignStmt.setParent(this);
    }

    public AssignStmt getAssignStmt() {
        return AssignStmt;
    }

    public void setAssignStmt(AssignStmt AssignStmt) {
        this.AssignStmt=AssignStmt;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(AssignStmt!=null) AssignStmt.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(AssignStmt!=null) AssignStmt.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(AssignStmt!=null) AssignStmt.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("AssignmentDesignatorStatement(\n");

        if(AssignStmt!=null)
            buffer.append(AssignStmt.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [AssignmentDesignatorStatement]");
        return buffer.toString();
    }
}
