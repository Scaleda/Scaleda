package top.criwits.scaleda
package idea.verilog.references

import verilog.psi.factory.nodes.{ModuleDeclarationPsiNode}

import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.{PsiElementResolveResult, PsiReferenceBase, ResolveResult}
import top.criwits.scaleda.idea.verilog.psi.factory.nodes.NamedPortConnectionPsiNode

class NamedPortConnectionReference(element: NamedPortConnectionPsiNode)
  extends PsiReferenceBase.Poly[NamedPortConnectionPsiNode](
    element,
    element.getHoldPsiNodeRelativeTextRange,
    true
  ) {
  override def multiResolve(incompleteCode: Boolean): Array[ResolveResult] = {
    val moduleInstantiationPsiNode = PsiTreeUtil.getParentOfType(myElement, classOf[ModuleInstantiationPsiNode])
    moduleInstantiationPsiNode.getReference.multiResolve(incompleteCode)
      .map(it => it.getElement)
      .filter(it => it.isInstanceOf[ModuleDeclarationPsiNode])
      .map(it => it.asInstanceOf[ModuleDeclarationPsiNode])
      // .flatMap
      .filter(it => it.getName == myElement.getHoldPsiNode.getName)
      .map(new PsiElementResolveResult(_))
      .toArray
  }

  override def getVariants = Array()
}
