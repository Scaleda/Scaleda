package top.criwits.scaleda
package verilog.psi.nodes.signal

import verilog.psi.nodes.{IdentifierPsiNode, TypedDeclaration}

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode

import java.util

/** PSI node for signal declaration.
 *
 * Signal includes nets (wire, tri, etc.), registers (reg, integer, etc.), parameters (including localparam and more) and ports
 * @param node The [[ASTNode]]
 */
abstract class SignalDeclarationPsiNode(node: ASTNode) extends ANTLRPsiNode(node) with TypedDeclaration{
  /**
   * Get type text for a signal.
   *
   * For example, for `reg signed [7:0] foo [32:0]`, type text is `reg signed [7:0] [32:0]`
   * @return type string
   */
  override def getTypeText: String = {
//    this.getChildren.filter(!_.isInstanceOf[IdentifierPsiNode])
//      .map(_.getText).reduce(_ + " " + _).replace(";", "")
    val typeChildren: Array[PsiElement] = util.Arrays.copyOf(this.getChildren, this.getChildren.length - 2)
    typeChildren.map(_.getText).reduce(_ + " " + _) // TODO: array error
  }
}
