// This is a generated file. Not intended for manual editing.
package com.intellij.whileLang.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface PsiBinaryBexpr extends PsiBexpr {

  @NotNull
  List<PsiBexpr> getBexprList();

  @NotNull
  PsiBlOp getBlOp();

  @NotNull
  PsiBexpr getLeft();

  @Nullable
  PsiBexpr getRight();

}
