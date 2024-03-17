package top.scaleda
package systemverilog.psi.factory.nodes

import systemverilog.parser.SystemVerilogLexer

import org.antlr.intellij.adaptor.psi.ANTLRPsiLeafNode

class IdentifierPsiLeafNode(text: CharSequence)
    extends ANTLRPsiLeafNode(
      SystemVerilogPsiLeafNodeFactory.getTokenIElementType(SystemVerilogLexer.IDENTIFIER),
      text
    ) {
  override def getName: String = {
    this.getText
  }
}
