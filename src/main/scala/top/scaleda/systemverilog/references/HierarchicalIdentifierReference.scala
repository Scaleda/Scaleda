package top.scaleda
package systemverilog.references

import systemverilog.psi.nodes.expression.HierarchicalIdentifierPsiNode
import systemverilog.psi.nodes.module.ModuleDeclarationPsiNode
import systemverilog.psi.nodes.signal.{SignalDeclarationPsiNode, SignalIdentifierPsiNode}

import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.{PsiElement, PsiReferenceBase}

/**
 * This [[com.intellij.psi.PsiReference]] aids a [[HierarchicalIdentifierPsiNode]] to resolve to its definition.
 * @param element A [[HierarchicalIdentifierPsiNode]]
 */
class HierarchicalIdentifierReference(element: HierarchicalIdentifierPsiNode)
    extends PsiReferenceBase[HierarchicalIdentifierPsiNode](
      element,
      element.getHoldPsiNodeRelativeTextRange()
    ) {

  override def resolve(): PsiElement = {
    val module = PsiTreeUtil.getParentOfType(myElement, classOf[ModuleDeclarationPsiNode])
    if (module == null) return null

    val moduleParams = module.getParameters
    val head = module.getModuleHead // todo use getPorts directly
    if (head.isEmpty) return null
    val ports = head.get.getPorts
    val declarations = module.getChildrenInModuleBody(classOf[SignalDeclarationPsiNode])
    val ids: Set[SignalIdentifierPsiNode] = (moduleParams ++ ports ++ declarations.map(_.getIdentifier).foldLeft(Seq[SignalIdentifierPsiNode]())(_ ++ _)).toSet

    ids.find(p => p.getName == element.getName).orNull
  }

  override def getVariants: Array[AnyRef] = Array.empty
}
