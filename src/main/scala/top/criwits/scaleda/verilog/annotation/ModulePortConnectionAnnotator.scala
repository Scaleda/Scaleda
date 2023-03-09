package top.criwits.scaleda
package verilog.annotation

import idea.{ScaledaBundle => SB}
import verilog.psi.nodes.instantiation.{ModuleInstantiationPsiNode, NameOfInstancePsiNode, NamedPortConnectionPsiNode, OrderedPortConnectionPsiNode}
import verilog.psi.nodes.module.ModuleDeclarationPsiNode
import verilog.psi.nodes.signal.PortDeclarationPsiNode

import com.intellij.lang.annotation.{AnnotationHolder, Annotator, HighlightSeverity}
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil

import scala.jdk.CollectionConverters._

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

      /** Several cases:
        *  - No parentheses are given, so neither [[OrderedPortConnectionPsiNode]] nor [[NamedPortConnectionPsiNode]] will be found.
        *    In this case we suggest completing the latter one;
        *  - User had typed (), with ordered connection. We can suggest converting those connections to named connection
        */

      val module       = result.head.getElement.asInstanceOf[ModuleDeclarationPsiNode]
      val ports        = module.getPorts
      val namedPorts   = PsiTreeUtil.findChildrenOfType(instance, classOf[NamedPortConnectionPsiNode]).asScala
      val orderedPorts = PsiTreeUtil.findChildrenOfType(instance, classOf[OrderedPortConnectionPsiNode]).asScala

      if (namedPorts.nonEmpty) {
        val connectedPorts = namedPorts

        val connectMap = ports
          .filter(_.getPortType match {
            case PortDeclarationPsiNode.INOUT | PortDeclarationPsiNode.INPUT => true
            case _                                                           => false
          })
          .map(port =>
            (
              port.getIdentifier.getName,
              connectedPorts.exists(_.getHoldPsiNode.textMatches(port.getIdentifier.getName))
            )
          )

        if (!connectMap.map(_._2).reduce(_ && _)) {
          holder
            .newAnnotation(
              HighlightSeverity.ERROR,
              SB.message(
                "annotation.not.connected.port",
                module.getName,
                connectMap.filter(!_._2).map(_._1).mkString(", ")
              )
            )
            .range(annotateRange)
            .create()
        }
      }

    }

  }
}
