package top.scaleda
package verilog.psi.nodes.assignments

import com.intellij.lang.ASTNode
import com.intellij.psi.util.PsiTreeUtil
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode
import top.scaleda.verilog.psi.nodes.expression.HierarchicalIdentifierPsiNode
import top.scaleda.verilog.psi.nodes.signal.SignalIdentifierPsiNode

import scala.jdk.CollectionConverters._

class NetAssignmentPsiNode(node: ASTNode) extends AbstractAssignmentPsiNode(node){
  override def getLValues: Iterable[SignalIdentifierPsiNode] = {
    val children = this.getChildren
    val LValue = children.head
    if (LValue == null || !LValue.isInstanceOf[ANTLRPsiNode]) return Iterable.empty

    val signals = PsiTreeUtil.findChildrenOfType(LValue, classOf[HierarchicalIdentifierPsiNode]).asScala
    signals.map(_.getReference.resolve).filter(_.isInstanceOf[SignalIdentifierPsiNode]).map(_.asInstanceOf[SignalIdentifierPsiNode])
    // TODO: rare case: LValue is a concatenation where parameters are used to indicate the width ...
  }
}
