package top.scaleda
package bluespec.psi.nodes

import com.intellij.lang.ASTNode
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode

class StructureBeforeSemicolonTextViewNode(node: ASTNode) extends ANTLRPsiNode(node) with StructureViewNode {

  override def getElementName: String = node.getText.split(";").head.replace("\n", "")
}
