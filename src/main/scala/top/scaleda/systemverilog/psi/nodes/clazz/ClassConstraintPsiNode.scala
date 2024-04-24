package top.scaleda
package systemverilog.psi.nodes.clazz

import systemverilog.parser.SystemVerilogLexer
import systemverilog.psi.SystemVerilogPsiLeafNodeFactory
import systemverilog.psi.nodes.{IdentifierPsiNode, StructureViewNode}

import com.intellij.lang.ASTNode
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.{PsiElement, PsiNameIdentifierOwner}
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode

import scala.jdk.CollectionConverters.CollectionHasAsScala

class ClassConstraintPsiNode(node: ASTNode)
    extends ANTLRPsiNode(node)
    with StructureViewNode
    with PsiNameIdentifierOwner {
  override def getElementName: String = getNameIdentifier.getText

  // override def getName: String = PsiTreeUtil
  //   .findChildrenOfType(this, classOf[IdentifierPsiNode])
  //   .asScala
  //   .map(_.getText)
  //   .headOption
  //   .getOrElse("(unknown constraint)")

  override def getNameIdentifier: PsiElement = PsiTreeUtil
    .findChildOfType(this, classOf[IdentifierPsiNode])

  override def setName(name: String): PsiElement = {
    val child = getNameIdentifier
    if (child == null) null
    else {
      child.replace(SystemVerilogPsiLeafNodeFactory.create(SystemVerilogLexer.SIMPLE_IDENTIFIER, name))
    }
  }
}
