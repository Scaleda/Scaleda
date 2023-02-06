package top.criwits.scaleda
package verilog.completion.keywords

import com.intellij.psi.PsiElement
import com.intellij.psi.filters.ElementFilter
import top.criwits.scaleda.verilog.psi.nodes.instantiation.ModuleInstantiationPsiNode

class ModuleItemFilter extends ElementFilter {
  override def isAcceptable(element: Any, context: PsiElement): Boolean = {
    try {
      val p = context.getParent.getParent.getParent
      return p match {
        case _: ModuleInstantiationPsiNode => return true
        case _                             => return false
      }
    } catch {
      case _: Throwable => return false
    }

    false
  }

  override def isClassAcceptable(hintClass: Class[_]): Boolean = true
}
