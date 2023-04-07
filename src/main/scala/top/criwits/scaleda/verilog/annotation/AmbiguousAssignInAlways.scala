package top.criwits.scaleda
package verilog.annotation

import idea.ScaledaBundle
import verilog.psi.nodes.always.{AlwaysConstructPsiNode, NonblockingAssignmentPsiNode}

import com.intellij.lang.annotation.{AnnotationHolder, Annotator, HighlightSeverity}
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil

/** Example:
  * <code>
  *   always @(a, b, c) begin
  *     d <= f;
  *   end
  * </code>
  */
class AmbiguousAssignInAlways extends Annotator {
  override def annotate(element: PsiElement, holder: AnnotationHolder): Unit = {
    if (!element.isInstanceOf[NonblockingAssignmentPsiNode]) return
    val always = PsiTreeUtil.getParentOfType(element, classOf[AlwaysConstructPsiNode])
    if (always == null) return
    if (always.isCombinationLogic) {
      holder
        .newAnnotation(
          HighlightSeverity.WARNING,
          ScaledaBundle.message("annotation.ambiguous.assign.combination")
        )
        .create()
    }
  }
}
