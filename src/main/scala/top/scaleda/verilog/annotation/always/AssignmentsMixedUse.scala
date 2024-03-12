package top.scaleda
package verilog.annotation.always

import idea.ScaledaBundle
import verilog.psi.nodes.always.AlwaysConstructPsiNode
import verilog.psi.nodes.assignments.{BlockingAssignmentPsiNode, NonblockingAssignmentPsiNode}

import com.intellij.lang.annotation.{AnnotationHolder, Annotator, HighlightSeverity}
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil

/** Example:
  * <code>
  *   always @(posedge clk) begin
  *     d <= f;
  *     g = h;
  *   end
  * </code>
  */
class AssignmentsMixedUse extends Annotator {
  override def annotate(element: PsiElement, holder: AnnotationHolder): Unit = {
    if (!element.isInstanceOf[NonblockingAssignmentPsiNode] && !element.isInstanceOf[BlockingAssignmentPsiNode]) return
    val always = PsiTreeUtil.getParentOfType(element, classOf[AlwaysConstructPsiNode])
    if (always == null) return
    val targetClazz = element match {
      case e: NonblockingAssignmentPsiNode => classOf[BlockingAssignmentPsiNode]
      case _                               => classOf[NonblockingAssignmentPsiNode]
    }
    if (PsiTreeUtil.findChildOfAnyType(always, targetClazz) != null)
      holder
        .newAnnotation(
          HighlightSeverity.WARNING,
          ScaledaBundle.message("annotation.ambiguous.assign.mixed")
        )
        .create()
  }
}
