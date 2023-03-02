package top.criwits.scaleda
package verilog.completion.module

import com.intellij.psi.PsiElement
import com.intellij.psi.filters.ElementFilter
import com.intellij.psi.util.PsiTreeUtil
import top.criwits.scaleda.verilog.psi.nodes.instantiation.NameOfInstancePsiNode

class ModuleInstanceNameFilter extends ElementFilter {
  override def isAcceptable(element: Any, context: PsiElement): Boolean = {
    val name = PsiTreeUtil.getParentOfType(context, classOf[NameOfInstancePsiNode])
    if (name != null) true
    else false
  }

  override def isClassAcceptable(hintClass: Class[_]): Boolean = true
}
