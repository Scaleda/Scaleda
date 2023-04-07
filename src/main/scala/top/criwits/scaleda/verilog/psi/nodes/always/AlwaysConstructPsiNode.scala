package top.criwits.scaleda
package verilog.psi.nodes.always

import verilog.psi.nodes.StructureViewNode
import verilog.psi.nodes.expression.HierarchicalIdentifierPsiNode
import verilog.psi.nodes.signal.SignalIdentifierPsiNode

import com.intellij.lang.ASTNode
import com.intellij.psi.util.PsiTreeUtil
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode

import scala.jdk.CollectionConverters._

class AlwaysConstructPsiNode(node: ASTNode) extends ANTLRPsiNode(node) with StructureViewNode {
  def getAssignments = PsiTreeUtil.findChildrenOfType(this, classOf[AssignmentPsiNode]).asScala

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
                case EventPrimaryPsiNode.POSEDGE => "↑"
                case EventPrimaryPsiNode.NEGEDGE => "↓"
                case EventPrimaryPsiNode.LEVEL   => "⎍"
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
    if (delayOrEventControlPsiNode == null) return true
    if (delayOrEventControlPsiNode.isEventControl) {
      val events = delayOrEventControlPsiNode.getEvents
      events.forall(event => event.getType == EventPrimaryPsiNode.LEVEL)
    } else {
      true
    }
  }
}
