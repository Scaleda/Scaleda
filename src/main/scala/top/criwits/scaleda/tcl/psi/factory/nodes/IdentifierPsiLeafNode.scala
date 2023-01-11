package top.criwits.scaleda
package tcl.psi.factory.nodes

import tcl.parser.TclLexer
import verilog.psi.factory.VerilogPsiLeafNodeFactory

import org.antlr.intellij.adaptor.psi.ANTLRPsiLeafNode

class IdentifierPsiLeafNode(text: CharSequence)
    extends ANTLRPsiLeafNode(
      VerilogPsiLeafNodeFactory.getTokenIElementType(TclLexer.IDENTIFICADOR),
      text
    ) {
  override def getName: String = {
    this.getText
  }
}
