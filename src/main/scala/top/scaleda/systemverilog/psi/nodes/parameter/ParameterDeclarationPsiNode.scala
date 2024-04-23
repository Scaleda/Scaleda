package top.scaleda
package systemverilog.psi.nodes.parameter

import systemverilog.psi.nodes.signal.SignalDeclarationPsiNode

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import top.scaleda.systemverilog.psi.nodes.StructureViewNode

import java.util

class ParameterDeclarationPsiNode(node: ASTNode) extends SignalDeclarationPsiNode(node) with StructureViewNode {
  override def getTypeText: String = {
    // val typeChildren: Array[PsiElement] =
    //   util.Arrays.copyOf(this.getChildren, this.getChildren.length - 1) // I don't have range; I don't have semicolon!
    // typeChildren.map(_.getText).foldLeft("")(_ + " " + _).trim
    node.getText.split(';').headOption.getOrElse("unknown")
  }

  def getListOfParamAssignments: Option[ListOfParamAssignmentsPsiNode] = {
    Option(PsiTreeUtil.getChildOfType(this, classOf[ListOfParamAssignmentsPsiNode]))
  }

  override def getElementName: String = getTypeText
}
