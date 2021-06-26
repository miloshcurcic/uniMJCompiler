// generated with ast extension for cup
// version 0.8
// 26/5/2021 3:14:45


package rs.ac.bg.etf.pp1.ast;

public class ConstNameValuePairListElement extends ConstNameValuePairs {

    private ConstNameValuePairs ConstNameValuePairs;
    private ConstNameValuePair ConstNameValuePair;

    public ConstNameValuePairListElement (ConstNameValuePairs ConstNameValuePairs, ConstNameValuePair ConstNameValuePair) {
        this.ConstNameValuePairs=ConstNameValuePairs;
        if(ConstNameValuePairs!=null) ConstNameValuePairs.setParent(this);
        this.ConstNameValuePair=ConstNameValuePair;
        if(ConstNameValuePair!=null) ConstNameValuePair.setParent(this);
    }

    public ConstNameValuePairs getConstNameValuePairs() {
        return ConstNameValuePairs;
    }

    public void setConstNameValuePairs(ConstNameValuePairs ConstNameValuePairs) {
        this.ConstNameValuePairs=ConstNameValuePairs;
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
        if(ConstNameValuePairs!=null) ConstNameValuePairs.accept(visitor);
        if(ConstNameValuePair!=null) ConstNameValuePair.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ConstNameValuePairs!=null) ConstNameValuePairs.traverseTopDown(visitor);
        if(ConstNameValuePair!=null) ConstNameValuePair.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ConstNameValuePairs!=null) ConstNameValuePairs.traverseBottomUp(visitor);
        if(ConstNameValuePair!=null) ConstNameValuePair.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ConstNameValuePairListElement(\n");

        if(ConstNameValuePairs!=null)
            buffer.append(ConstNameValuePairs.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ConstNameValuePair!=null)
            buffer.append(ConstNameValuePair.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ConstNameValuePairListElement]");
        return buffer.toString();
    }
}
