package com.intellij.whileLang;
import com.intellij.lexer.*;
import com.intellij.psi.tree.IElementType;
import static com.intellij.whileLang.psi.WhileTypes.*;

%%

%{
  public _WhileLexer() {
    this((java.io.Reader)null);
  }
%}

%public
%class _WhileLexer
%implements FlexLexer
%function advance
%type IElementType
%unicode

EOL="\r"|"\n"|"\r\n"
LINE_WS=[\ \t\f]
WHITE_SPACE=({LINE_WS}|{EOL})+

CRLF=(\r\n|\n)
NUMBER=[0-9]+(\.[0-9]*)?
ASSIGN=\:=
ID=[:letter:][a-zA-Z_0-9]*

%%
<YYINITIAL> {
  {WHITE_SPACE}      { return com.intellij.psi.TokenType.WHITE_SPACE; }

  ";"                { return SEP; }
  ","                { return COMMA; }
  "write"            { return WRITE; }
  "read"             { return READ; }
  "while"            { return WHILE; }
  "do"               { return DO; }
  "od"               { return OD; }
  "if"               { return IF; }
  "fi"               { return FI; }
  "then"             { return THEN; }
  "else"             { return ELSE; }
  "proc"             { return PROC; }
  "endp"             { return ENDP; }
  "skip"             { return SKIP; }
  "not"              { return NOT; }
  "or"               { return OR; }
  "and"              { return AND; }
  "true"             { return TRUE; }
  "false"            { return FALSE; }
  "COMMENT"          { return COMMENT; }

  {CRLF}             { return CRLF; }
  {NUMBER}           { return NUMBER; }
  {ASSIGN}           { return ASSIGN; }
  {ID}               { return ID; }

  [^] { return com.intellij.psi.TokenType.BAD_CHARACTER; }
}
