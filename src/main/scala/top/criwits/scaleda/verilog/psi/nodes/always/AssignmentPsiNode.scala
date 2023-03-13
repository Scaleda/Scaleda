package top.criwits.scaleda
package verilog.psi.nodes.always

import com.intellij.lang.ASTNode
import com.intellij.psi.util.PsiTreeUtil
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode
import top.criwits.scaleda.verilog.psi.nodes.expression.HierarchicalIdentifierPsiNode
import top.criwits.scaleda.verilog.psi.nodes.signal.SignalIdentifierPsiNode

import scala.jdk.CollectionConverters._

abstract class AssignmentPsiNode(node: ASTNode) extends ANTLRPsiNode(node) {
  def getLValues: Iterable[SignalIdentifierPsiNode] = {
    val children = this.getChildren
    val LValue = children.head
    if (LValue == null || !LValue.isInstanceOf[ANTLRPsiNode]) return Iterable.empty

    val signals = PsiTreeUtil.findChildrenOfType(LValue, classOf[HierarchicalIdentifierPsiNode]).asScala
    signals.map(_.getReference.resolve).filter(_.isInstanceOf[SignalIdentifierPsiNode]).map(_.asInstanceOf[SignalIdentifierPsiNode])
  }
}

class NonblockingAssignmentPsiNode(node: ASTNode) extends AssignmentPsiNode(node)
class BlockingAssignmentPsiNode(node: ASTNode) extends AssignmentPsiNode(node)
