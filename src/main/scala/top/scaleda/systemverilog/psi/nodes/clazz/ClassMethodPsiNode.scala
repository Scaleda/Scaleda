package top.scaleda
package systemverilog.psi.nodes.clazz

import systemverilog.psi.nodes.StructureViewNode

import com.intellij.lang.ASTNode
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode

class ClassMethodPsiNode(node: ASTNode) extends ANTLRPsiNode(node) with StructureViewNode {
  // override def getElementName: String =
  //   PsiTreeUtil
  //     .findChildrenOfType(this, classOf[IdentifierPsiNode])
  //     .asScala
  //     .headOption
  //     .map(_.getName)
  //     .getOrElse("(unknown function)")
  override def getElementName: String = node.getText.split(";").head
}
