// This is a generated file. Not intended for manual editing.
package com.intellij.whileLang.psi.impl;

import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.PsiElement;

public class PsiVisitor extends PsiElementVisitor {

  public void visitAndBexpr(@NotNull PsiAndBexpr o) {
    visitBinaryBexpr(o);
  }

  public void visitArOp(@NotNull PsiArOp o) {
    visitElement(o);
  }

  public void visitAssignStmt(@NotNull PsiAssignStmt o) {
    visitStmt(o);
  }

  public void visitBexpr(@NotNull PsiBexpr o) {
    visitElement(o);
  }

  public void visitBinaryBexpr(@NotNull PsiBinaryBexpr o) {
    visitBexpr(o);
  }

  public void visitBinaryExpr(@NotNull PsiBinaryExpr o) {
    visitExpr(o);
  }

  public void visitBlOp(@NotNull PsiBlOp o) {
    visitElement(o);
  }

  public void visitExpr(@NotNull PsiExpr o) {
    visitElement(o);
  }

  public void visitIfStmt(@NotNull PsiIfStmt o) {
    visitStmt(o);
  }

  public void visitLiteralBexpr(@NotNull PsiLiteralBexpr o) {
    visitBexpr(o);
  }

  public void visitLiteralExpr(@NotNull PsiLiteralExpr o) {
    visitExpr(o);
  }

  public void visitMulExpr(@NotNull PsiMulExpr o) {
    visitBinaryExpr(o);
  }

  public void visitMulOp(@NotNull PsiMulOp o) {
    visitArOp(o);
  }

  public void visitNotBexpr(@NotNull PsiNotBexpr o) {
    visitBexpr(o);
  }

  public void visitOrBexpr(@NotNull PsiOrBexpr o) {
    visitBinaryBexpr(o);
  }

  public void visitParamList(@NotNull PsiParamList o) {
    visitElement(o);
  }

  public void visitParenBexpr(@NotNull PsiParenBexpr o) {
    visitBexpr(o);
  }

  public void visitParenExpr(@NotNull PsiParenExpr o) {
    visitExpr(o);
  }

  public void visitPlusExpr(@NotNull PsiPlusExpr o) {
    visitExpr(o);
  }

  public void visitPlusOp(@NotNull PsiPlusOp o) {
    visitArOp(o);
  }

  public void visitProcList(@NotNull PsiProcList o) {
    visitElement(o);
  }

  public void visitProcedure(@NotNull PsiProcedure o) {
    visitElement(o);
  }

  public void visitReadStmt(@NotNull PsiReadStmt o) {
    visitStmt(o);
  }

  public void visitRefExpr(@NotNull PsiRefExpr o) {
    visitExpr(o);
  }

  public void visitRel(@NotNull PsiRel o) {
    visitArOp(o);
  }

  public void visitRelBexpr(@NotNull PsiRelBexpr o) {
    visitBexpr(o);
  }

  public void visitSkipStmt(@NotNull PsiSkipStmt o) {
    visitStmt(o);
  }

  public void visitStmt(@NotNull PsiStmt o) {
    visitElement(o);
  }

  public void visitStmtList(@NotNull PsiStmtList o) {
    visitElement(o);
  }

  public void visitWhileStmt(@NotNull PsiWhileStmt o) {
    visitStmt(o);
  }

  public void visitWriteStmt(@NotNull PsiWriteStmt o) {
    visitStmt(o);
  }

  public void visitElement(@NotNull PsiElement o) {
    visitElement(o);
  }

}
