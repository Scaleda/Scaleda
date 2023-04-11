package top.criwits.scaleda
package verilog.annotation

import idea.ScaledaBundle
import verilog.psi.nodes.always.AlwaysConstructPsiNode
import verilog.psi.nodes.module.ModuleDeclarationPsiNode

import com.intellij.lang.annotation.{AnnotationHolder, Annotator, HighlightSeverity}
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import top.criwits.scaleda.verilog.psi.nodes.assignments.AlwaysAssignmentPsiNode

import scala.jdk.CollectionConverters._

class MultiDrivenAnnotator extends Annotator {
  override def annotate(element: PsiElement, holder: AnnotationHolder): Unit = {
    if (!element.isInstanceOf[AlwaysAssignmentPsiNode]) return
    val assignmenet = element.asInstanceOf[AlwaysAssignmentPsiNode]

    val module = PsiTreeUtil.getParentOfType(element, classOf[ModuleDeclarationPsiNode])
    if (module == null) return

    val always = PsiTreeUtil.getParentOfType(element, classOf[AlwaysConstructPsiNode])
    if (always == null) return
    val alwayss      = PsiTreeUtil.findChildrenOfType(module, classOf[AlwaysConstructPsiNode]).asScala
    val otherAlwayss = alwayss.filter(_ != always)

    val myDriven = assignmenet.getLValues

    // FIXME: multiple annotation!
    myDriven.foreach(driven => {
      otherAlwayss
        .map(_.getAssignments)
        .foreach(_.foreach(assign => {
          if (assign.getLValues.exists(_.getName == driven.getName)) {
            holder
              .newAnnotation(
                HighlightSeverity.WARNING,
                ScaledaBundle.message("annotation.multidriven", driven.getName)
              )
              .withFix(new MultiDrivenNavigateIntent(assign))
              .create()
          }
        }))
    })

  }
}
