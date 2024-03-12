package top.scaleda
package verilog.annotation.instantiation

import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.lang.annotation.{AnnotationHolder, Annotator, HighlightSeverity}
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import top.scaleda.idea.ScaledaBundle
import top.scaleda.verilog.psi.nodes.expression.ExpressionPsiNode
import top.scaleda.verilog.psi.nodes.instantiation.NamedPortConnectionPsiNode

class EmptyNamedPortConnectionAnnotator extends Annotator {
  override def annotate(element: PsiElement, holder: AnnotationHolder): Unit = {
    // check if is NamedPortConnectionPsiNode
    if (!element.isInstanceOf[NamedPortConnectionPsiNode]) return
    val conn = element.asInstanceOf[NamedPortConnectionPsiNode]

    val expression = PsiTreeUtil.getChildOfType(conn, classOf[ExpressionPsiNode])
    if (expression == null) {
      holder.newAnnotation(HighlightSeverity.WARNING, ScaledaBundle.message("annotation.empty.port.connection"))
        .highlightType(ProblemHighlightType.WARNING)
        .create()
    }
  }
}
