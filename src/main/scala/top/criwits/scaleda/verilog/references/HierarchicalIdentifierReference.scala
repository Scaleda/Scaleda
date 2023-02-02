package top.criwits.scaleda
package verilog.references

import top.criwits.scaleda.verilog.psi.nodes.module.ModuleDeclarationPsiNode
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.{PsiElement, PsiReferenceBase}
import top.criwits.scaleda.verilog.psi.nodes.IdentifierPsiNode
import top.criwits.scaleda.verilog.psi.nodes.expression.HierarchicalIdentifierPsiNode

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

//    override def resolve(): PsiElement? {
//        return (PsiTreeUtil
//            .getParentOfType(
//                myElement,
//                ModuleDeclarationPsiNode::class.java
//            ) ?: return null)
////                .availableNamedElements
////                .find { it.name == myElement.name }
//    }

  override def resolve(): PsiElement = {
    val module = PsiTreeUtil.getParentOfType(myElement, classOf[ModuleDeclarationPsiNode])
    if (module == null) return null

    val identifiers = PsiTreeUtil.findChildrenOfAnyType(module, classOf[IdentifierPsiNode]).asScala
    identifiers.find(p => p.getName == element.getName).orNull
  }

  override def getVariants: Array[AnyRef] = Array.empty
}
