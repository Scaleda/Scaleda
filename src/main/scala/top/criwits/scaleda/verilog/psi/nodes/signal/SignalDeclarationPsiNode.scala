package top.criwits.scaleda
package verilog.psi.nodes.signal

import verilog.psi.nodes.TypedDeclaration

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode

import java.util

abstract class SignalDeclarationPsiNode(node: ASTNode) extends ANTLRPsiNode(node) with TypedDeclaration{
  override def getTypeText: String = {
    val typeChildren: Array[PsiElement] = util.Arrays.copyOf(this.getChildren, this.getChildren.length - 2)
    typeChildren.map(_.getText).reduce(_ + " " + _)
  }
}
