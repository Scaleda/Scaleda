package top.criwits.scaleda
package verilog.annotation

import idea.ScaledaBundle
import verilog.psi.nodes.always.AlwaysConstructPsiNode
import verilog.psi.nodes.block.{CaseBodyPsiNode, CaseDefaultPsiNode}

import com.intellij.lang.annotation.{AnnotationHolder, Annotator, HighlightSeverity}
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil

class CaseStatementLatchAnnotator extends Annotator {
  override def annotate(element: PsiElement, holder: AnnotationHolder): Unit = {
    if (!element.isInstanceOf[CaseBodyPsiNode]) return
    val always = PsiTreeUtil.getParentOfType(element, classOf[AlwaysConstructPsiNode])
    if (always == null || !always.isCombinationLogic) return
    if (PsiTreeUtil.findChildOfAnyType(always, classOf[CaseDefaultPsiNode]) == null) {
      holder
        .newAnnotation(
          HighlightSeverity.WARNING,
          ScaledaBundle.message("annotation.latch.case")
        )
        .create()
    }
  }
}
