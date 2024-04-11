package top.scaleda
package systemverilog.psi.nodes.assignments

import systemverilog.psi.nodes.expression.HierarchicalIdentifierPsiNode
import systemverilog.psi.nodes.signal.SignalIdentifierPsiNode

import com.intellij.lang.ASTNode
import com.intellij.psi.util.PsiTreeUtil
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode

import scala.jdk.CollectionConverters._

abstract class AlwaysAssignmentPsiNode(node: ASTNode) extends AbstractAssignmentPsiNode(node) {
  override def getLValues: Iterable[SignalIdentifierPsiNode] = {
    val children = this.getChildren
    val LValue = children.head
    if (LValue == null || !LValue.isInstanceOf[ANTLRPsiNode]) return Iterable.empty

    val signals = PsiTreeUtil.findChildrenOfType(LValue, classOf[HierarchicalIdentifierPsiNode]).asScala
    signals.map(_.getReference.resolve).filter(_.isInstanceOf[SignalIdentifierPsiNode]).map(_.asInstanceOf[SignalIdentifierPsiNode])
    // TODO: rare case: LValue is a concatenation where parameters are used to indicate the width ...

  }
}

class NonblockingAssignmentPsiNode(node: ASTNode) extends AlwaysAssignmentPsiNode(node)
class BlockingAssignmentPsiNode(node: ASTNode) extends AlwaysAssignmentPsiNode(node)
