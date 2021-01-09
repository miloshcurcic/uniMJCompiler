// generated with ast extension for cup
// version 0.8
// 9/0/2021 0:50:22


package rs.ac.bg.etf.pp1.ast;

public class FunctionCallResultFactor extends Factor {

    private Designator Designator;
    private FuncCallActPars FuncCallActPars;

    public FunctionCallResultFactor (Designator Designator, FuncCallActPars FuncCallActPars) {
        this.Designator=Designator;
        if(Designator!=null) Designator.setParent(this);
        this.FuncCallActPars=FuncCallActPars;
        if(FuncCallActPars!=null) FuncCallActPars.setParent(this);
    }

    public Designator getDesignator() {
        return Designator;
    }

    public void setDesignator(Designator Designator) {
        this.Designator=Designator;
    }

    public FuncCallActPars getFuncCallActPars() {
        return FuncCallActPars;
    }

    public void setFuncCallActPars(FuncCallActPars FuncCallActPars) {
        this.FuncCallActPars=FuncCallActPars;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Designator!=null) Designator.accept(visitor);
        if(FuncCallActPars!=null) FuncCallActPars.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Designator!=null) Designator.traverseTopDown(visitor);
        if(FuncCallActPars!=null) FuncCallActPars.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Designator!=null) Designator.traverseBottomUp(visitor);
        if(FuncCallActPars!=null) FuncCallActPars.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("FunctionCallResultFactor(\n");

        if(Designator!=null)
            buffer.append(Designator.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(FuncCallActPars!=null)
            buffer.append(FuncCallActPars.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [FunctionCallResultFactor]");
        return buffer.toString();
    }
}
