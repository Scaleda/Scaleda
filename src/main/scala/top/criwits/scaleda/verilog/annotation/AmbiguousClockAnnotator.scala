package top.criwits.scaleda
package verilog.annotation

import com.intellij.lang.annotation.{AnnotationHolder, Annotator, HighlightSeverity}
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import top.criwits.scaleda.idea.ScaledaBundle
import top.criwits.scaleda.verilog.psi.nodes.always.{AlwaysConstructPsiNode, DelayOrEventControlPsiNode}
import top.criwits.scaleda.verilog.psi.nodes.signal.SignalIdentifierPsiNode

class AmbiguousClockAnnotator extends Annotator {
  override def annotate(element: PsiElement, holder: AnnotationHolder): Unit = {
    if (!element.isInstanceOf[DelayOrEventControlPsiNode]) return
    val always = PsiTreeUtil.getParentOfType(element, classOf[AlwaysConstructPsiNode])
    if (always == null) return
    if (!element.asInstanceOf[DelayOrEventControlPsiNode].isEventControl) return
    val events = element.asInstanceOf[DelayOrEventControlPsiNode].getEvents.map(_.getExpression)

    val sensitiveSiganls = try {
      events.map(_.getUsedSignals).foldLeft(Iterable[SignalIdentifierPsiNode]())(_ ++ _)
    } catch {
      case _: Throwable => return
    }
    val usedSiganls = always.getUsedSignals

    val remainingSiganls = sensitiveSiganls.filter(sensitiveSignal => !usedSiganls.exists(sensitiveSignal == _))

    if (remainingSiganls.size > 1) {
      holder.newAnnotation(
        HighlightSeverity.WARNING,
        ScaledaBundle.message("annotation.ambiguous.clock")
      )
        .create()
    }
  }
}
