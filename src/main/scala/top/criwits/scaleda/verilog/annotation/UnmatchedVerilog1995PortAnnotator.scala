package top.criwits.scaleda
package verilog.annotation

import idea.ScaledaBundle
import verilog.psi.nodes.module.{PortPsiNode, PortReferencePsiNode}

import com.intellij.lang.annotation.{AnnotationHolder, Annotator, HighlightSeverity}
import com.intellij.psi.PsiElement

class UnmatchedVerilog1995PortAnnotator extends Annotator {
  override def annotate(element: PsiElement, holder: AnnotationHolder): Unit = {
    if (!element.isInstanceOf[PortReferencePsiNode]) return
    val e    = element.asInstanceOf[PortReferencePsiNode]
    val name = e.getName
    if (name == null) return

    if (e.getReference.resolve() == null) {
      holder
        .newAnnotation(HighlightSeverity.ERROR, ScaledaBundle.message("annotation.unmatched.old.style.port", name))
        .create()
    }
  }
}
