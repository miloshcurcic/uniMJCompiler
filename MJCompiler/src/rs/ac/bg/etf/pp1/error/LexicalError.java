package rs.ac.bg.etf.pp1.error;

import rs.ac.bg.etf.pp1.test.CompilerError;

public class LexicalError extends CompilerError {
    public static final String LEXICAL_UNRECOGNIZED_TEXT_ERROR_TEMPLATE = "Line %d Column %d: Lexical analysis error for text \"%s\"!";

    public LexicalError(int line, String message) {
        super(line, message, CompilerErrorType.LEXICAL_ERROR);
    }
}
