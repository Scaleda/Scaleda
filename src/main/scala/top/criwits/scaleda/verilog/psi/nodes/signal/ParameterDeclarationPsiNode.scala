package top.criwits.scaleda
package verilog.psi.nodes.signal

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement

import java.util

class ParameterDeclarationPsiNode(node: ASTNode) extends SignalDeclarationPsiNode(node) {
  override def getTypeText: String = {
    val typeChildren: Array[PsiElement] =
      util.Arrays.copyOf(this.getChildren, this.getChildren.length - 1) // I don't have range; I don't have semicolon!
    typeChildren.map(_.getText).foldLeft("")(_ + " " + _).trim
  }
}
