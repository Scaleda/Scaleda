package top.criwits.scaleda
package verilog.annotation

import idea.ScaledaBundle
import verilog.psi.nodes.instantiation.{ModuleInstantiationPsiNode, NameOfInstancePsiNode}
import verilog.psi.nodes.module.{ModuleDeclarationPsiNode, ModuleIdentifierPsiNode}

import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.lang.annotation.{AnnotationHolder, Annotator, HighlightSeverity}
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil

/** Annotate instantiated module
  */
class ModuleInstantiationAnnotator extends Annotator {
  override def annotate(element: PsiElement, holder: AnnotationHolder): Unit = {
    // check if is module instance
    if (!element.isInstanceOf[ModuleInstantiationPsiNode]) return
    val instance = element.asInstanceOf[ModuleInstantiationPsiNode]

    val reference = instance.getReference
    val result    = reference.multiResolve(true)
    val instantName = PsiTreeUtil.findChildOfType(instance, classOf[NameOfInstancePsiNode])
    val annotateRange = new TextRange(instance.getTextRange.getStartOffset, instantName.getTextRange.getEndOffset) // FIXME

    if (result.isEmpty) {
      holder.newAnnotation(
        HighlightSeverity.ERROR,
        ScaledaBundle.message(
          "annotation.unresolved.module",
          instance.getHoldPsiNode.getNameIdentifier.getText
        )
      )
        .range(annotateRange)
        .highlightType(ProblemHighlightType.LIKE_UNKNOWN_SYMBOL) // good?
        .create()
    } else {
      holder.newAnnotation(
        HighlightSeverity.INFORMATION,
        ScaledaBundle.message("annotation.resolved.module",
          instance.getHoldPsiNode.getNameIdentifier.getText,
          result.head.getElement.asInstanceOf[ModuleDeclarationPsiNode].getFile.getVirtualFile.getName)
      ).create()
    }
  }
}
