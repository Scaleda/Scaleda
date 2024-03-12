package top.scaleda
package verilog.completion.keywords

import com.intellij.psi.{PsiComment, PsiElement}
import com.intellij.psi.filters.ElementFilter
import com.intellij.psi.util.PsiTreeUtil
import top.scaleda.verilog.psi.nodes.signal.parameter.ParameterDeclarationPsiNode

class ModuleParameterFilter extends ElementFilter {
  override def isAcceptable(element: Any, context: PsiElement): Boolean = {
    if (context == null || context.isInstanceOf[PsiComment]) return false
    val leaf = PsiTreeUtil.getDeepestFirst(context)

    if (leaf != null) {
      val parent = leaf.getParent
      if (parent == null) return false
      return parent.getParent match {
        case _: ParameterDeclarationPsiNode => return true
        case _ => return false
      }
    }

    false
  }

  override def isClassAcceptable(hintClass: Class[_]): Boolean = true
}
