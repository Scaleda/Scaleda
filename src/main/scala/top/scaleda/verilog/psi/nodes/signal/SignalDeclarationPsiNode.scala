package top.scaleda
package verilog.psi.nodes.signal

import verilog.psi.nodes.TypedDeclaration

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode

import java.util
import scala.jdk.CollectionConverters._

/** PSI node for signal declaration.
  *
  * Signal includes nets (wire, tri, etc.), registers (reg, integer, etc.), parameters (including localparam and more) and ports
  * @param node The [[ASTNode]]
  */
abstract class SignalDeclarationPsiNode(node: ASTNode) extends ANTLRPsiNode(node) with TypedDeclaration {

  /** Get type text for a signal.
    *
    * For example, for `reg signed [7:0] foo [32:0]`, type text is `reg signed`
    * @return type string
    */
  override def getTypeText: String = {
    val typeChildren: Array[PsiElement] =
      util.Arrays.copyOf(this.getChildren, this.getChildren.length - 2).filterNot(_.isInstanceOf[RangePsiNode])
    typeChildren.map(_.getText).foldLeft("")(_ + " " + _).trim
  }

  def getRange: Option[RangePsiNode] = Option(PsiTreeUtil.getChildOfType(this, classOf[RangePsiNode]))

  def getIdentifier: Seq[SignalIdentifierPsiNode] = {
    val result = PsiTreeUtil.findChildrenOfType(this, classOf[SignalIdentifierPsiNode]).asScala
    result.toSeq
  }

}
