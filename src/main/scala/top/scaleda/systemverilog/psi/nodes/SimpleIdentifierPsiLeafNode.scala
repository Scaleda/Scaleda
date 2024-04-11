package top.scaleda
package systemverilog.psi.nodes

import systemverilog.parser.SystemVerilogLexer
import systemverilog.psi.SystemVerilogPsiLeafNodeFactory

import org.antlr.intellij.adaptor.psi.ANTLRPsiLeafNode

class SimpleIdentifierPsiLeafNode(text: CharSequence)
    extends ANTLRPsiLeafNode(
      SystemVerilogPsiLeafNodeFactory.getTokenIElementType(SystemVerilogLexer.SIMPLE_IDENTIFIER),
      text
    ) {
  override def getName: String = getText
}
