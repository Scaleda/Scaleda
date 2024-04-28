package top.scaleda
package bluespec.references

import bluespec.psi.nodes.module.TypeIdePsiNode

import com.intellij.psi.{PsiElement, PsiReferenceBase}

class ModuleReference(element: TypeIdePsiNode)
    extends PsiReferenceBase[TypeIdePsiNode](element, element.getHoldPsiNodeRelativeTextRange) {
  // TODO: import paths
  override def resolve(): PsiElement = null
}
