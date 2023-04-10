package top.criwits.scaleda
package verilog.references

import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.{PsiElement, PsiElementResolveResult, PsiReferenceBase, ResolveResult}
import top.criwits.scaleda.verilog.psi.nodes.instantiation.{ModuleInstantiationPsiNode, NamedPortConnectionPsiNode}
import top.criwits.scaleda.verilog.psi.nodes.module.ModuleDeclarationPsiNode
import top.criwits.scaleda.verilog.psi.nodes.signal.{PortDeclarationPsiNode, PortIdentifierPsiNode}

class NamedPortConnectionReference(element: NamedPortConnectionPsiNode)
  extends PsiReferenceBase[NamedPortConnectionPsiNode](
    element,
    element.getHoldPsiNodeRelativeTextRange
  ) {
  override def resolve: PsiElement = {
    // first: find which module
    val moduleInstantiationPsiNode = PsiTreeUtil.getParentOfType(myElement, classOf[ModuleInstantiationPsiNode])
    if (moduleInstantiationPsiNode == null) return null

    moduleInstantiationPsiNode.getReference.multiResolve(incompleteCode)
      .map(it => it.getElement)
      .filter(it => it.isInstanceOf[ModuleDeclarationPsiNode])
      .map(it => it.asInstanceOf[ModuleDeclarationPsiNode])
      .map(it => it.getModuleHead.getPorts)
      .foldLeft(Seq[PortIdentifierPsiNode]())(_ ++ _)
      .filter(_.getName == element.getHoldPsiNode.getName)
      .map(new PsiElementResolveResult((_)))
      .toArray
  }
}
