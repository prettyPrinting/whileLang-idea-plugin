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
//FIRST_VALUE_CHARACTER=[^ ;\n\r\f\\] | "\\"{CRLF} | "\\".
//VALUE_CHARACTER=[^;:=\n\r\f\\] | "\\"{CRLF} | "\\".
END_OF_LINE_COMMENT=("#"|"!")[^\r\n]*
//KEY_CHARACTER=[^;:=\ \n\r\t\f\\] | "\\"{CRLF} | "\\".

ASSIGN=":="
SEPARATOR=";"
IF="if"
THEN="then"
ELSE="else"
FI="fi"
WHILE="while"
DO="do"
OD="od"
READ="read"
WRITE="write"
VARNAME=[:letter:]([0-9]|[:letter:])*
NUMBER=[0-9]+

%state WAITING_VALUE

%%

<YYINITIAL> {END_OF_LINE_COMMENT}                           { yybegin(YYINITIAL); return WhileTypes.COMMENT; }


<YYINITIAL> {SEPARATOR}                                     { yybegin(YYINITIAL); return WhileTypes.SEPARATOR; }
<YYINITIAL> {ASSIGN}                                        { yybegin(YYINITIAL); return WhileTypes.ASSIGN; }
<YYINITIAL> {IF}                                            { yybegin(YYINITIAL); return WhileTypes.IF; }
<YYINITIAL> {FI}                                            { yybegin(YYINITIAL); return WhileTypes.FI; }
<YYINITIAL> {THEN}                                          { yybegin(YYINITIAL); return WhileTypes.THEN; }
<YYINITIAL> {ELSE}                                          { yybegin(YYINITIAL); return WhileTypes.ELSE; }
<YYINITIAL> {WHILE}                                         { yybegin(YYINITIAL); return WhileTypes.WHILE; }
<YYINITIAL> {DO}                                            { yybegin(YYINITIAL); return WhileTypes.DO; }
<YYINITIAL> {OD}                                            { yybegin(YYINITIAL); return WhileTypes.OD; }
<YYINITIAL> {READ}                                          { yybegin(YYINITIAL); return WhileTypes.READ; }
<YYINITIAL> {WRITE}                                         { yybegin(YYINITIAL); return WhileTypes.WRITE; }
<YYINITIAL> {VARNAME}                                       { yybegin(YYINITIAL); return WhileTypes.VARNAME; }
<YYINITIAL> {NUMBER}                                        { yybegin(YYINITIAL); return WhileTypes.NUMBER; }

//<YYINITIAL> {KEY_CHARACTER}+                                { yybegin(YYINITIAL); return WhileTypes.KEY; }
<WAITING_VALUE> {CRLF}                                      { yybegin(YYINITIAL); return WhileTypes.CRLF; }

//<WAITING_VALUE> {WHITE_SPACE}+                              { yybegin(WAITING_VALUE); return TokenType.WHITE_SPACE; }

//<WAITING_VALUE> {FIRST_VALUE_CHARACTER}{VALUE_CHARACTER}*   { yybegin(YYINITIAL); return WhileTypes.VALUE; }

{CRLF}                                                      { yybegin(YYINITIAL); return WhileTypes.CRLF; }

{WHITE_SPACE}+                                              { yybegin(YYINITIAL); return TokenType.WHITE_SPACE; }

.                                                           { return TokenType.BAD_CHARACTER; }