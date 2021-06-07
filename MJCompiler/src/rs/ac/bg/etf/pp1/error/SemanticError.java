package rs.ac.bg.etf.pp1.error;

import rs.ac.bg.etf.pp1.test.CompilerError;

public class SemanticError extends CompilerError {
    public SemanticError(int line, String message) {
        super(line, message, CompilerErrorType.SEMANTIC_ERROR);
    }
}
