// This is a generated file. Not intended for manual editing.
package com.intellij.whileLang.psi;

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.PsiElement;
import com.intellij.lang.ASTNode;
import com.intellij.whileLang.WhileElementType;
import com.intellij.whileLang.WhileTokenType;
import generated.psi.impl.*;

public interface WhileTypes {

  IElementType AND_BEXPR = new WhileElementType("AND_BEXPR");
  IElementType ASSIGN_STMT = new WhileElementType("ASSIGN_STMT");
  IElementType BEXPR = new WhileElementType("BEXPR");
  IElementType EXPR = new WhileElementType("EXPR");
  IElementType IF_STMT = new WhileElementType("IF_STMT");
  IElementType LITERAL_BEXPR = new WhileElementType("LITERAL_BEXPR");
  IElementType LITERAL_EXPR = new WhileElementType("LITERAL_EXPR");
  IElementType MUL_EXPR = new WhileElementType("MUL_EXPR");
  IElementType MUL_OP = new WhileElementType("MUL_OP");
  IElementType NOT_BEXPR = new WhileElementType("NOT_BEXPR");
  IElementType OR_BEXPR = new WhileElementType("OR_BEXPR");
  IElementType PARAM_LIST = new WhileElementType("PARAM_LIST");
  IElementType PAREN_BEXPR = new WhileElementType("PAREN_BEXPR");
  IElementType PAREN_EXPR = new WhileElementType("PAREN_EXPR");
  IElementType PLUS_EXPR = new WhileElementType("PLUS_EXPR");
  IElementType PLUS_OP = new WhileElementType("PLUS_OP");
  IElementType PROCEDURE = new WhileElementType("PROCEDURE");
  IElementType PROC_LIST = new WhileElementType("PROC_LIST");
  IElementType READ_STMT = new WhileElementType("READ_STMT");
  IElementType REF_EXPR = new WhileElementType("REF_EXPR");
  IElementType REL = new WhileElementType("REL");
  IElementType REL_BEXPR = new WhileElementType("REL_BEXPR");
  IElementType SKIP_STMT = new WhileElementType("SKIP_STMT");
  IElementType STMT = new WhileElementType("STMT");
  IElementType STMT_LIST = new WhileElementType("STMT_LIST");
  IElementType WHILE_STMT = new WhileElementType("WHILE_STMT");
  IElementType WRITE_STMT = new WhileElementType("WRITE_STMT");

  IElementType AND = new WhileTokenType("and");
  IElementType ASSIGN = new WhileTokenType("ASSIGN");
  IElementType COMMA = new WhileTokenType(",");
  IElementType COMMENT = new WhileTokenType("COMMENT");
  IElementType DO = new WhileTokenType("do");
  IElementType ELSE = new WhileTokenType("else");
  IElementType ENDP = new WhileTokenType("endp");
  IElementType FALSE = new WhileTokenType("false");
  IElementType FI = new WhileTokenType("fi");
  IElementType ID = new WhileTokenType("id");
  IElementType IF = new WhileTokenType("if");
  IElementType NOT = new WhileTokenType("not");
  IElementType NUMBER = new WhileTokenType("number");
  IElementType OD = new WhileTokenType("od");
  IElementType OR = new WhileTokenType("or");
  IElementType PROC = new WhileTokenType("proc");
  IElementType READ = new WhileTokenType("read");
  IElementType SEP = new WhileTokenType(";");
  IElementType SKIP = new WhileTokenType("skip");
  IElementType THEN = new WhileTokenType("then");
  IElementType TRUE = new WhileTokenType("true");
  IElementType WHILE = new WhileTokenType("while");
  IElementType WRITE = new WhileTokenType("write");

  class Factory {
    public static PsiElement createElement(ASTNode node) {
      IElementType type = node.getElementType();
       if (type == AND_BEXPR) {
        return new PsiAndBexprImpl(node);
      }
      else if (type == ASSIGN_STMT) {
        return new PsiAssignStmtImpl(node);
      }
      else if (type == BEXPR) {
        return new PsiBexprImpl(node);
      }
      else if (type == EXPR) {
        return new PsiExprImpl(node);
      }
      else if (type == IF_STMT) {
        return new PsiIfStmtImpl(node);
      }
      else if (type == LITERAL_BEXPR) {
        return new PsiLiteralBexprImpl(node);
      }
      else if (type == LITERAL_EXPR) {
        return new PsiLiteralExprImpl(node);
      }
      else if (type == MUL_EXPR) {
        return new PsiMulExprImpl(node);
      }
      else if (type == MUL_OP) {
        return new PsiMulOpImpl(node);
      }
      else if (type == NOT_BEXPR) {
        return new PsiNotBexprImpl(node);
      }
      else if (type == OR_BEXPR) {
        return new PsiOrBexprImpl(node);
      }
      else if (type == PARAM_LIST) {
        return new PsiParamListImpl(node);
      }
      else if (type == PAREN_BEXPR) {
        return new PsiParenBexprImpl(node);
      }
      else if (type == PAREN_EXPR) {
        return new PsiParenExprImpl(node);
      }
      else if (type == PLUS_EXPR) {
        return new PsiPlusExprImpl(node);
      }
      else if (type == PLUS_OP) {
        return new PsiPlusOpImpl(node);
      }
      else if (type == PROCEDURE) {
        return new PsiProcedureImpl(node);
      }
      else if (type == PROC_LIST) {
        return new PsiProcListImpl(node);
      }
      else if (type == READ_STMT) {
        return new PsiReadStmtImpl(node);
      }
      else if (type == REF_EXPR) {
        return new PsiRefExprImpl(node);
      }
      else if (type == REL) {
        return new PsiRelImpl(node);
      }
      else if (type == REL_BEXPR) {
        return new PsiRelBexprImpl(node);
      }
      else if (type == SKIP_STMT) {
        return new PsiSkipStmtImpl(node);
      }
      else if (type == STMT) {
        return new PsiStmtImpl(node);
      }
      else if (type == STMT_LIST) {
        return new PsiStmtListImpl(node);
      }
      else if (type == WHILE_STMT) {
        return new PsiWhileStmtImpl(node);
      }
      else if (type == WRITE_STMT) {
        return new PsiWriteStmtImpl(node);
      }
      throw new AssertionError("Unknown element type: " + type);
    }
  }
}
