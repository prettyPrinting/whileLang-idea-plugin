// This is a generated file. Not intended for manual editing.
package generated.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.intellij.whileLang.psi.WhileTypes.*;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.intellij.whileLang.psi.impl.*;

public class PsiProcedureImpl extends ASTWrapperPsiElement implements PsiProcedure {

  public PsiProcedureImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PsiVisitor) ((PsiVisitor)visitor).visitProcedure(this);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public PsiParamList getParamList() {
    return findNotNullChildByClass(PsiParamList.class);
  }

  @Override
  @NotNull
  public PsiStmtList getStmtList() {
    return findNotNullChildByClass(PsiStmtList.class);
  }

  @Override
  @NotNull
  public PsiElement getId() {
    return findNotNullChildByType(ID);
  }

}
