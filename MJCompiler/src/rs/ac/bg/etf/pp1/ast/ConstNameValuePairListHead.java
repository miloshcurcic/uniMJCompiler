// generated with ast extension for cup
// version 0.8
// 11/0/2021 2:21:35


package rs.ac.bg.etf.pp1.ast;

public class ConstNameValuePairListHead extends ConstNameValuePairs {

    private ConstNameValuePair ConstNameValuePair;

    public ConstNameValuePairListHead (ConstNameValuePair ConstNameValuePair) {
        this.ConstNameValuePair=ConstNameValuePair;
        if(ConstNameValuePair!=null) ConstNameValuePair.setParent(this);
    }

    public ConstNameValuePair getConstNameValuePair() {
        return ConstNameValuePair;
    }

    public void setConstNameValuePair(ConstNameValuePair ConstNameValuePair) {
        this.ConstNameValuePair=ConstNameValuePair;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ConstNameValuePair!=null) ConstNameValuePair.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ConstNameValuePair!=null) ConstNameValuePair.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ConstNameValuePair!=null) ConstNameValuePair.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ConstNameValuePairListHead(\n");

        if(ConstNameValuePair!=null)
            buffer.append(ConstNameValuePair.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ConstNameValuePairListHead]");
        return buffer.toString();
    }
}
