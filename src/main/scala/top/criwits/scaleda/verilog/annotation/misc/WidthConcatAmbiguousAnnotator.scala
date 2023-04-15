package top.criwits.scaleda
package verilog.annotation.misc

import idea.ScaledaBundle
import verilog.psi.nodes.expression.{ConcatPsiNode, ExpressionPsiNode, TermPsiNode}

import com.intellij.lang.annotation.{AnnotationHolder, Annotator, HighlightSeverity}
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil

import scala.jdk.CollectionConverters.CollectionHasAsScala

/** Example: {1'b1, (a + b)} is ambiguous concat, but {1'b1, a} is ok.
  */
class WidthConcatAmbiguousAnnotator extends Annotator {
  override def annotate(element: PsiElement, holder: AnnotationHolder): Unit = {
    if (!element.isInstanceOf[ConcatPsiNode]) return
    val expressions = PsiTreeUtil.findChildrenOfType(element, classOf[ExpressionPsiNode]).asScala
    if (
      expressions.exists(exp => {
        // 1. children more than one term
        // 2. signal count >= 2
        PsiTreeUtil.findChildrenOfType(exp, classOf[TermPsiNode]).asScala.size > 1 && exp.getUsedSignals.size >= 2
      })
    ) {
      holder
        .newAnnotation(
          HighlightSeverity.WARNING,
          ScaledaBundle.message("annotation.ambiguous.concat")
        )
        .create()
    }
  }
}
