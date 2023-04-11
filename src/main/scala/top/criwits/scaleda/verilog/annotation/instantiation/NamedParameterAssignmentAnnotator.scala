package top.criwits.scaleda
package verilog.annotation.instantiation

import com.intellij.codeInsight.intention.impl.BaseIntentionAction
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.lang.annotation.{AnnotationHolder, Annotator, HighlightSeverity}
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.{PsiElement, PsiFile}
import top.criwits.scaleda.idea.ScaledaBundle
import top.criwits.scaleda.verilog.psi.nodes.instantiation.NamedParameterAssignmentPsiNode

class NamedParameterAssignmentAnnotator extends Annotator {
  override def annotate(element: PsiElement, holder: AnnotationHolder): Unit = {
    if (!element.isInstanceOf[NamedParameterAssignmentPsiNode]) return
    val e = element.asInstanceOf[NamedParameterAssignmentPsiNode]

    if (e.getReference.resolve() == null)  {
        holder.newAnnotation(HighlightSeverity.ERROR, ScaledaBundle.message("annotation.unresolved.parameter"))
          .range(e.getTextRange)
          .withFix(new RemoveItemWithCommasIntent[NamedParameterAssignmentPsiNode](e, ScaledaBundle.message("annotation.unresolved.parameter.fix")))
          .highlightType(ProblemHighlightType.LIKE_UNKNOWN_SYMBOL)
          .create()
    }
  }
}

