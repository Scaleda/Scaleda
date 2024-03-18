package top.scaleda
package systemverilog.psi.nodes.always

import systemverilog.psi.nodes.StructureViewNode
import systemverilog.psi.nodes.assignments.AlwaysAssignmentPsiNode
import systemverilog.psi.nodes.expression.HierarchicalIdentifierPsiNode
import systemverilog.psi.nodes.signal.SignalIdentifierPsiNode

import com.intellij.lang.ASTNode
import com.intellij.psi.util.PsiTreeUtil
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode

import scala.jdk.CollectionConverters._

class AlwaysConstructPsiNode(node: ASTNode) extends ANTLRPsiNode(node) with StructureViewNode {
  def getAssignments = PsiTreeUtil.findChildrenOfType(this, classOf[AlwaysAssignmentPsiNode]).asScala

  def getDrivenSignals: Iterable[SignalIdentifierPsiNode] = {
    val assignments = getAssignments
    assignments.map(_.getLValues).foldLeft(Iterable[SignalIdentifierPsiNode]())(_ ++ _).toSet
  }

  def getUsedSignals: Iterable[SignalIdentifierPsiNode] = {
    val original = PsiTreeUtil.findChildrenOfType(this, classOf[HierarchicalIdentifierPsiNode]).asScala
    original
      .filter(f => {
        PsiTreeUtil.getParentOfType(f, classOf[DelayOrEventControlPsiNode]) == null
      })
      .map(_.getReference.resolve)
      .filter(_.isInstanceOf[SignalIdentifierPsiNode])
      .map(_.asInstanceOf[SignalIdentifierPsiNode])
      .filter(f => !getDrivenSignals.exists(_ == f))
  }

  def getControl: DelayOrEventControlPsiNode = PsiTreeUtil.findChildOfAnyType(this, classOf[DelayOrEventControlPsiNode])

  override def getElementName: String = {
    val delayOrEventControlPsiNode = getControl
    val eventText =
      if (delayOrEventControlPsiNode == null) ""
      else {
        if (delayOrEventControlPsiNode.isEventControl) {
          val events = delayOrEventControlPsiNode.getEvents
          events
            .map(event =>
              (event.getType match {
                case EventExpressionPsiNode.POSEDGE => "↑"
                case EventExpressionPsiNode.NEGEDGE => "↓"
                case EventExpressionPsiNode.LEVEL   => "⎍"
              }) + " " + (if (event.getExpression != null) event.getExpression.getText else "")
            )
            .mkString(", ")
        } else {
          s"#${delayOrEventControlPsiNode.getDelayValue.getValue}"
        }
      }
    s"($eventText) → " + getDrivenSignals.map(_.getName).mkString(", ")
  }

  /**
    * Will judge simply by posedge/negedge (events) or delay (delay)
    */
  def isCombinationLogic: Boolean = {
    val delayOrEventControlPsiNode = getControl
    // default to time logic, example: `always #1`
    if (delayOrEventControlPsiNode == null) return false
    if (delayOrEventControlPsiNode.isEventControl) {
      val events = delayOrEventControlPsiNode.getEvents
      events.forall(event => event.getType == EventExpressionPsiNode.LEVEL)
    } else {
      false
    }
  }
}
