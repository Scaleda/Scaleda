package top.criwits.scaleda
package verilog.completion.keywords

import com.intellij.psi.{PsiComment, PsiElement}
import com.intellij.psi.filters.ElementFilter
import com.intellij.psi.util.PsiTreeUtil
import top.criwits.scaleda.verilog.psi.nodes.SourceTextPsiNode

class ModuleFilter extends ElementFilter {
  override def isAcceptable(element: Any, context: PsiElement): Boolean = {
    // context is SimpleIdentifier; parent of context is Psi
    if (context == null || context.isInstanceOf[PsiComment]) return false
    val leaf = PsiTreeUtil.getDeepestFirst(context) // should be itself

    if (leaf != null) {
      val parent = leaf.getParent
      if (parent == null) return false
      return parent.getParent match {
        case _: SourceTextPsiNode => true
        case _ => false
      }
    }

    false
  }

  override def isClassAcceptable(hintClass: Class[_]): Boolean = true
}
