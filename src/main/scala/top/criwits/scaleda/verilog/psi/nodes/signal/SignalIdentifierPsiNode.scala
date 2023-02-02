package top.criwits.scaleda
package verilog.psi.nodes.signal

import verilog.parser.VerilogLexer
import verilog.psi.VerilogPsiLeafNodeFactory
import verilog.psi.nodes.StructureViewNode

import com.intellij.lang.ASTNode
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.{PsiElement, PsiNameIdentifierOwner}
import com.intellij.util.IncorrectOperationException
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode

abstract class SignalIdentifierPsiNode(node: ASTNode)
    extends ANTLRPsiNode(node)
    with PsiNameIdentifierOwner
    with StructureViewNode {
  override def getName: String = {
    if (getNameIdentifier == null) return null
    getNameIdentifier.getText
  }

  override def getNameIdentifier: PsiElement = getFirstChild.getFirstChild // TODO: really?

  @throws[IncorrectOperationException]
  override def setName(s: String): PsiElement =
    getFirstChild.replace(VerilogPsiLeafNodeFactory.create(VerilogLexer.Simple_identifier, s))

  override def getElementName: String = if (getName == null) "(unknown)"
  else {
    val parent = PsiTreeUtil.getParentOfType(this, classOf[SignalDeclarationPsiNode])
    if (parent != null) getName + ": " + parent.getTypeText
    else getName + ": (unknown type)"
  }
}
