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

public class PsiIfStmtImpl extends PsiStmtImpl implements PsiIfStmt {

  public PsiIfStmtImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PsiVisitor) ((PsiVisitor)visitor).visitIfStmt(this);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public PsiBexpr getBexpr() {
    return findNotNullChildByClass(PsiBexpr.class);
  }

  @Override
  @NotNull
  public List<PsiStmtList> getStmtListList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, PsiStmtList.class);
  }

  @Override
  @NotNull
  public PsiStmtList getThenBranch() {
    List<PsiStmtList> p1 = getStmtListList();
    return p1.get(0);
  }

  @Override
  @Nullable
  public PsiStmtList getElseBranch() {
    List<PsiStmtList> p1 = getStmtListList();
    return p1.size() < 2 ? null : p1.get(1);
  }

}
