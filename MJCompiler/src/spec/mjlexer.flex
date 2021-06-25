package rs.ac.bg.etf.pp1;

import java.util.*;
import org.apache.log4j.*;
import rs.ac.bg.etf.pp1.test.*;
import rs.ac.bg.etf.pp1.error.*;
import java_cup.runtime.Symbol;

import static rs.ac.bg.etf.pp1.error.LexicalError.LEXICAL_UNRECOGNIZED_TEXT_ERROR_TEMPLATE;

%%

%class Lexer
%implements rs.ac.bg.etf.pp1.error.Errorable

%cup
%line
%column
%unicode

%{
	private Logger log = Logger.getLogger(getClass());
        private StringBuilder errorMessageBuilder = new StringBuilder();
        private List<CompilerError> errorList = new ArrayList<>();
    
        private int errorLine = 0;
        private int errorColumn = 0;
        private int errorPreviousColumn = 0;
    
        public List<CompilerError> getErrorList() {
            return errorList;
        }

        public boolean isErrorDetected() {
            return !errorList.isEmpty();
        }
    
        public void flushErrorIfExists() {
            if (errorMessageBuilder.length() != 0) {
                reportError(LEXICAL_UNRECOGNIZED_TEXT_ERROR_TEMPLATE, errorMessageBuilder.toString(), errorLine + 1, errorColumn + 1);

                errorMessageBuilder = new StringBuilder();
            }
        }
    
        public void updateOrInitializeErrorContext(String text, int line, int column) {
            if (errorPreviousColumn != (column - 1)) {
                flushErrorIfExists();
            }
    
            if (errorMessageBuilder.length() == 0) {
                errorLine = line;
                errorColumn = column;
            }
    
            errorPreviousColumn = column;
            errorMessageBuilder.append(text);
        }
    
        public void reportError(String template, Object... args) {
            String errorMessage = String.format(template, (Object[])args);

            errorList.add(new LexicalError(errorLine + 1, errorMessage));
            log.error(errorMessage);
        }
    
        private Symbol newSymbol(int type) {
            return new Symbol(type, yyline+1, yycolumn);
        }
    
        private Symbol newSymbol(int type, Object value) {
            return new Symbol(type, yyline+1, yycolumn, value);
        }
%}

%xstate COMMENT

%eofval{
    flushErrorIfExists();

	return newSymbol(sym.EOF);
%eofval}

%%

// White spaces
" " 	{ }
"\b" 	{ }
"\t" 	{ }
"\r\n" 	{ flushErrorIfExists(); }
"\f" 	{ }

// Key words
"program"   { return newSymbol(sym.PROGRAM); }
"break" 	{ return newSymbol(sym.BREAK); }
"class" 	{ return newSymbol(sym.CLASS); }
"const" 	{ return newSymbol(sym.CONST); }
"if" 	{ return newSymbol(sym.IF); }
"else" 	{ return newSymbol(sym.ELSE); }
"switch" 	{ return newSymbol(sym.SWITCH); }
"yield" 	{ return newSymbol(sym.YIELD); }
"default" 	{ return newSymbol(sym.DEFAULT); }
"do" 	{ return newSymbol(sym.DO); }
"while" 	{ return newSymbol(sym.WHILE); }
"new" 	{ return newSymbol(sym.NEW); }
"print" 	{ return newSymbol(sym.PRINT); }
"read" 	{ return newSymbol(sym.READ); }
"return" 	{ return newSymbol(sym.RETURN); }
"void" 	{ return newSymbol(sym.VOID); }
"extends" 	{ return newSymbol(sym.EXTENDS); }
"continue" 	{ return newSymbol(sym.CONTINUE); }
"case" 	{ return newSymbol(sym.CASE); }


// Operators - Arithmetic
"+" 		{ return newSymbol(sym.PLUS); }
"-" 		{ return newSymbol(sym.MINUS); }
"*" 		{ return newSymbol(sym.MULTIPLY); }
"/" 		{ return newSymbol(sym.DIVIDE); }
"%" 		{ return newSymbol(sym.MODULO); }
"=" 		{ return newSymbol(sym.EQUALS); }
"++" 		{ return newSymbol(sym.PPLUS); }
"--" 		{ return newSymbol(sym.MMINUS); }

// Operators - Comparison
"==" 		{ return newSymbol(sym.CEQUALS); }
"!=" 		{ return newSymbol(sym.CNEQUALS); }
">" 		{ return newSymbol(sym.GREATER); }
">=" 		{ return newSymbol(sym.GEQUALS); }
"<" 		{ return newSymbol(sym.LESS); }
"<=" 		{ return newSymbol(sym.LEQUALS); }

// Operators - Logical
"&&" 		{ return newSymbol(sym.AND); }
"||" 		{ return newSymbol(sym.OR); }

// Operators - Control
";" 		{ return newSymbol(sym.SEMICOLON); }
":" 		{ return newSymbol(sym.COLON); }
"," 		{ return newSymbol(sym.COMMA); }
"." 		{ return newSymbol(sym.DOT); }
"(" 		{ return newSymbol(sym.LPAREN); }
")" 		{ return newSymbol(sym.RPAREN); }
"{" 		{ return newSymbol(sym.LBRACE); }
"}"			{ return newSymbol(sym.RBRACE); }
"[" 		{ return newSymbol(sym.LBRACKET); }
"]"			{ return newSymbol(sym.RBRACKET); }

// Comments
"//" 		     { yybegin(COMMENT); }
<COMMENT> .      { yybegin(COMMENT); }
<COMMENT> "\r\n" { yybegin(YYINITIAL); }

// Symbols and literals
'.' { return newSymbol(sym.CHAR, yytext().charAt(1)); }
("true" | "false") { return newSymbol(sym.BOOL, Boolean.parseBoolean(yytext())); }
[0-9]+  { return newSymbol(sym.NUMBER, new Integer (yytext())); }
([a-z]|[A-Z])[a-z|A-Z|0-9|_]* 	{return newSymbol (sym.IDENT, yytext()); }

// Consume error
. { updateOrInitializeErrorContext(yytext(), yyline, yycolumn); }
