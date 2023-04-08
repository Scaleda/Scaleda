package top.criwits.scaleda
package verilog.psi.nodes.expression.number

import verilog.parser.VerilogLexer

class BinaryNumberPsiLeafNode(text: CharSequence) extends NumberPsiLeafNode(text, VerilogLexer.Binary_number) {
  override def evaluate: Option[Int] = {
    val (_, _, number) = parseLiteral
    number
      .map(_.replace("_", ""))
      .filterNot(n => n.contains("x") || n.contains("X") || n.contains("z") || n.contains("Z"))
      .map(Integer.parseInt(_, 2)) // TODO: in case of Exception?
  }
}
