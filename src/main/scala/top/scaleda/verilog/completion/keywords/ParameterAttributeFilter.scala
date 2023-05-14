package top.scaleda
package verilog.completion.keywords

import com.intellij.psi.filters.ElementFilter
import com.intellij.psi.{PsiComment, PsiElement}
import top.scaleda.verilog.psi.nodes.signal.parameter.ParameterIdentifierPsiNode

class ParameterAttributeFilter extends ElementFilter {
  override def isAcceptable(element: Any, context: PsiElement): Boolean = {
    if (context == null || context.isInstanceOf[PsiComment]) return false

    val parent = context.getParent
    if (parent == null) return false
    return parent.getParent match {
      case parent: ParameterIdentifierPsiNode => {
        val root = parent.getParent.getParent.getParent
        return !root.getChildren.exists(element =>
          element.textMatches("signed") || element.textMatches("integer") || element.textMatches("real") || element
            .textMatches("realtime") || element.textMatches("time")
        )

      }
      case _ => return false
    }

    false
  }

  override def isClassAcceptable(hintClass: Class[_]): Boolean = true
}
