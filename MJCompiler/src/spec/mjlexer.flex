package rs.ac.bg.etf.pp1;

import java_cup.runtime.Symbol;
import org.apache.log4j.*;

%%

%{
	Logger log = Logger.getLogger(getClass());
    StringBuilder errorMessageBuilder = new StringBuilder();
    int errorLine = 0;
    int errorColumn = 0;
    int errorPreviousColumn = 0;

    public void flushErrorIfExists() {
        if (errorMessageBuilder.length() != 0) {
            report_error("Lexical analysis error for text \"" + errorMessageBuilder.toString() + "\" on line " + (errorLine + 1) + ":" + (errorColumn + 1) + "!");
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

    public void report_error(String message) {
        log.error(message);
    }

	private Symbol new_symbol(int type) {
		return new Symbol(type, yyline+1, yycolumn);
	}

	private Symbol new_symbol(int type, Object value) {
		return new Symbol(type, yyline+1, yycolumn, value);
	}
%}

%cup
%line
%column

%xstate COMMENT

%eofval{
    flushErrorIfExists();
	return new_symbol(sym.EOF);
%eofval}

%%

// White spaces
" " 	{ }
"\b" 	{ }
"\t" 	{ }
"\r\n" 	{ flushErrorIfExists(); }
"\f" 	{ }

// Key words
"program"   { return new_symbol(sym.PROGRAM); }
"break" 	{ return new_symbol(sym.BREAK); }
"class" 	{ return new_symbol(sym.CLASS); }
"const" 	{ return new_symbol(sym.CONST); }
"if" 	{ return new_symbol(sym.IF); }
"else" 	{ return new_symbol(sym.ELSE); }
"switch" 	{ return new_symbol(sym.SWITCH); }
"yield" 	{ return new_symbol(sym.YIELD); }
"default" 	{ return new_symbol(sym.DEFAULT); }
"do" 	{ return new_symbol(sym.DO); }
"while" 	{ return new_symbol(sym.WHILE); }
"new" 	{ return new_symbol(sym.NEW); }
"print" 	{ return new_symbol(sym.PRINT); }
"read" 	{ return new_symbol(sym.READ); }
"return" 	{ return new_symbol(sym.RETURN); }
"void" 	{ return new_symbol(sym.VOID); }
"extends" 	{ return new_symbol(sym.EXTENDS); }
"continue" 	{ return new_symbol(sym.CONTINUE); }
"case" 	{ return new_symbol(sym.CASE); }


// Operators - Arithmetic
"+" 		{ return new_symbol(sym.PLUS); }
"-" 		{ return new_symbol(sym.MINUS); }
"*" 		{ return new_symbol(sym.MULTIPLY); }
"/" 		{ return new_symbol(sym.DIVIDE); }
"%" 		{ return new_symbol(sym.MODULO); }
"=" 		{ return new_symbol(sym.EQUALS); }
"++" 		{ return new_symbol(sym.PPLUS); }
"--" 		{ return new_symbol(sym.MMINUS); }

// Operators - Comparison
"==" 		{ return new_symbol(sym.CEQUALS); }
"!=" 		{ return new_symbol(sym.CNEQUALS); }
">" 		{ return new_symbol(sym.GREATER); }
">=" 		{ return new_symbol(sym.GEQUALS); }
"<" 		{ return new_symbol(sym.LESS); }
"<=" 		{ return new_symbol(sym.LEQUALS); }

// Operators - Logical
"&&" 		{ return new_symbol(sym.AND); }
"||" 		{ return new_symbol(sym.OR); }

// Operators - Control
";" 		{ return new_symbol(sym.SEMICOLON); }
":" 		{ return new_symbol(sym.COLON); }
"," 		{ return new_symbol(sym.COMMA); }
"." 		{ return new_symbol(sym.DOT); }
"(" 		{ return new_symbol(sym.LPAREN); }
")" 		{ return new_symbol(sym.RPAREN); }
"{" 		{ return new_symbol(sym.LBRACE); }
"}"			{ return new_symbol(sym.RBRACE); }
"[" 		{ return new_symbol(sym.LBRACKET); }
"]"			{ return new_symbol(sym.RBRACKET); }
"?"			{ return new_symbol(sym.QUESTION); }

// Comments
"//" 		     { yybegin(COMMENT); }
<COMMENT> .      { yybegin(COMMENT); }
<COMMENT> "\r\n" { yybegin(YYINITIAL); }

'.' { return new_symbol(sym.CHAR, yytext().charAt(1)); }
("true" | "false") { return new_symbol(sym.BOOL, Boolean.parseBoolean(yytext())); }
[0-9]+  { return new_symbol(sym.NUMBER, new Integer (yytext())); }
([a-z]|[A-Z])[a-z|A-Z|0-9|_]* 	{return new_symbol (sym.IDENT, yytext()); }

. { updateOrInitializeErrorContext(yytext(), yyline, yycolumn); }
