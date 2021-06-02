// generated with ast extension for cup
// version 0.8
// 3/5/2021 1:2:58


package rs.ac.bg.etf.pp1.ast;

public class FunctionCall implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    public rs.etf.pp1.symboltable.concepts.Struct struct = null;

    private FunctionCallDesignator FunctionCallDesignator;
    private FuncCallActPars FuncCallActPars;

    public FunctionCall (FunctionCallDesignator FunctionCallDesignator, FuncCallActPars FuncCallActPars) {
        this.FunctionCallDesignator=FunctionCallDesignator;
        if(FunctionCallDesignator!=null) FunctionCallDesignator.setParent(this);
        this.FuncCallActPars=FuncCallActPars;
        if(FuncCallActPars!=null) FuncCallActPars.setParent(this);
    }

    public FunctionCallDesignator getFunctionCallDesignator() {
        return FunctionCallDesignator;
    }

    public void setFunctionCallDesignator(FunctionCallDesignator FunctionCallDesignator) {
        this.FunctionCallDesignator=FunctionCallDesignator;
    }

    public FuncCallActPars getFuncCallActPars() {
        return FuncCallActPars;
    }

    public void setFuncCallActPars(FuncCallActPars FuncCallActPars) {
        this.FuncCallActPars=FuncCallActPars;
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
        if(FunctionCallDesignator!=null) FunctionCallDesignator.accept(visitor);
        if(FuncCallActPars!=null) FuncCallActPars.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(FunctionCallDesignator!=null) FunctionCallDesignator.traverseTopDown(visitor);
        if(FuncCallActPars!=null) FuncCallActPars.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(FunctionCallDesignator!=null) FunctionCallDesignator.traverseBottomUp(visitor);
        if(FuncCallActPars!=null) FuncCallActPars.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("FunctionCall(\n");

        if(FunctionCallDesignator!=null)
            buffer.append(FunctionCallDesignator.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(FuncCallActPars!=null)
            buffer.append(FuncCallActPars.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [FunctionCall]");
        return buffer.toString();
    }
}
