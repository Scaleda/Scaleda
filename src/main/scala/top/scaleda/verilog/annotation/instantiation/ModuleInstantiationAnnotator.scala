package top.scaleda
package verilog.annotation.instantiation

import idea.ScaledaBundle
import verilog.psi.nodes.instantiation.{ModuleInstantiationPsiNode, NameOfInstancePsiNode}

import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.lang.annotation.{AnnotationHolder, Annotator, HighlightSeverity}
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import top.scaleda.verilog.psi.nodes.module.ModuleIdentifierPsiNode

/** Annotate instantiated module
  */
class ModuleInstantiationAnnotator extends Annotator {
  override def annotate(element: PsiElement, holder: AnnotationHolder): Unit = {
    // check if is module instance
    if (!element.isInstanceOf[ModuleInstantiationPsiNode]) return
    val instance = element.asInstanceOf[ModuleInstantiationPsiNode]
    if (instance == null) return

    val reference = instance.getReference
    val result    = reference.resolve
    val moduleName = PsiTreeUtil.findChildOfType(instance, classOf[ModuleIdentifierPsiNode])
    val instanceName = PsiTreeUtil.findChildOfType(instance, classOf[NameOfInstancePsiNode])
    val annotateRange = new TextRange(instance.getTextRange.getStartOffset, if (instanceName == null) moduleName.getTextRange.getEndOffset else instanceName.getTextRange.getEndOffset)

    if (result == null) {
      holder.newAnnotation(
        HighlightSeverity.ERROR,
        ScaledaBundle.message(
          "annotation.unresolved.module"
        )
      )
        .range(annotateRange)
        .highlightType(ProblemHighlightType.LIKE_UNKNOWN_SYMBOL)
        .create()
    }
  }
}
