package rs.ac.bg.etf.pp1;

import java_cup.runtime.Symbol;

%%

%{
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
	return new_symbol(sym.EOF);
%eofval}

%%

// White spaces
" " 	{ }
"\b" 	{ }
"\t" 	{ }
"\r\n" 	{ }
"\f" 	{ }

// Key words
"program"   { return new_symbol(sym.PROGRAM); }
"break" 	{ return new_symbol(sym.BREAK); }
"class" 	{ return new_symbol(sym.CLASS); }
"const" 	{ return new_symbol(sym.CONST); }
"if" 	{ return new_symbol(sym.IF); }
"else" 	{ return new_symbol(sym.ELSE); }
"switch" 	{ return new_symbol(sym.SWITCH); }
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

"\".\"" { return new_symbol(sym.CHAR, yytext()); }
("true" | "false") { return new_symbol(sym.BOOL); }
[0-9]+  { return new_symbol(sym.NUMBER, new Integer (yytext())); }
([a-z]|[A-Z])[a-z|A-Z|0-9|_]* 	{return new_symbol (sym.IDENT, yytext()); }

. { System.err.println("Leksicka greska ("+yytext()+") u liniji "+(yyline+1)); }
