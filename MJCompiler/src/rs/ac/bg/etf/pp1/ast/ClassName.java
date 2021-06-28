// generated with ast extension for cup
// version 0.8
// 28/5/2021 2:1:52


package rs.ac.bg.etf.pp1.ast;

public class ClassName implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    private ClassType ClassType;

    public ClassName (ClassType ClassType) {
        this.ClassType=ClassType;
        if(ClassType!=null) ClassType.setParent(this);
    }

    public ClassType getClassType() {
        return ClassType;
    }

    public void setClassType(ClassType ClassType) {
        this.ClassType=ClassType;
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
        if(ClassType!=null) ClassType.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ClassType!=null) ClassType.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ClassType!=null) ClassType.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ClassName(\n");

        if(ClassType!=null)
            buffer.append(ClassType.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ClassName]");
        return buffer.toString();
    }
}
