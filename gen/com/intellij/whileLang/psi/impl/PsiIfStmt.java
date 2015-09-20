// This is a generated file. Not intended for manual editing.
package com.intellij.whileLang.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface PsiIfStmt extends PsiStmt {

  @NotNull
  PsiBexpr getBexpr();

  @NotNull
  List<PsiStmtList> getStmtListList();

  @NotNull
  PsiStmtList getThenBranch();

  @Nullable
  PsiStmtList getElseBranch();

}
