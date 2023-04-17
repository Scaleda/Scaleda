package top.criwits.scaleda
package verilog.annotation.always

import idea.ScaledaBundle
import verilog.psi.nodes.always.{AlwaysConstructPsiNode, DelayOrEventControlPsiNode}
import verilog.psi.nodes.signal.SignalIdentifierPsiNode

import com.intellij.lang.annotation.{AnnotationHolder, Annotator, HighlightSeverity}
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil

class AmbiguousClockAnnotator extends Annotator {
  override def annotate(element: PsiElement, holder: AnnotationHolder): Unit = {
    if (!element.isInstanceOf[DelayOrEventControlPsiNode]) return
    val always = PsiTreeUtil.getParentOfType(element, classOf[AlwaysConstructPsiNode])
    if (always == null) return
    if (!element.asInstanceOf[DelayOrEventControlPsiNode].isEventControl) return
    val events = element.asInstanceOf[DelayOrEventControlPsiNode].getEvents.map(_.getExpression)

    if (!always.isCombinationLogic) {
      // 1. always (posedge clk or posedge a) begin ... end

      val sensitiveSignals =
        try {
          events.map(_.getUsedSignals).foldLeft(Iterable[SignalIdentifierPsiNode]())(_ ++ _)
        } catch {
          case _: Throwable => return
        }
      val usedSignals = always.getUsedSignals

      val remainingSignals = sensitiveSignals.filter(sensitiveSignal => !usedSignals.exists(sensitiveSignal == _))

      if (remainingSignals.size > 1) {
        holder
          .newAnnotation(
            HighlightSeverity.WARNING,
            ScaledaBundle.message("annotation.ambiguous.clock")
          )
          .create()
        return // one annotation is enough
      }

      // 2. always @(posedge clk or posedge clk) begin ... end
      // count name and type, if size > 1, then ambiguous
      val signalsWithType = element
        .asInstanceOf[DelayOrEventControlPsiNode]
        .getEvents
        .map(event => {
          val signal = event.getExpression.getUsedSignals.headOption
          (signal, event.getType)
        })
        .filter(_._1.nonEmpty)
        .map(p => (p._1.get, p._2))
      val signalNames = signalsWithType.map(_._1.getElementName).toSet
      val signalCountMap = signalNames
        .map(name => {
          (signalsWithType.find(_._1.getElementName == name).get._1, signalsWithType.count(_._1.getElementName == name))
        })
        .toMap
      signalCountMap.map(s => {
        if (s._2 > 1) {
          holder
            .newAnnotation(
              HighlightSeverity.WARNING,
              ScaledaBundle.message("annotation.ambiguous.clock")
            )
            // .range(element.getTextRange)
            .create()
        }
      })
    }
  }
}
