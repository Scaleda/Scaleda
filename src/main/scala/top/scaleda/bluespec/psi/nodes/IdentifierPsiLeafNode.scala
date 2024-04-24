package top.scaleda
package bluespec.psi.nodes

import bluespec.psi
import bluespec.parser.BluespecLexer

import org.antlr.intellij.adaptor.psi.ANTLRPsiLeafNode

class IdentifierPsiLeafNode(text: CharSequence)
    extends ANTLRPsiLeafNode(
      psi.BluespecPsiLeafNodeFactory.getTokenIElementType(BluespecLexer.Identifier),
      text
    ) {
  override def getName: String = {
    this.getText
  }
}
