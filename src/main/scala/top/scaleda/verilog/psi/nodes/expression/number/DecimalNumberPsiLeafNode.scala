package top.scaleda
package verilog.psi.nodes.expression.number

import verilog.parser.VerilogLexer

class DecimalNumberPsiLeafNode(text: CharSequence) extends NumberPsiLeafNode(text, VerilogLexer.Decimal_number) {
  override def evaluate: Option[Int] = {
    val (_, _, number) = parseLiteral
    number
      .map(_.replace("_", ""))
      .filterNot(n => n.contains("x") || n.contains("X") || n.contains("z") || n.contains("Z"))
      .map(_.toInt) // TODO: in case of Exception?
  }
}
