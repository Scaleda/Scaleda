package top.criwits.scaleda
package verilog.document

import idea.ScaledaBundle
import verilog.psi.nodes.expression.HierarchicalIdentifierPsiNode

import com.intellij.lang.documentation.DocumentationProvider
import com.intellij.psi.PsiElement
import org.jetbrains.annotations.Nls
import top.criwits.scaleda.verilog.psi.nodes.DocumentHolder
import top.criwits.scaleda.verilog.psi.nodes.signal.SignalIdentifierPsiNode

class VerilogDocumentationProvider extends DocumentationProvider {
  override def generateDoc(element: PsiElement, originalElement: PsiElement): String = {
    println(element)
    element match {
      case n: DocumentHolder => n.getDocument // TODO: Nls?
      case _ => null
    }
  }
}
