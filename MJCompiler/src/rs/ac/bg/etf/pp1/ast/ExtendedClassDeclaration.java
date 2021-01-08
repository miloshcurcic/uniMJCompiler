// generated with ast extension for cup
// version 0.8
// 7/0/2021 23:41:32


package rs.ac.bg.etf.pp1.ast;

public class ExtendedClassDeclaration extends ClassDecl {

    private ClassName ClassName;
    private BaseClassName BaseClassName;
    private VarDecls VarDecls;
    private ExtendedClassSplitter ExtendedClassSplitter;
    private ClassMethodDecls ClassMethodDecls;

    public ExtendedClassDeclaration (ClassName ClassName, BaseClassName BaseClassName, VarDecls VarDecls, ExtendedClassSplitter ExtendedClassSplitter, ClassMethodDecls ClassMethodDecls) {
        this.ClassName=ClassName;
        if(ClassName!=null) ClassName.setParent(this);
        this.BaseClassName=BaseClassName;
        if(BaseClassName!=null) BaseClassName.setParent(this);
        this.VarDecls=VarDecls;
        if(VarDecls!=null) VarDecls.setParent(this);
        this.ExtendedClassSplitter=ExtendedClassSplitter;
        if(ExtendedClassSplitter!=null) ExtendedClassSplitter.setParent(this);
        this.ClassMethodDecls=ClassMethodDecls;
        if(ClassMethodDecls!=null) ClassMethodDecls.setParent(this);
    }

    public ClassName getClassName() {
        return ClassName;
    }

    public void setClassName(ClassName ClassName) {
        this.ClassName=ClassName;
    }

    public BaseClassName getBaseClassName() {
        return BaseClassName;
    }

    public void setBaseClassName(BaseClassName BaseClassName) {
        this.BaseClassName=BaseClassName;
    }

    public VarDecls getVarDecls() {
        return VarDecls;
    }

    public void setVarDecls(VarDecls VarDecls) {
        this.VarDecls=VarDecls;
    }

    public ExtendedClassSplitter getExtendedClassSplitter() {
        return ExtendedClassSplitter;
    }

    public void setExtendedClassSplitter(ExtendedClassSplitter ExtendedClassSplitter) {
        this.ExtendedClassSplitter=ExtendedClassSplitter;
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
        if(BaseClassName!=null) BaseClassName.accept(visitor);
        if(VarDecls!=null) VarDecls.accept(visitor);
        if(ExtendedClassSplitter!=null) ExtendedClassSplitter.accept(visitor);
        if(ClassMethodDecls!=null) ClassMethodDecls.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ClassName!=null) ClassName.traverseTopDown(visitor);
        if(BaseClassName!=null) BaseClassName.traverseTopDown(visitor);
        if(VarDecls!=null) VarDecls.traverseTopDown(visitor);
        if(ExtendedClassSplitter!=null) ExtendedClassSplitter.traverseTopDown(visitor);
        if(ClassMethodDecls!=null) ClassMethodDecls.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ClassName!=null) ClassName.traverseBottomUp(visitor);
        if(BaseClassName!=null) BaseClassName.traverseBottomUp(visitor);
        if(VarDecls!=null) VarDecls.traverseBottomUp(visitor);
        if(ExtendedClassSplitter!=null) ExtendedClassSplitter.traverseBottomUp(visitor);
        if(ClassMethodDecls!=null) ClassMethodDecls.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ExtendedClassDeclaration(\n");

        if(ClassName!=null)
            buffer.append(ClassName.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(BaseClassName!=null)
            buffer.append(BaseClassName.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(VarDecls!=null)
            buffer.append(VarDecls.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ExtendedClassSplitter!=null)
            buffer.append(ExtendedClassSplitter.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ClassMethodDecls!=null)
            buffer.append(ClassMethodDecls.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ExtendedClassDeclaration]");
        return buffer.toString();
    }
}
