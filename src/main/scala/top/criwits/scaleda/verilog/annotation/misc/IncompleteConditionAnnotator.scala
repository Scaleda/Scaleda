package top.criwits.scaleda
package verilog.annotation.misc

import idea.ScaledaBundle
import verilog.psi.nodes.incompleted.IncompleteConditionPsiNode

import com.intellij.lang.annotation.{AnnotationHolder, Annotator, HighlightSeverity}
import com.intellij.psi.PsiElement

class IncompleteConditionAnnotator extends Annotator {
  override def annotate(element: PsiElement, holder: AnnotationHolder): Unit = {
    if (!element.isInstanceOf[IncompleteConditionPsiNode]) return
    holder
      .newAnnotation(HighlightSeverity.ERROR, ScaledaBundle.message("annotation.incomplete.condition"))
      .range(element.getTextRange)
      .create()
  }
}
