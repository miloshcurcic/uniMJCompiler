// generated with ast extension for cup
// version 0.8
// 29/5/2021 22:10:5


package rs.ac.bg.etf.pp1.ast;

public class ExtendedClassDeclaration extends ClassDecl {

    private ClassName ClassName;
    private BaseClassType BaseClassType;
    private ClassVarDecls ClassVarDecls;
    private ExtendedClassSplitter ExtendedClassSplitter;
    private ClassMethodDecls ClassMethodDecls;

    public ExtendedClassDeclaration (ClassName ClassName, BaseClassType BaseClassType, ClassVarDecls ClassVarDecls, ExtendedClassSplitter ExtendedClassSplitter, ClassMethodDecls ClassMethodDecls) {
        this.ClassName=ClassName;
        if(ClassName!=null) ClassName.setParent(this);
        this.BaseClassType=BaseClassType;
        if(BaseClassType!=null) BaseClassType.setParent(this);
        this.ClassVarDecls=ClassVarDecls;
        if(ClassVarDecls!=null) ClassVarDecls.setParent(this);
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

    public BaseClassType getBaseClassType() {
        return BaseClassType;
    }

    public void setBaseClassType(BaseClassType BaseClassType) {
        this.BaseClassType=BaseClassType;
    }

    public ClassVarDecls getClassVarDecls() {
        return ClassVarDecls;
    }

    public void setClassVarDecls(ClassVarDecls ClassVarDecls) {
        this.ClassVarDecls=ClassVarDecls;
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
        if(BaseClassType!=null) BaseClassType.accept(visitor);
        if(ClassVarDecls!=null) ClassVarDecls.accept(visitor);
        if(ExtendedClassSplitter!=null) ExtendedClassSplitter.accept(visitor);
        if(ClassMethodDecls!=null) ClassMethodDecls.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ClassName!=null) ClassName.traverseTopDown(visitor);
        if(BaseClassType!=null) BaseClassType.traverseTopDown(visitor);
        if(ClassVarDecls!=null) ClassVarDecls.traverseTopDown(visitor);
        if(ExtendedClassSplitter!=null) ExtendedClassSplitter.traverseTopDown(visitor);
        if(ClassMethodDecls!=null) ClassMethodDecls.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ClassName!=null) ClassName.traverseBottomUp(visitor);
        if(BaseClassType!=null) BaseClassType.traverseBottomUp(visitor);
        if(ClassVarDecls!=null) ClassVarDecls.traverseBottomUp(visitor);
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

        if(BaseClassType!=null)
            buffer.append(BaseClassType.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ClassVarDecls!=null)
            buffer.append(ClassVarDecls.toString("  "+tab));
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
