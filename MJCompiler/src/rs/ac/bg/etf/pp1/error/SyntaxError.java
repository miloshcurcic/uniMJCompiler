package rs.ac.bg.etf.pp1.error;

import rs.ac.bg.etf.pp1.test.CompilerError;

public class SyntaxError extends CompilerError {
    public SyntaxError(int line, String message, CompilerErrorType type) {
        super(line, message, type);
    }
}
