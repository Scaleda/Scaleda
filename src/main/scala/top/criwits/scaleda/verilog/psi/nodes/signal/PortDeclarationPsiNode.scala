package top.criwits.scaleda
package verilog.psi.nodes.signal

import verilog.psi.nodes.signal.PortDeclarationPsiNode.{INOUT, INPUT, OUTPUT, OUTPUT_REG}

import com.intellij.lang.ASTNode
import com.intellij.psi.util.PsiTreeUtil

class PortDeclarationPsiNode(node: ASTNode) extends SignalDeclarationPsiNode(node) {
  def getDeclaration: Option[AbstractDeclarationPsiNode] = {
    Option(PsiTreeUtil.getChildOfType(this, classOf[AbstractDeclarationPsiNode]))
  }

  def getPortType: PortDeclarationPsiNode.PortType = {
    val typeText = getTypeText
    if (typeText.contains("input")) INPUT
    else if (typeText.contains("inout")) INOUT
    else if (typeText.contains("output")) OUTPUT
    else if (typeText.contains("output") && (typeText.contains("reg") || typeText.contains("integer") || typeText.contains("time"))) OUTPUT_REG
    else INPUT // default
  }

  override def getTypeText: String = {
    val declaration = PsiTreeUtil.getChildOfType(this, classOf[AbstractDeclarationPsiNode])
    if (declaration == null) "unknown" else declaration.getTypeText
  }

  override def getRange: Option[RangePsiNode] = getDeclaration.flatMap(d => Option(PsiTreeUtil.getChildOfType(d, classOf[RangePsiNode])))
}

object PortDeclarationPsiNode extends Enumeration {
  type PortType = Value
  val INPUT, INOUT, OUTPUT, OUTPUT_REG = Value
}
