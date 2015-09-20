// This is a generated file. Not intended for manual editing.
package com.intellij.whileLang.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface PsiRelBexpr extends PsiBexpr {

  @NotNull
  List<PsiExpr> getExprList();

  @NotNull
  PsiRel getRel();

  @NotNull
  PsiExpr getLeft();

  @Nullable
  PsiExpr getRight();

}
