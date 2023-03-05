package top.criwits.scaleda
package verilog.annotation

import idea.ScaledaBundle
import verilog.psi.nodes.instantiation.ModuleInstantiationPsiNode
import verilog.psi.nodes.module.{ModuleDeclarationPsiNode, ModuleIdentifierPsiNode}

import com.intellij.lang.annotation.{AnnotationHolder, Annotator, HighlightSeverity}
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil

/** Annotate instantiated module
  */
class ModuleInstantiationAnnotator extends Annotator {
  override def annotate(element: PsiElement, holder: AnnotationHolder): Unit = {
    // check if is module instance
    if (!element.isInstanceOf[ModuleIdentifierPsiNode]) return
    val instance = PsiTreeUtil.getParentOfType(element, classOf[ModuleInstantiationPsiNode])
    if (instance == null) return

    val reference = instance.getReference
    val result    = reference.multiResolve(true)

    if (result.isEmpty) {
      holder.newAnnotation(
        HighlightSeverity.ERROR,
        ScaledaBundle.message(
          "annotation.unresolved.module",
          element.asInstanceOf[ModuleIdentifierPsiNode].getNameIdentifier.getText
        )
      ).create()
    } else {
      holder.newAnnotation(
        HighlightSeverity.INFORMATION,
        ScaledaBundle.message("annotation.resolved.module",
          element.asInstanceOf[ModuleIdentifierPsiNode].getNameIdentifier.getText,
          result.head.getElement.asInstanceOf[ModuleDeclarationPsiNode].getFile.getVirtualFile.getName)
      ).create()
    }
  }
}
