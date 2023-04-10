package top.criwits.scaleda
package verilog.references

import verilog.psi.nodes.instantiation.{ModuleInstantiationPsiNode, NamedParameterAssignmentPsiNode}

import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.{PsiElement, PsiElementResolveResult, PsiReferenceBase}
import top.criwits.scaleda.verilog.psi.nodes.module.ModuleDeclarationPsiNode
import top.criwits.scaleda.verilog.psi.nodes.signal.parameter.ParameterIdentifierPsiNode

class NamedParameterAssignmentReference(element: NamedParameterAssignmentPsiNode)
  extends PsiReferenceBase[NamedParameterAssignmentPsiNode](element, element.getHoldPsiNodeRelativeTextRange) {
  override def resolve(): PsiElement = {
    // first: find which module
    val moduleInstantiationPsiNode = PsiTreeUtil.getParentOfType(myElement, classOf[ModuleInstantiationPsiNode])
    if (moduleInstantiationPsiNode == null) return null

    moduleInstantiationPsiNode.getReference.multiResolve(false)
      .map(it => it.getElement)
      .filter(it => it.isInstanceOf[ModuleDeclarationPsiNode])
      .map(it => it.asInstanceOf[ModuleDeclarationPsiNode])
      .map(it => it.getParameters)
      .foldLeft(Seq[ParameterIdentifierPsiNode]())(_ ++ _)
      .find(element.getHoldPsiNode != null &&  _.getName == element.getHoldPsiNode.getName) // fix NPE
      .orNull
  }
}
