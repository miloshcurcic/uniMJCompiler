// generated with ast extension for cup
// version 0.8
// 5/0/2021 3:41:37


package rs.ac.bg.etf.pp1.ast;

public class CharConstNameValuePair extends ConstNameValuePair {

    private String I1;
    private Char C2;

    public CharConstNameValuePair (String I1, Char C2) {
        this.I1=I1;
        this.C2=C2;
        if(C2!=null) C2.setParent(this);
    }

    public String getI1() {
        return I1;
    }

    public void setI1(String I1) {
        this.I1=I1;
    }

    public Char getC2() {
        return C2;
    }

    public void setC2(Char C2) {
        this.C2=C2;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(C2!=null) C2.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(C2!=null) C2.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(C2!=null) C2.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("CharConstNameValuePair(\n");

        buffer.append(" "+tab+I1);
        buffer.append("\n");

        if(C2!=null)
            buffer.append(C2.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [CharConstNameValuePair]");
        return buffer.toString();
    }
}
