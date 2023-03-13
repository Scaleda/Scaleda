package top.criwits.scaleda
package verilog.annotation

import com.intellij.lang.annotation.{AnnotationHolder, Annotator, HighlightSeverity}
import com.intellij.psi.PsiElement
import top.criwits.scaleda.idea.ScaledaBundle
import top.criwits.scaleda.verilog.psi.nodes.expression.HierarchicalIdentifierPsiNode

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
