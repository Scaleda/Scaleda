package top.criwits.scaleda
package verilog.annotation.always

import idea.ScaledaBundle
import verilog.psi.nodes.always.AlwaysConstructPsiNode
import verilog.psi.nodes.condition.{ConditionalStatementHeadPsiNode, ConditionalStatementPsiNode, ConditionalStatementTailPsiNode}

import com.intellij.lang.annotation.{AnnotationHolder, Annotator, HighlightSeverity}
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil

class ConditionalStatementLatchAnnotator extends Annotator {
  override def annotate(element: PsiElement, holder: AnnotationHolder): Unit = {
    if (!element.isInstanceOf[ConditionalStatementHeadPsiNode]) return
    val always = PsiTreeUtil.getParentOfType(element, classOf[AlwaysConstructPsiNode])
    if (always == null || !always.isCombinationLogic) return
    val block = element.getParent
    if (!block.isInstanceOf[ConditionalStatementPsiNode]) return
    val cond = block.asInstanceOf[ConditionalStatementPsiNode]
    if (!cond.getChildren.exists(_.isInstanceOf[ConditionalStatementTailPsiNode])) {
      holder
        .newAnnotation(
          HighlightSeverity.WARNING,
          ScaledaBundle.message("annotation.latch.conditional")
        )
        .create()
    }
  }
}
