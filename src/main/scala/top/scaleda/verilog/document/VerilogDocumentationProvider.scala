package top.scaleda
package verilog.document

import verilog.psi.nodes.DocumentHolder

import com.intellij.lang.documentation.AbstractDocumentationProvider
import com.intellij.psi.PsiElement

class VerilogDocumentationProvider extends AbstractDocumentationProvider {
  override def generateDoc(element: PsiElement, originalElement: PsiElement): String = {
    println(element)
    element match {
      case n: DocumentHolder => n.getDocument // TODO: Nls?
      case _                 => null
    }
  }
}
