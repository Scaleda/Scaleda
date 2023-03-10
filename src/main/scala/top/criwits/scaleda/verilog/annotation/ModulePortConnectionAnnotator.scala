package top.criwits.scaleda
package verilog.annotation

import idea.{ScaledaBundle => SB}
import verilog.psi.nodes.instantiation.{ModuleInstantiationPsiNode, NameOfInstancePsiNode, NamedPortConnectionPsiNode, OrderedPortConnectionPsiNode}
import verilog.psi.nodes.signal.PortDeclarationPsiNode

import com.intellij.lang.annotation.{AnnotationHolder, Annotator, HighlightSeverity}
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import top.criwits.scaleda.verilog.psi.nodes.module.ModuleDeclarationPsiNode

class ModulePortConnectionAnnotator extends Annotator {
  override def annotate(element: PsiElement, holder: AnnotationHolder): Unit = {
    // check if is module instance
    if (!element.isInstanceOf[ModuleInstantiationPsiNode]) return
    val instance = element.asInstanceOf[ModuleInstantiationPsiNode]

    val reference     = instance.getReference
    val result        = reference.multiResolve(true)
    val instantName   = PsiTreeUtil.findChildOfType(instance, classOf[NameOfInstancePsiNode])
    val annotateRange = new TextRange(instance.getTextRange.getStartOffset, instantName.getTextRange.getEndOffset)

    if (result.nonEmpty) {
      // Module is valid, so should check
      val module = result.head.getElement.asInstanceOf[ModuleDeclarationPsiNode]
      /** Several cases:
       *  - No parentheses are given, so neither [[OrderedPortConnectionPsiNode]] nor [[NamedPortConnectionPsiNode]] will be found.
       *    In this case we suggest completing the latter one;
       *  - User had typed (), with ordered connection. We can suggest converting those connections to named connection
       */

      val typeAndConnection = instance.getConnectionType
      typeAndConnection._1 match {
        case ModuleInstantiationPsiNode.NAMED =>
          val connectMap = instance.getConnectMap
          val mp = connectMap
              .filter(p =>
                p._1.getPortType == PortDeclarationPsiNode.INPUT || p._1.getPortType == PortDeclarationPsiNode.INOUT
              )
          if (
            !mp
              .map(_._2.nonEmpty)
              .reduce(_ && _)
          ) {
            holder
              .newAnnotation(
                HighlightSeverity.ERROR,
                SB.message(
                  "annotation.not.connected.port",
                  module.getName,
                  mp.filter(_._2.isEmpty).map(_._1.getIdentifier.getName).mkString(", ")
                )
              )
              .withFix(new ModulePortConnectionIntent(instance, Seq.empty))
              .range(annotateRange)
              .create()
          }
        case ModuleInstantiationPsiNode.NONE =>
          if (module.getPorts.length != 0) {
          holder
              .newAnnotation(
                HighlightSeverity.ERROR,
                SB.message(
                  "annotation.not.connected.ordered.port",
                  module.getName,
                  module.getPorts.map(_.getIdentifier.getName).mkString(", ")
                )
              )
              .withFix(new ModulePortConnectionIntent(instance, Seq.empty))
              .range(annotateRange)
              .create()
          }
        case ModuleInstantiationPsiNode.ORDERED =>
          val connectMap = instance.getConnectMap
          if (
            !connectMap
              .map(_._2.nonEmpty)
              .reduce(_ && _)
          ) {
            holder
              .newAnnotation(
                HighlightSeverity.ERROR,
                SB.message(
                  "annotation.not.connected.ordered.port",
                  module.getName,
                  connectMap.filter(_._2.isEmpty).map(_._1.getIdentifier.getName).mkString(", ")
                )
              )
              .withFix(new ModulePortConnectionIntent(instance, Seq.empty))
              .range(annotateRange)
              .create()
          }
      }
    }

  }
}
