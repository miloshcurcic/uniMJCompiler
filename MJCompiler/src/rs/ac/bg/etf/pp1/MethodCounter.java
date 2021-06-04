package rs.ac.bg.etf.pp1;

import rs.ac.bg.etf.pp1.ast.*;

public class MethodCounter extends VisitorAdaptor {
    private int varCount;
    private int formParCount;

    @Override
    public void visit(ScalarVarIdent varDecl) {
        System.err.println(varDecl.getName());
        varCount++;
    }

    @Override
    public void visit(ArrayVarIdent varDecl) {
        System.err.println(varDecl.getName());
        varCount++;
    }

    @Override
    public void visit(ScalarFormalParameter formPar) {
        System.err.println(formPar.getName());
        formParCount++;
    }

    @Override
    public void visit(ArrayFormalParameter formPar) {
        System.err.println(formPar.getName());
        formParCount++;
    }

    public int getVarCount() {
        return varCount;
    }

    public int getFormParCount() {
        return formParCount;
    }
}
