package rs.ac.bg.etf.pp1.error;

import rs.ac.bg.etf.pp1.test.CompilerError;

import java.util.List;

public interface Errorable {
    List<CompilerError> getErrorList();
    boolean isErrorDetected();
}
