package top.criwits.scaleda
package verilog.references

import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.{PsiElement, PsiReferenceBase}
import top.criwits.scaleda.verilog.psi.nodes.instantiation.{ModuleInstantiationPsiNode, OrderedPortConnectionPsiNode}
import top.criwits.scaleda.verilog.psi.nodes.module.ModuleDeclarationPsiNode

class OrderedPortConnectionReference(element: OrderedPortConnectionPsiNode, index: Int)
  extends PsiReferenceBase[OrderedPortConnectionPsiNode](element, element.getHoldPsiNodeRelativeTextRange) {
  override def resolve(): PsiElement = {
    val moduleInstantiationPsiNode = PsiTreeUtil.getParentOfType(myElement, classOf[ModuleInstantiationPsiNode])
    if (moduleInstantiationPsiNode == null) return null

    val ref = moduleInstantiationPsiNode.getReference.resolve
    if (!ref.isInstanceOf[ModuleDeclarationPsiNode]) return null

    val ports = ref.asInstanceOf[ModuleDeclarationPsiNode].getModuleHead.getPorts
    if (ports.size <= index) return null
    ports(index)
  }
}
