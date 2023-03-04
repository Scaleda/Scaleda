package top.criwits.scaleda
package verilog.completion.module

import com.intellij.psi.PsiElement
import com.intellij.psi.filters.ElementFilter
import com.intellij.psi.util.PsiTreeUtil
import top.criwits.scaleda.verilog.psi.nodes.instantiation.NamedPortConnectionPsiNode
import top.criwits.scaleda.verilog.psi.nodes.signal.PortIdentifierPsiNode

class ModulePortFilter extends ElementFilter {
  override def isClassAcceptable(hintClass: Class[_]): Boolean = true

  override def isAcceptable(element: Any, context: PsiElement): Boolean = {
    val portIdentifier = PsiTreeUtil.getParentOfType(context, classOf[PortIdentifierPsiNode])
    val namedPortConn = PsiTreeUtil.getParentOfType(context, classOf[NamedPortConnectionPsiNode])

    if (portIdentifier != null && namedPortConn != null) {
      true
    } else false
  }
}
