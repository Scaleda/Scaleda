package top.scaleda
package systemverilog.references

import systemverilog.psi.nodes.instantiation.{ModuleInstantiationPsiNode, OrderedPortConnectionPsiNode}
import systemverilog.psi.nodes.module.ModuleDeclarationPsiNode

import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.{PsiElement, PsiReferenceBase}

class OrderedPortConnectionReference(element: OrderedPortConnectionPsiNode, index: Int)
  extends PsiReferenceBase[OrderedPortConnectionPsiNode](element, element.getHoldPsiNodeRelativeTextRange) {
  override def resolve(): PsiElement = {
    val moduleInstantiationPsiNode = PsiTreeUtil.getParentOfType(myElement, classOf[ModuleInstantiationPsiNode])
    if (moduleInstantiationPsiNode == null) return null

    val ref = moduleInstantiationPsiNode.getReference.resolve
    if (!ref.isInstanceOf[ModuleDeclarationPsiNode]) return null

    val head = ref.asInstanceOf[ModuleDeclarationPsiNode].getModuleHead
    if (head.isEmpty) return  null
    val ports = head.get.getPorts
    if (ports.size <= index) return null
    ports(index)
  }
}
