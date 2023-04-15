package top.criwits.scaleda
package verilog.annotation.assign

import idea.ScaledaBundle
import verilog.psi.nodes.always.{AlwaysConstructPsiNode, DelayOrEventControlPsiNode}
import verilog.psi.nodes.expression.BinaryOperatorOrPsiNode

import com.intellij.lang.annotation.{AnnotationHolder, Annotator, HighlightSeverity}
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil

/**
  * Example: <code>
  *   always @(a || b) begin ... end
  *   </code>
  */
class OrOperatorInSensitiveList extends Annotator {
  override def annotate(element: PsiElement, holder: AnnotationHolder): Unit = {
    if (!element.isInstanceOf[DelayOrEventControlPsiNode]) return
    val always = PsiTreeUtil.getParentOfType(element, classOf[AlwaysConstructPsiNode])
    if (always == null || !always.isCombinationLogic) return
    if (!element.asInstanceOf[DelayOrEventControlPsiNode].isEventControl) return
    if (
      !element
        .asInstanceOf[DelayOrEventControlPsiNode]
        .getEvents
        .forall(e => PsiTreeUtil.findChildOfType(e, classOf[BinaryOperatorOrPsiNode]) == null)
    )
      holder
        .newAnnotation(HighlightSeverity.WARNING, ScaledaBundle.message("annotation.ambiguous.or"))
        .range(element.getTextRange)
        .create()
  }
}
