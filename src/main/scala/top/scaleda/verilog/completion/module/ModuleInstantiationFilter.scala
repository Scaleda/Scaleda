package top.scaleda
package verilog.completion.module

import com.intellij.psi.PsiElement
import com.intellij.psi.filters.ElementFilter
import top.scaleda.verilog.psi.nodes.instantiation.ModuleInstantiationPsiNode
import top.scaleda.verilog.psi.nodes.module.ModuleIdentifierPsiNode

class ModuleInstantiationFilter extends ElementFilter {
  override def isAcceptable(element: Any, context: PsiElement): Boolean = {
    try {
      val identifier = context.getParent.getParent
      val insIdentifier = identifier.getParent
      if (identifier.isInstanceOf[ModuleIdentifierPsiNode] && insIdentifier.isInstanceOf[ModuleInstantiationPsiNode]) {
        return true
      } else return false
    } catch {
      case _: Throwable => false
    }
  }

  override def isClassAcceptable(hintClass: Class[_]): Boolean = true
}
