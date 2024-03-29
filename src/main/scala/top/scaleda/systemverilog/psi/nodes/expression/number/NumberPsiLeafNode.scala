package top.scaleda
package systemverilog.psi.nodes.expression.number

import systemverilog.psi.SystemVerilogPsiLeafNodeFactory
import systemverilog.psi.nodes.expression.IntEvaluable

import org.antlr.intellij.adaptor.psi.ANTLRPsiLeafNode

import java.util.regex.Pattern

abstract class NumberPsiLeafNode(text: CharSequence, tokenType: Int)
    extends ANTLRPsiLeafNode(SystemVerilogPsiLeafNodeFactory.getTokenIElementType(tokenType), text)
    with IntEvaluable {

  /** Parse number literal into 3 parts: width, base, number
    * @return
    */
  final def parseLiteral: (Option[String], Option[String], Option[String]) = {
    val originalText = getText
    if (originalText.contains('\'')) {
      val parts = originalText.split('\'')
      if (parts.length == 2) {
        val width          = if (parts(0).trim.isBlank) None else Some(parts(0).trim)
        val numberWithBase = parts(1).trim

        val pattern = Pattern.compile("([sS]?[dDbBoOhH])([0-9xXzZ_]+)")
        val matcher = pattern.matcher(numberWithBase)
        if (matcher.matches()) {
          val base   = matcher.group(1)
          val number = matcher.group(2)
          (width, Some(base), Some(number))
        } else {
          (width, None, None)
        }
      } else {
        // Can't be here...
        (None, None, None)
      }
    } else {
      // special case: must be decimal
      (None, None, Some(originalText))
    }

  }

}
