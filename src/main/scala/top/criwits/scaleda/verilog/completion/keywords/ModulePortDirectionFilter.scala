package top.criwits.scaleda
package verilog.completion.keywords

import com.intellij.psi.{PsiComment, PsiElement}
import com.intellij.psi.filters.ElementFilter
import com.intellij.psi.util.PsiTreeUtil
import top.criwits.scaleda.verilog.psi.nodes.instantiation.ModuleInstantiationPsiNode
import top.criwits.scaleda.verilog.psi.nodes.signal.PortIdentifierPsiNode

class ModulePortDirectionFilter extends ElementFilter {
  override def isAcceptable(element: Any, context: PsiElement): Boolean = {
    if (context == null || context.isInstanceOf[PsiComment]) return false

    val identifier = context.getParent
    if (identifier == null) return false
    val port = identifier.getParent
    if (port != null) {
      return port match {
        case _: PortIdentifierPsiNode => {
          val isInstantiation = PsiTreeUtil.getParentOfType(context, classOf[ModuleInstantiationPsiNode])
          if (isInstantiation == null)
          return true
          else return false
        }
        case _ => return false
      }
    }

    false
  }

  override def isClassAcceptable(hintClass: Class[_]): Boolean = true
}
