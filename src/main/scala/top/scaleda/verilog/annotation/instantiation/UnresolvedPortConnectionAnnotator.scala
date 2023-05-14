package top.scaleda
package verilog.annotation.instantiation

import idea.ScaledaBundle
import verilog.psi.nodes.instantiation.AbstractPortConnectionPsiNode

import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.lang.annotation.{AnnotationHolder, Annotator, HighlightSeverity}
import com.intellij.psi.PsiElement

class UnresolvedPortConnectionAnnotator extends Annotator {
  override def annotate(element: PsiElement, holder: AnnotationHolder): Unit = {
    if (!element.isInstanceOf[AbstractPortConnectionPsiNode[_]]) return
    val conn = element.asInstanceOf[AbstractPortConnectionPsiNode[_]]

    // FIXME: buggy fix. If there's no port connected, an 'Ordered..' will be reduced.
    if (conn.getText.isBlank) return

    if (conn.getReference == null || conn.getReference.resolve() == null) {
      holder
        .newAnnotation(HighlightSeverity.ERROR, ScaledaBundle.message("annotation.unresolved.port.reference"))
        .highlightType(ProblemHighlightType.LIKE_UNKNOWN_SYMBOL)
        .withFix(new RemoveItemWithCommasIntent[AbstractPortConnectionPsiNode[_]](conn, ScaledaBundle.message("annotation.unresolved.port.reference.fix")))
        .range(conn.getTextRange)
        .create()
    }
  }
}
