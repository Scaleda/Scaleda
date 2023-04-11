package top.criwits.scaleda
package verilog.references

import top.criwits.scaleda.verilog.psi.nodes.module.ModuleDeclarationPsiNode
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.{PsiElement, PsiReferenceBase}
import top.criwits.scaleda.verilog.psi.nodes.IdentifierPsiNode
import top.criwits.scaleda.verilog.psi.nodes.expression.HierarchicalIdentifierPsiNode
import top.criwits.scaleda.verilog.psi.nodes.signal.SignalIdentifierPsiNode
import top.criwits.scaleda.verilog.psi.nodes.signal.parameter.ParameterIdentifierPsiNode

import scala.jdk.CollectionConverters._

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
    val ports = module.getModuleHead.getPorts // todo use getPorts directly
    val identifiers = module.getChildrenInModuleBody(classOf[SignalIdentifierPsiNode])
    val ids: Set[SignalIdentifierPsiNode] = (moduleParams ++ ports ++ identifiers).toSet

    ids.find(p => p.getName == element.getName).orNull
  }

  override def getVariants: Array[AnyRef] = Array.empty
}
