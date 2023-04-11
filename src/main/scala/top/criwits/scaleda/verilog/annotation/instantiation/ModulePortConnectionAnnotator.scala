package top.criwits.scaleda
package verilog.annotation.instantiation

import idea.{ScaledaBundle => SB}
import verilog.psi.nodes.instantiation.{ModuleInstancePsiNode, ModuleInstantiationPsiNode}
import verilog.psi.nodes.module.ModuleDeclarationPsiNode

import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.lang.annotation.{AnnotationHolder, Annotator, HighlightSeverity}
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil

class ModulePortConnectionAnnotator extends Annotator {
  override def annotate(element: PsiElement, holder: AnnotationHolder): Unit = {
    // check if is module instance
    if (!element.isInstanceOf[ModuleInstantiationPsiNode]) return
    val instance = element.asInstanceOf[ModuleInstantiationPsiNode]
    if (instance == null) return

    val reference = instance.getReference
    val result    = reference.resolve

    if (result != null) {
      // Module is valid, so should check
      val module  = result.asInstanceOf[ModuleDeclarationPsiNode]
      val connMap = instance.getConnectMap

      if (connMap.exists(_._2.isEmpty)) {
        val moduleInstancePsiNode = PsiTreeUtil.getChildOfType(instance, classOf[ModuleInstancePsiNode])
        if (moduleInstancePsiNode != null) {
          // has empty connection
          holder
            .newAnnotation(
              HighlightSeverity.WARNING,
              SB.message("annotation.not.connected.port", connMap.filter(_._2.isEmpty).map(_._1.getName).mkString(", "))
            )
            .range(moduleInstancePsiNode.getTextRange)
            .withFix(new ModulePortConnectionIntent(instance, connMap))
            .highlightType(ProblemHighlightType.WARNING)
            .create()
        }
      }

    }

  }
}
