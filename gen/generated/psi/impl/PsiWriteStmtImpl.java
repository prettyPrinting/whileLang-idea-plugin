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

public class PsiWriteStmtImpl extends PsiStmtImpl implements PsiWriteStmt {

  public PsiWriteStmtImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof PsiVisitor) ((PsiVisitor)visitor).visitWriteStmt(this);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public PsiExpr getExpr() {
    return findNotNullChildByClass(PsiExpr.class);
  }

}
