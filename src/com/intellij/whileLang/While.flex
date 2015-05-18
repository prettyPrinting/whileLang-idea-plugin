package com.intellij.whileLang;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;
import com.intellij.whileLang.psi.WhileTypes;
import com.intellij.psi.TokenType;

%%

%class WhileLexer
%implements FlexLexer
%unicode
%function advance
%type IElementType
%eof{  return;
%eof}

CRLF= \n|\r|\r\n
WHITE_SPACE=[\ \t\f]
FIRST_VALUE_CHARACTER=[^ \n\r\f\\] | "\\"{CRLF} | "\\".
VALUE_CHARACTER=[^\n\r\f\\] | "\\"{CRLF} | "\\".
END_OF_LINE_COMMENT=("#"|"!")[^\r\n]*
//SEPARATOR=[:=]
KEY_CHARACTER=[^:=\ \n\r\t\f\\] | "\\"{CRLF} | "\\".

ASSIGN=[:=]
SEPARATOR=[;]

%state WAITING_VALUE

%%

<YYINITIAL> {END_OF_LINE_COMMENT}                           { yybegin(YYINITIAL); return WhileTypes.COMMENT; }

<YYINITIAL> {KEY_CHARACTER}+                                { yybegin(YYINITIAL); return WhileTypes.KEY; }

<YYINITIAL> {SEPARATOR}                                     { yybegin(YYINITIAL); return WhileTypes.SEPARATOR; }
<YYINITIAL> {ASSIGN}                                        { yybegin(WAITING_VALUE); return WhileTypes.ASSIGN; }

<WAITING_VALUE> {CRLF}                                      { yybegin(YYINITIAL); return WhileTypes.CRLF; }

<WAITING_VALUE> {WHITE_SPACE}+                              { yybegin(WAITING_VALUE); return TokenType.WHITE_SPACE; }

<WAITING_VALUE> {FIRST_VALUE_CHARACTER}{VALUE_CHARACTER}*   { yybegin(YYINITIAL); return WhileTypes.VALUE; }

{CRLF}                                                      { yybegin(YYINITIAL); return WhileTypes.CRLF; }

{WHITE_SPACE}+                                              { yybegin(YYINITIAL); return TokenType.WHITE_SPACE; }

.                                                           { return TokenType.BAD_CHARACTER; }