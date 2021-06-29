// generated with ast extension for cup
// version 0.8
// 29/5/2021 3:22:56


package rs.ac.bg.etf.pp1.ast;

public class ClassDeclaration extends ClassDecl {

    private ClassName ClassName;
    private ClassVarDecls ClassVarDecls;
    private ClassMethodDecls ClassMethodDecls;

    public ClassDeclaration (ClassName ClassName, ClassVarDecls ClassVarDecls, ClassMethodDecls ClassMethodDecls) {
        this.ClassName=ClassName;
        if(ClassName!=null) ClassName.setParent(this);
        this.ClassVarDecls=ClassVarDecls;
        if(ClassVarDecls!=null) ClassVarDecls.setParent(this);
        this.ClassMethodDecls=ClassMethodDecls;
        if(ClassMethodDecls!=null) ClassMethodDecls.setParent(this);
    }

    public ClassName getClassName() {
        return ClassName;
    }

    public void setClassName(ClassName ClassName) {
        this.ClassName=ClassName;
    }

    public ClassVarDecls getClassVarDecls() {
        return ClassVarDecls;
    }

    public void setClassVarDecls(ClassVarDecls ClassVarDecls) {
        this.ClassVarDecls=ClassVarDecls;
    }

    public ClassMethodDecls getClassMethodDecls() {
        return ClassMethodDecls;
    }

    public void setClassMethodDecls(ClassMethodDecls ClassMethodDecls) {
        this.ClassMethodDecls=ClassMethodDecls;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ClassName!=null) ClassName.accept(visitor);
        if(ClassVarDecls!=null) ClassVarDecls.accept(visitor);
        if(ClassMethodDecls!=null) ClassMethodDecls.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ClassName!=null) ClassName.traverseTopDown(visitor);
        if(ClassVarDecls!=null) ClassVarDecls.traverseTopDown(visitor);
        if(ClassMethodDecls!=null) ClassMethodDecls.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ClassName!=null) ClassName.traverseBottomUp(visitor);
        if(ClassVarDecls!=null) ClassVarDecls.traverseBottomUp(visitor);
        if(ClassMethodDecls!=null) ClassMethodDecls.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ClassDeclaration(\n");

        if(ClassName!=null)
            buffer.append(ClassName.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ClassVarDecls!=null)
            buffer.append(ClassVarDecls.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ClassMethodDecls!=null)
            buffer.append(ClassMethodDecls.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ClassDeclaration]");
        return buffer.toString();
    }
}
