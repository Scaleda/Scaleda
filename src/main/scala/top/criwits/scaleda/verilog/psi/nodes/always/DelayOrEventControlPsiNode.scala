package top.criwits.scaleda
package verilog.psi.nodes.always

import com.intellij.lang.ASTNode
import com.intellij.psi.util.PsiTreeUtil
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode

class DelayOrEventControlPsiNode(node: ASTNode) extends ANTLRPsiNode(node) {
  def isEventControl: Boolean = {
    val delayValue = PsiTreeUtil.findChildrenOfType(this, classOf[DelayValuePsiNode])
    if (delayValue.isEmpty) true
    else false
  }

  def getDelayValue: DelayValuePsiNode = {
    PsiTreeUtil.findChildOfType(this, classOf[DelayValuePsiNode])
  }

  def getEvents: Iterable[EventPrimaryPsiNode] = {
    import scala.jdk.CollectionConverters._
    PsiTreeUtil.findChildrenOfType(this, classOf[EventPrimaryPsiNode]).asScala
  }
}
