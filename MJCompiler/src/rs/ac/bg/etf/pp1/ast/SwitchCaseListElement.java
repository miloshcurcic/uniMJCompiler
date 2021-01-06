// generated with ast extension for cup
// version 0.8
// 6/0/2021 3:41:52


package rs.ac.bg.etf.pp1.ast;

public class SwitchCaseListElement extends SwitchCases {

    private SwitchCases SwitchCases;
    private SwitchCase SwitchCase;

    public SwitchCaseListElement (SwitchCases SwitchCases, SwitchCase SwitchCase) {
        this.SwitchCases=SwitchCases;
        if(SwitchCases!=null) SwitchCases.setParent(this);
        this.SwitchCase=SwitchCase;
        if(SwitchCase!=null) SwitchCase.setParent(this);
    }

    public SwitchCases getSwitchCases() {
        return SwitchCases;
    }

    public void setSwitchCases(SwitchCases SwitchCases) {
        this.SwitchCases=SwitchCases;
    }

    public SwitchCase getSwitchCase() {
        return SwitchCase;
    }

    public void setSwitchCase(SwitchCase SwitchCase) {
        this.SwitchCase=SwitchCase;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(SwitchCases!=null) SwitchCases.accept(visitor);
        if(SwitchCase!=null) SwitchCase.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(SwitchCases!=null) SwitchCases.traverseTopDown(visitor);
        if(SwitchCase!=null) SwitchCase.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(SwitchCases!=null) SwitchCases.traverseBottomUp(visitor);
        if(SwitchCase!=null) SwitchCase.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("SwitchCaseListElement(\n");

        if(SwitchCases!=null)
            buffer.append(SwitchCases.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(SwitchCase!=null)
            buffer.append(SwitchCase.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [SwitchCaseListElement]");
        return buffer.toString();
    }
}
