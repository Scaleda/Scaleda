package top.scaleda
package systemverilog.psi.nodes.expression.number

import systemverilog.parser.SystemVerilogLexer

class BinaryNumberPsiLeafNode(text: CharSequence)
    extends NumberPsiLeafNode(text, SystemVerilogLexer.BINARY_NUMBER_MODE) {
  override def evaluate: Option[Int] = {
    val (_, _, number) = parseLiteral
    number
      .map(_.replace("_", ""))
      .filterNot(n => n.contains("x") || n.contains("X") || n.contains("z") || n.contains("Z"))
      .map(Integer.parseInt(_, 2)) // TODO: in case of Exception?
  }
}
