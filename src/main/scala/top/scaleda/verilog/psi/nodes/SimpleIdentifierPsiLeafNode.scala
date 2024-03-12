package top.scaleda
package verilog.psi.nodes

import org.antlr.intellij.adaptor.psi.ANTLRPsiLeafNode
import top.scaleda.verilog.parser.VerilogLexer
import top.scaleda.verilog.psi.VerilogPsiLeafNodeFactory

class SimpleIdentifierPsiLeafNode(text: CharSequence)
    extends ANTLRPsiLeafNode(VerilogPsiLeafNodeFactory.getTokenIElementType(VerilogLexer.Simple_identifier), text) {
  override def getName: String = getText
}
