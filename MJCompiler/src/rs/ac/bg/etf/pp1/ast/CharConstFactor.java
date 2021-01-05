// generated with ast extension for cup
// version 0.8
// 5/0/2021 3:41:38


package rs.ac.bg.etf.pp1.ast;

public class CharConstFactor extends Factor {

    private Char C1;

    public CharConstFactor (Char C1) {
        this.C1=C1;
        if(C1!=null) C1.setParent(this);
    }

    public Char getC1() {
        return C1;
    }

    public void setC1(Char C1) {
        this.C1=C1;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(C1!=null) C1.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(C1!=null) C1.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(C1!=null) C1.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("CharConstFactor(\n");

        if(C1!=null)
            buffer.append(C1.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [CharConstFactor]");
        return buffer.toString();
    }
}
