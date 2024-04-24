package top.scaleda
package systemverilog.psi.nodes

import systemverilog.parser.SystemVerilogLexer

import org.antlr.intellij.adaptor.psi.ANTLRPsiLeafNode
import top.scaleda.systemverilog.psi.SystemVerilogPsiLeafNodeFactory

class IdentifierPsiLeafNode(text: CharSequence)
    extends ANTLRPsiLeafNode(
      SystemVerilogPsiLeafNodeFactory.getTokenIElementType(SystemVerilogLexer.IDENTIFIER),
      text
    ) {
  override def getName: String = {
    this.getText
  }
}
