// generated with ast extension for cup
// version 0.8
// 11/0/2021 2:21:35


package rs.ac.bg.etf.pp1.ast;

public class NoClassMethodDeclarations extends ClassMethodDecls {

    public NoClassMethodDeclarations () {
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("NoClassMethodDeclarations(\n");

        buffer.append(tab);
        buffer.append(") [NoClassMethodDeclarations]");
        return buffer.toString();
    }
}
