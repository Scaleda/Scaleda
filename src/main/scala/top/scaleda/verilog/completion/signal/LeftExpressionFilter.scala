package top.scaleda
package verilog.completion.signal

import com.intellij.psi.PsiElement
import com.intellij.psi.filters.ElementFilter
import org.antlr.intellij.adaptor.lexer.PSIElementTypeFactory
import top.scaleda.verilog.VerilogLanguage
import top.scaleda.verilog.parser.VerilogLexer
import top.scaleda.verilog.psi.nodes.StatementPsiNode

class LeftExpressionFilter extends ElementFilter {
  override def isAcceptable(element: Any, context: PsiElement): Boolean = {
    try {
      val p =context.getParent.getParent
      return p match {
        case _: StatementPsiNode => true
        case _ => false
      }
    } catch {
      case _:Throwable => return false
    }

    false
  }

  override def isClassAcceptable(hintClass: Class[_]): Boolean = true
}
