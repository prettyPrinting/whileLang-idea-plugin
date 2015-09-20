// This is a generated file. Not intended for manual editing.
package generated.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.intellij.whileLang.psi.WhileTypes.*;
import com.intellij.whileLang.psi.impl.*;

public class PsiBinaryBexprImpl extends PsiBexprImpl implements PsiBinaryBexpr {

  public PsiBinaryBexprImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PsiVisitor) ((PsiVisitor)visitor).visitBinaryBexpr(this);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<PsiBexpr> getBexprList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PsiBexpr.class);
  }

  @Override
  @NotNull
  public PsiBlOp getBlOp() {
    return findNotNullChildByClass(PsiBlOp.class);
  }

  @Override
  @NotNull
  public PsiBexpr getLeft() {
    List<PsiBexpr> p1 = getBexprList();
    return p1.get(0);
  }

  @Override
  @Nullable
  public PsiBexpr getRight() {
    List<PsiBexpr> p1 = getBexprList();
    return p1.size() < 2 ? null : p1.get(1);
  }

}
