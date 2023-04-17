package top.criwits.scaleda
package verilog.annotation.misc

import idea.ScaledaBundle
import verilog.psi.nodes.expression.HierarchicalIdentifierPsiNode

import com.intellij.lang.annotation.{AnnotationHolder, Annotator, HighlightSeverity}
import com.intellij.psi.PsiElement

class HierarchicalReferenceAnnotator extends Annotator {
  override def annotate(element: PsiElement, holder: AnnotationHolder): Unit = {
    if (!element.isInstanceOf[HierarchicalIdentifierPsiNode]) return
    val hierarchical = element.asInstanceOf[HierarchicalIdentifierPsiNode]

    val result = hierarchical.getReference.resolve()
    if (result == null) {
      holder.newAnnotation(
        HighlightSeverity.ERROR,
        ScaledaBundle.message("annotation.unresolved.signal", hierarchical.getName)
      ).create()
    }
  }
}
