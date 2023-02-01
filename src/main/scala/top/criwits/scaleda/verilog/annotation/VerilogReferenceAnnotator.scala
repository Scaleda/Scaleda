package top.criwits.scaleda
package verilog.annotation

import verilog.psi.factory.nodes.{NamedPortConnectionPsiNode, ReferenceHolder}

import com.intellij.lang.annotation.{AnnotationHolder, Annotator, HighlightSeverity}
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.{PsiElement, PsiReferenceBase}
import top.criwits.scaleda.verilog.psi.factory.nodes.module.{ModuleIdentifierPsiNode, PortIdentifierPsiNode}
import top.criwits.scaleda.verilog.references.ModuleInstantiationPsiNode

class VerilogReferenceAnnotator extends Annotator {
  override def annotate(element: PsiElement, holder: AnnotationHolder): Unit = {
    val reference = element match {
      case _: ModuleIdentifierPsiNode =>
        val r = PsiTreeUtil.getParentOfType(element, classOf[ModuleInstantiationPsiNode])
        if (r == null) return
        r.getReference
      case _: PortIdentifierPsiNode =>
        val r = PsiTreeUtil.getParentOfType(element, classOf[NamedPortConnectionPsiNode])
        if (r == null) return
        r.getReference
      case _ => if (element != null) element.getReference else null
    }
    if (reference == null) return
    reference match {
      case r: PsiReferenceBase.Poly[_] =>
        if (r.multiResolve(true).isEmpty) {
          annotateUnresolvedReference(element, holder)
        }
      case r: PsiReferenceBase[_] =>
        if (r.resolve() == null) {
          annotateUnresolvedReference(element, holder)
        }
    }
  }

  private def annotateUnresolvedReference(psiElement: PsiElement, annotationHolder: AnnotationHolder) = {
    if (psiElement.isInstanceOf[ReferenceHolder[_]]) {
      annotationHolder.newAnnotation(HighlightSeverity.ERROR, "Unresolved reference")
        .range(psiElement)
        .create()
    } else {
      annotationHolder.newAnnotation(HighlightSeverity.ERROR, "Unresolved reference")
        .create()
    }
  }
}
