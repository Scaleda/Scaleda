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
          .withFix(new RemoveUnresolvedAssignmentIntent(e))
          .highlightType(ProblemHighlightType.LIKE_UNKNOWN_SYMBOL)
          .create()
    }
  }
}

class RemoveUnresolvedAssignmentIntent(element: NamedParameterAssignmentPsiNode) extends BaseIntentionAction {
  override def getFamilyName: String = ScaledaBundle.message("annotation.unresolved.parameter.fix")

  override def getText: String = getFamilyName

  override def isAvailable(project: Project, editor: Editor, file: PsiFile): Boolean = true

  override def invoke(project: Project, editor: Editor, file: PsiFile): Unit = {
    val prev = PsiTreeUtil.prevVisibleLeaf(element)
    val next = PsiTreeUtil.nextVisibleLeaf(element)
    val prevHas = !(prev == null || !prev.textMatches(","))
    val nextHas = !(next == null || !next.textMatches(","))

    val doc = editor.getDocument

    if (!prevHas && !nextHas) {
      doc.replaceString(element.getTextRange.getStartOffset, element.getTextRange.getEndOffset, "")
    } else if (prevHas && nextHas) {
        doc.replaceString(prev.getTextRange.getStartOffset, element.getTextRange.getEndOffset, "") // remove previous comma
    } else if (prevHas) {
      doc.replaceString(prev.getTextRange.getStartOffset, element.getTextRange.getEndOffset, "")
    } else if (nextHas) {
        doc.replaceString(element.getTextRange.getStartOffset, next.getTextRange.getEndOffset, "")
    }
  }
}
