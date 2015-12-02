// This is a generated file. Not intended for manual editing.
package com.intellij.whileLang.parser;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilder.Marker;
import static com.intellij.whileLang.psi.WhileTypes.*;
import static com.intellij.lang.parser.GeneratedParserUtilBase.*;
import com.intellij.psi.tree.IElementType;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.TokenSet;
import com.intellij.lang.PsiParser;
import com.intellij.lang.LightPsiParser;

@SuppressWarnings({"SimplifiableIfStatement", "UnusedAssignment"})
public class WhileParser implements PsiParser, LightPsiParser {

  public ASTNode parse(IElementType t, PsiBuilder b) {
    parseLight(t, b);
    return b.getTreeBuilt();
  }

  public void parseLight(IElementType t, PsiBuilder b) {
    boolean r;
    b = adapt_builder_(t, b, this, EXTENDS_SETS_);
    Marker m = enter_section_(b, 0, _COLLAPSE_, null);
    if (t == AND_BEXPR) {
      r = and_bexpr(b, 0);
    }
    else if (t == ASSIGN_STMT) {
      r = assign_stmt(b, 0);
    }
    else if (t == BEXPR) {
      r = bexpr(b, 0);
    }
    else if (t == EXPR) {
      r = expr(b, 0);
    }
    else if (t == IF_STMT) {
      r = if_stmt(b, 0);
    }
    else if (t == LITERAL_BEXPR) {
      r = literal_bexpr(b, 0);
    }
    else if (t == LITERAL_EXPR) {
      r = literal_expr(b, 0);
    }
    else if (t == MUL_EXPR) {
      r = mul_expr(b, 0);
    }
    else if (t == MUL_OP) {
      r = mul_op(b, 0);
    }
    else if (t == NOT_BEXPR) {
      r = not_bexpr(b, 0);
    }
    else if (t == OR_BEXPR) {
      r = or_bexpr(b, 0);
    }
    else if (t == PARAM_LIST) {
      r = param_list(b, 0);
    }
    else if (t == PAREN_BEXPR) {
      r = paren_bexpr(b, 0);
    }
    else if (t == PAREN_EXPR) {
      r = paren_expr(b, 0);
    }
    else if (t == PLUS_EXPR) {
      r = plus_expr(b, 0);
    }
    else if (t == PLUS_OP) {
      r = plus_op(b, 0);
    }
    else if (t == PROC_LIST) {
      r = proc_list(b, 0);
    }
    else if (t == PROCEDURE) {
      r = procedure(b, 0);
    }
    else if (t == READ_STMT) {
      r = read_stmt(b, 0);
    }
    else if (t == REF_EXPR) {
      r = ref_expr(b, 0);
    }
    else if (t == REL) {
      r = rel(b, 0);
    }
    else if (t == REL_BEXPR) {
      r = rel_bexpr(b, 0);
    }
    else if (t == SKIP_STMT) {
      r = skip_stmt(b, 0);
    }
    else if (t == STMT) {
      r = stmt(b, 0);
    }
    else if (t == STMT_LIST) {
      r = stmt_list(b, 0);
    }
    else if (t == WHILE_STMT) {
      r = while_stmt(b, 0);
    }
    else if (t == WRITE_STMT) {
      r = write_stmt(b, 0);
    }
    else {
      r = parse_root_(t, b, 0);
    }
    exit_section_(b, 0, m, t, r, true, TRUE_CONDITION);
  }

  protected boolean parse_root_(IElementType t, PsiBuilder b, int l) {
    return whileFile(b, l + 1);
  }

  public static final TokenSet[] EXTENDS_SETS_ = new TokenSet[] {
    create_token_set_(AND_BEXPR, OR_BEXPR),
    create_token_set_(MUL_OP, PLUS_OP, REL),
    create_token_set_(EXPR, LITERAL_EXPR, MUL_EXPR, PAREN_EXPR,
      PLUS_EXPR, REF_EXPR),
    create_token_set_(ASSIGN_STMT, IF_STMT, READ_STMT, SKIP_STMT,
      STMT, WHILE_STMT, WRITE_STMT),
    create_token_set_(AND_BEXPR, BEXPR, LITERAL_BEXPR, NOT_BEXPR,
      OR_BEXPR, PAREN_BEXPR, REL_BEXPR),
  };

  /* ********************************************************** */
  // AND bprimary
  public static boolean and_bexpr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "and_bexpr")) return false;
    if (!nextTokenIs(b, AND)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _LEFT_, null);
    r = consumeToken(b, AND);
    r = r && bprimary(b, l + 1);
    exit_section_(b, l, m, AND_BEXPR, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // id ASSIGN expr SEP
  public static boolean assign_stmt(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "assign_stmt")) return false;
    if (!nextTokenIs(b, ID)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, ID, ASSIGN);
    r = r && expr(b, l + 1);
    r = r && consumeToken(b, SEP);
    exit_section_(b, m, ASSIGN_STMT, r);
    return r;
  }

  /* ********************************************************** */
  // bfactor or_bexpr *
  public static boolean bexpr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "bexpr")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _COLLAPSE_, "<bexpr>");
    r = bfactor(b, l + 1);
    r = r && bexpr_1(b, l + 1);
    exit_section_(b, l, m, BEXPR, r, false, null);
    return r;
  }

  // or_bexpr *
  private static boolean bexpr_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "bexpr_1")) return false;
    int c = current_position_(b);
    while (true) {
      if (!or_bexpr(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "bexpr_1", c)) break;
      c = current_position_(b);
    }
    return true;
  }

  /* ********************************************************** */
  // bprimary and_bexpr *
  static boolean bfactor(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "bfactor")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = bprimary(b, l + 1);
    r = r && bfactor_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // and_bexpr *
  private static boolean bfactor_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "bfactor_1")) return false;
    int c = current_position_(b);
    while (true) {
      if (!and_bexpr(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "bfactor_1", c)) break;
      c = current_position_(b);
    }
    return true;
  }

  /* ********************************************************** */
  // literal_bexpr | not_bexpr | paren_bexpr | rel_bexpr
  static boolean bprimary(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "bprimary")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = literal_bexpr(b, l + 1);
    if (!r) r = not_bexpr(b, l + 1);
    if (!r) r = paren_bexpr(b, l + 1);
    if (!r) r = rel_bexpr(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // factor plus_expr *
  public static boolean expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expr")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _COLLAPSE_, "<expr>");
    r = factor(b, l + 1);
    r = r && expr_1(b, l + 1);
    exit_section_(b, l, m, EXPR, r, false, null);
    return r;
  }

  // plus_expr *
  private static boolean expr_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expr_1")) return false;
    int c = current_position_(b);
    while (true) {
      if (!plus_expr(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "expr_1", c)) break;
      c = current_position_(b);
    }
    return true;
  }

  /* ********************************************************** */
  // primary mul_expr *
  static boolean factor(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "factor")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = primary(b, l + 1);
    r = r && factor_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // mul_expr *
  private static boolean factor_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "factor_1")) return false;
    int c = current_position_(b);
    while (true) {
      if (!mul_expr(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "factor_1", c)) break;
      c = current_position_(b);
    }
    return true;
  }

  /* ********************************************************** */
  // IF '(' bexpr ')' THEN stmt_list (ELSE stmt_list)? FI
  public static boolean if_stmt(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "if_stmt")) return false;
    if (!nextTokenIs(b, IF)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, IF);
    r = r && consumeToken(b, "(");
    r = r && bexpr(b, l + 1);
    r = r && consumeToken(b, ")");
    r = r && consumeToken(b, THEN);
    r = r && stmt_list(b, l + 1);
    r = r && if_stmt_6(b, l + 1);
    r = r && consumeToken(b, FI);
    exit_section_(b, m, IF_STMT, r);
    return r;
  }

  // (ELSE stmt_list)?
  private static boolean if_stmt_6(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "if_stmt_6")) return false;
    if_stmt_6_0(b, l + 1);
    return true;
  }

  // ELSE stmt_list
  private static boolean if_stmt_6_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "if_stmt_6_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, ELSE);
    r = r && stmt_list(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // TRUE | FALSE
  public static boolean literal_bexpr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "literal_bexpr")) return false;
    if (!nextTokenIs(b, "<literal bexpr>", FALSE, TRUE)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<literal bexpr>");
    r = consumeToken(b, TRUE);
    if (!r) r = consumeToken(b, FALSE);
    exit_section_(b, l, m, LITERAL_BEXPR, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // number
  public static boolean literal_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "literal_expr")) return false;
    if (!nextTokenIs(b, NUMBER)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, NUMBER);
    exit_section_(b, m, LITERAL_EXPR, r);
    return r;
  }

  /* ********************************************************** */
  // mul_op primary
  public static boolean mul_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "mul_expr")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _LEFT_, "<mul expr>");
    r = mul_op(b, l + 1);
    r = r && primary(b, l + 1);
    exit_section_(b, l, m, MUL_EXPR, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // '*'|'/'|'%'
  public static boolean mul_op(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "mul_op")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<mul op>");
    r = consumeToken(b, "*");
    if (!r) r = consumeToken(b, "/");
    if (!r) r = consumeToken(b, "%");
    exit_section_(b, l, m, MUL_OP, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // NOT bexpr
  public static boolean not_bexpr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "not_bexpr")) return false;
    if (!nextTokenIs(b, NOT)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, NOT);
    r = r && bexpr(b, l + 1);
    exit_section_(b, m, NOT_BEXPR, r);
    return r;
  }

  /* ********************************************************** */
  // OR bfactor
  public static boolean or_bexpr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "or_bexpr")) return false;
    if (!nextTokenIs(b, OR)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _LEFT_, null);
    r = consumeToken(b, OR);
    r = r && bfactor(b, l + 1);
    exit_section_(b, l, m, OR_BEXPR, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // ref_expr? (COMMA ref_expr)*
  public static boolean param_list(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "param_list")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<param list>");
    r = param_list_0(b, l + 1);
    r = r && param_list_1(b, l + 1);
    exit_section_(b, l, m, PARAM_LIST, r, false, null);
    return r;
  }

  // ref_expr?
  private static boolean param_list_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "param_list_0")) return false;
    ref_expr(b, l + 1);
    return true;
  }

  // (COMMA ref_expr)*
  private static boolean param_list_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "param_list_1")) return false;
    int c = current_position_(b);
    while (true) {
      if (!param_list_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "param_list_1", c)) break;
      c = current_position_(b);
    }
    return true;
  }

  // COMMA ref_expr
  private static boolean param_list_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "param_list_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && ref_expr(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // '(' bexpr ')'
  public static boolean paren_bexpr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "paren_bexpr")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, "<paren bexpr>");
    r = consumeToken(b, "(");
    p = r; // pin = 1
    r = r && report_error_(b, bexpr(b, l + 1));
    r = p && consumeToken(b, ")") && r;
    exit_section_(b, l, m, PAREN_BEXPR, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // '(' expr ')'
  public static boolean paren_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "paren_expr")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, "<paren expr>");
    r = consumeToken(b, "(");
    p = r; // pin = 1
    r = r && report_error_(b, expr(b, l + 1));
    r = p && consumeToken(b, ")") && r;
    exit_section_(b, l, m, PAREN_EXPR, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // plus_op factor
  public static boolean plus_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "plus_expr")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _LEFT_, "<plus expr>");
    r = plus_op(b, l + 1);
    r = r && factor(b, l + 1);
    exit_section_(b, l, m, PLUS_EXPR, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // '+'|'-'
  public static boolean plus_op(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "plus_op")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<plus op>");
    r = consumeToken(b, "+");
    if (!r) r = consumeToken(b, "-");
    exit_section_(b, l, m, PLUS_OP, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // literal_expr | ref_expr | paren_expr
  static boolean primary(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "primary")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = literal_expr(b, l + 1);
    if (!r) r = ref_expr(b, l + 1);
    if (!r) r = paren_expr(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // procedure*
  public static boolean proc_list(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "proc_list")) return false;
    Marker m = enter_section_(b, l, _NONE_, "<proc list>");
    int c = current_position_(b);
    while (true) {
      if (!procedure(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "proc_list", c)) break;
      c = current_position_(b);
    }
    exit_section_(b, l, m, PROC_LIST, true, false, null);
    return true;
  }

  /* ********************************************************** */
  // PROC id '(' param_list ')' stmt_list ENDP
  public static boolean procedure(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "procedure")) return false;
    if (!nextTokenIs(b, PROC)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, PROC, ID);
    r = r && consumeToken(b, "(");
    r = r && param_list(b, l + 1);
    r = r && consumeToken(b, ")");
    r = r && stmt_list(b, l + 1);
    r = r && consumeToken(b, ENDP);
    exit_section_(b, m, PROCEDURE, r);
    return r;
  }

  /* ********************************************************** */
  // READ '(' id ')' SEP
  public static boolean read_stmt(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "read_stmt")) return false;
    if (!nextTokenIs(b, READ)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, READ);
    r = r && consumeToken(b, "(");
    r = r && consumeToken(b, ID);
    r = r && consumeToken(b, ")");
    r = r && consumeToken(b, SEP);
    exit_section_(b, m, READ_STMT, r);
    return r;
  }

  /* ********************************************************** */
  // id
  public static boolean ref_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ref_expr")) return false;
    if (!nextTokenIs(b, ID)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, ID);
    exit_section_(b, m, REF_EXPR, r);
    return r;
  }

  /* ********************************************************** */
  // '<'|'<='|'='|'>='|'>'
  public static boolean rel(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "rel")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<rel>");
    r = consumeToken(b, "<");
    if (!r) r = consumeToken(b, "<=");
    if (!r) r = consumeToken(b, "=");
    if (!r) r = consumeToken(b, ">=");
    if (!r) r = consumeToken(b, ">");
    exit_section_(b, l, m, REL, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // expr rel expr
  public static boolean rel_bexpr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "rel_bexpr")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, "<rel bexpr>");
    r = expr(b, l + 1);
    r = r && rel(b, l + 1);
    r = r && expr(b, l + 1);
    exit_section_(b, l, m, REL_BEXPR, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // SKIP SEP
  public static boolean skip_stmt(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "skip_stmt")) return false;
    if (!nextTokenIs(b, SKIP)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, SKIP, SEP);
    exit_section_(b, m, SKIP_STMT, r);
    return r;
  }

  /* ********************************************************** */
  // skip_stmt|assign_stmt|if_stmt|while_stmt|write_stmt|read_stmt
  public static boolean stmt(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "stmt")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _COLLAPSE_, "<stmt>");
    r = skip_stmt(b, l + 1);
    if (!r) r = assign_stmt(b, l + 1);
    if (!r) r = if_stmt(b, l + 1);
    if (!r) r = while_stmt(b, l + 1);
    if (!r) r = write_stmt(b, l + 1);
    if (!r) r = read_stmt(b, l + 1);
    exit_section_(b, l, m, STMT, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // stmt*
  public static boolean stmt_list(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "stmt_list")) return false;
    Marker m = enter_section_(b, l, _NONE_, "<stmt list>");
    int c = current_position_(b);
    while (true) {
      if (!stmt(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "stmt_list", c)) break;
      c = current_position_(b);
    }
    exit_section_(b, l, m, STMT_LIST, true, false, null);
    return true;
  }

  /* ********************************************************** */
  // COMMENT|proc_list stmt_list
  static boolean whileFile(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "whileFile")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMENT);
    if (!r) r = whileFile_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // proc_list stmt_list
  private static boolean whileFile_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "whileFile_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = proc_list(b, l + 1);
    r = r && stmt_list(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // WHILE '(' bexpr ')' DO stmt_list OD
  public static boolean while_stmt(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "while_stmt")) return false;
    if (!nextTokenIs(b, WHILE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, WHILE);
    r = r && consumeToken(b, "(");
    r = r && bexpr(b, l + 1);
    r = r && consumeToken(b, ")");
    r = r && consumeToken(b, DO);
    r = r && stmt_list(b, l + 1);
    r = r && consumeToken(b, OD);
    exit_section_(b, m, WHILE_STMT, r);
    return r;
  }

  /* ********************************************************** */
  // WRITE '(' expr ')' SEP
  public static boolean write_stmt(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "write_stmt")) return false;
    if (!nextTokenIs(b, WRITE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, WRITE);
    r = r && consumeToken(b, "(");
    r = r && expr(b, l + 1);
    r = r && consumeToken(b, ")");
    r = r && consumeToken(b, SEP);
    exit_section_(b, m, WRITE_STMT, r);
    return r;
  }

}
