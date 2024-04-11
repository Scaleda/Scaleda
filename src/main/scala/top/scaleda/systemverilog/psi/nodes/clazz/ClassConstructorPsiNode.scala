package top.scaleda
package systemverilog.psi.nodes.clazz

import systemverilog.psi.nodes.StructureViewNode

import com.intellij.lang.ASTNode
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode

class ClassConstructorPsiNode(node: ASTNode) extends ANTLRPsiNode(node) with StructureViewNode {
  // override def getElementName: String =
  //   PsiTreeUtil
  //     .findChildrenOfType(this, classOf[IdentifierPsiNode])
  //     .asScala
  //     .headOption
  //     .map(_.getName)
  //     .getOrElse("(unknown constructor)")
  // override def getElementName: String = "new"
  override def getElementName: String = node.getText.split(";").head
}
