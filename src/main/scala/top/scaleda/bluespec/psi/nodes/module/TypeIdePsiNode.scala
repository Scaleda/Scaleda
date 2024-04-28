package top.scaleda
package bluespec.psi.nodes.module

import bluespec.psi.nodes.{IdentifierPsiNode, ReferenceHolderAutoRange, SimpleIdentifierOwnerPsiNode}
import bluespec.references.ModuleReference

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiReference
import com.intellij.psi.util.PsiTreeUtil
import org.jetbrains.annotations.NotNull

class TypeIdePsiNode(@NotNull node: ASTNode)
    extends SimpleIdentifierOwnerPsiNode(node)
    with ReferenceHolderAutoRange[IdentifierPsiNode] {
  override def getHoldPsiNode: IdentifierPsiNode = PsiTreeUtil.findChildOfType(this, classOf[IdentifierPsiNode])

  override def getReference: PsiReference = new ModuleReference(this)
}
