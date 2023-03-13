package top.criwits.scaleda
package verilog.psi.nodes.always

import verilog.psi.nodes.StructureViewNode

import com.intellij.lang.ASTNode
import com.intellij.psi.util.PsiTreeUtil
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode
import top.criwits.scaleda.verilog.psi.nodes.signal.SignalIdentifierPsiNode

import scala.jdk.CollectionConverters._

class AlwaysConstructPsiNode(node: ASTNode) extends ANTLRPsiNode(node) with StructureViewNode {
  def getDrivenSignals: Iterable[SignalIdentifierPsiNode] = {
    val assignments = PsiTreeUtil.findChildrenOfType(this, classOf[AssignmentPsiNode]).asScala
    assignments.map(_.getLValues).foldLeft(Iterable[SignalIdentifierPsiNode]())(_ ++ _).toSet
  }

  override def getElementName: String = {
    val delayOrEventControlPsiNode = PsiTreeUtil.findChildOfAnyType(this, classOf[DelayOrEventControlPsiNode])
    val eventText = if (delayOrEventControlPsiNode == null) "(unknown)" else {
      val txt = delayOrEventControlPsiNode.getText.replaceAll("posedge", "↑").replaceAll("negedge", "↓").replaceAll("\\s+", " ")
      txt
    }
    s"$eventText → " + getDrivenSignals.map(_.getName).mkString(", ")
  }
}
