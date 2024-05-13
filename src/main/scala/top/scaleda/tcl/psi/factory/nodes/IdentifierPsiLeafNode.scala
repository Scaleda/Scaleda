package top.scaleda
package tcl.psi.factory.nodes

import tcl.parser.TclLexer

import org.antlr.intellij.adaptor.psi.ANTLRPsiLeafNode

class IdentifierPsiLeafNode(text: CharSequence)
    extends ANTLRPsiLeafNode(
      TclPsiLeafNodeFactory.getTokenIElementType(TclLexer.Identifier),
      text
    ) {
  override def getName: String = {
    this.getText
  }
}
