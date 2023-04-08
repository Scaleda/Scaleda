package top.criwits.scaleda
package verilog.psi.nodes.signal

import verilog.parser.VerilogLexer
import verilog.psi.VerilogPsiLeafNodeFactory
import verilog.psi.nodes.{DocumentHolder, StructureViewNode}

import com.intellij.lang.ASTNode
import com.intellij.lang.documentation.DocumentationMarkup._
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.{PsiElement, PsiNameIdentifierOwner}
import com.intellij.util.IncorrectOperationException
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode

abstract class SignalIdentifierPsiNode(node: ASTNode)
    extends ANTLRPsiNode(node)
    with PsiNameIdentifierOwner
      with DocumentHolder
    with StructureViewNode {
  override def getName: String = {
    if (getNameIdentifier == null) return null
    getNameIdentifier.getText
  }

  def getDeclaration: SignalDeclarationPsiNode = PsiTreeUtil.getParentOfType(this, classOf[SignalDeclarationPsiNode])

  def getTypeText = getDeclaration.getTypeText
  def getRange = getDeclaration.getRange
  def getDimension: Option[DimensionPsiNode] = {
    val variableType = PsiTreeUtil.getParentOfType(this, classOf[VariableTypePsiNode])
    Option(PsiTreeUtil.getChildOfType(variableType, classOf[DimensionPsiNode]))
  }

  override def getDocument: String = {
    def getFullDefinition = s"${getTypeText} ${getRange.map(_.toString + " ").getOrElse("")}${getName}${getDimension.map(" " + _.toString).getOrElse("")}"
    s"${DEFINITION_START}${getFullDefinition}${DEFINITION_END}"

  }


  override def getNameIdentifier: PsiElement = getFirstChild.getFirstChild // TODO: really?

  @throws[IncorrectOperationException]
  override def setName(s: String): PsiElement =
    getFirstChild.replace(VerilogPsiLeafNodeFactory.create(VerilogLexer.Simple_identifier, s))

  override def getElementName: String = if (getName == null) "(unknown)"
  else {
    val parent = getDeclaration
    if (parent != null) getName + ": " + parent.getTypeText
    else getName + ": (unknown type)"
  }
}
