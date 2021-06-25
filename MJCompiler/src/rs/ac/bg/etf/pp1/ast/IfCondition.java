// generated with ast extension for cup
// version 0.8
// 25/5/2021 21:0:8


package rs.ac.bg.etf.pp1.ast;

public class IfCondition extends IfCond {

    private IfConditionStart IfConditionStart;
    private Condition Condition;
    private IfConditionEnd IfConditionEnd;

    public IfCondition (IfConditionStart IfConditionStart, Condition Condition, IfConditionEnd IfConditionEnd) {
        this.IfConditionStart=IfConditionStart;
        if(IfConditionStart!=null) IfConditionStart.setParent(this);
        this.Condition=Condition;
        if(Condition!=null) Condition.setParent(this);
        this.IfConditionEnd=IfConditionEnd;
        if(IfConditionEnd!=null) IfConditionEnd.setParent(this);
    }

    public IfConditionStart getIfConditionStart() {
        return IfConditionStart;
    }

    public void setIfConditionStart(IfConditionStart IfConditionStart) {
        this.IfConditionStart=IfConditionStart;
    }

    public Condition getCondition() {
        return Condition;
    }

    public void setCondition(Condition Condition) {
        this.Condition=Condition;
    }

    public IfConditionEnd getIfConditionEnd() {
        return IfConditionEnd;
    }

    public void setIfConditionEnd(IfConditionEnd IfConditionEnd) {
        this.IfConditionEnd=IfConditionEnd;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(IfConditionStart!=null) IfConditionStart.accept(visitor);
        if(Condition!=null) Condition.accept(visitor);
        if(IfConditionEnd!=null) IfConditionEnd.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(IfConditionStart!=null) IfConditionStart.traverseTopDown(visitor);
        if(Condition!=null) Condition.traverseTopDown(visitor);
        if(IfConditionEnd!=null) IfConditionEnd.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(IfConditionStart!=null) IfConditionStart.traverseBottomUp(visitor);
        if(Condition!=null) Condition.traverseBottomUp(visitor);
        if(IfConditionEnd!=null) IfConditionEnd.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("IfCondition(\n");

        if(IfConditionStart!=null)
            buffer.append(IfConditionStart.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Condition!=null)
            buffer.append(Condition.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(IfConditionEnd!=null)
            buffer.append(IfConditionEnd.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [IfCondition]");
        return buffer.toString();
    }
}
