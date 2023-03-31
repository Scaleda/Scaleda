package top.criwits.scaleda
package verilog.annotation

import idea.{ScaledaBundle => SB}
import verilog.psi.nodes.instantiation.{
  ModuleInstantiationPsiNode,
  NameOfInstancePsiNode,
  NamedPortConnectionPsiNode,
  OrderedPortConnectionPsiNode
}
import verilog.psi.nodes.module.ModuleDeclarationPsiNode
import verilog.psi.nodes.signal.PortDeclarationPsiNode

import com.intellij.lang.annotation.{AnnotationHolder, Annotator, HighlightSeverity}
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil

class ModulePortConnectionAnnotator extends Annotator {
  override def annotate(element: PsiElement, holder: AnnotationHolder): Unit = {
    // check if is module instance
    if (!element.isInstanceOf[ModuleInstantiationPsiNode]) return
    val instance = element.asInstanceOf[ModuleInstantiationPsiNode]
    if (instance == null) return

    val reference     = instance.getReference
    val result        = reference.multiResolve(true)
    val instantName   = PsiTreeUtil.findChildOfType(instance, classOf[NameOfInstancePsiNode])
    val textRange = instance.getTextRange
    val nameTextRange = instantName.getTextRange
    if (textRange == null || nameTextRange == null) return
    val annotateRange = new TextRange(textRange.getStartOffset, nameTextRange.getEndOffset)

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
          // filter input / inout port
          val mp = connectMap
            .filter(p =>
              p._1.getPortType == PortDeclarationPsiNode.INPUT || p._1.getPortType == PortDeclarationPsiNode.INOUT
            )
          // find unconnected (without expression)
          if (!mp.forall(_._2.nonEmpty)) {
            holder
              .newAnnotation(
                HighlightSeverity.ERROR,
                SB.message(
                  "annotation.not.connected.port",
                  module.getName,
                  mp.filter(_._2.isEmpty).map(_._1.getIdentifier.getName).mkString(", ")
                )
              )
              .withFix(new ConnectPortIndent(instance, mp) {
                override def getText: String = SB.message("annotation.not.connected.port.fix.input")
              })
              .withFix(new ConnectPortIndent(instance, connectMap) {
                override def getText: String = SB.message("annotation.not.connected.port.fix.all")
              })
              .range(annotateRange)
              .create()
          }
        case ModuleInstantiationPsiNode.NONE =>
          if (module.getPorts.length != 0) {
            val connectMap = instance.getConnectMap
            holder
              .newAnnotation(
                HighlightSeverity.ERROR,
                SB.message(
                  "annotation.not.connected.ordered.port",
                  module.getName,
                  module.getPorts.map(_.getIdentifier.getName).mkString(", ")
                )
              )
              .withFix(new ConnectPortIndent(instance, connectMap) {
                override def getText: String = SB.message("annotation.not.connected.port.fix.all")
              })
              .range(annotateRange)
              .create()
          }
        case ModuleInstantiationPsiNode.ORDERED =>
          val connectMap = instance.getConnectMap
          if (!connectMap.forall(_._2.nonEmpty)) {
            holder
              .newAnnotation(
                HighlightSeverity.ERROR,
                SB.message(
                  "annotation.not.connected.ordered.port",
                  module.getName,
                  connectMap.filter(_._2.isEmpty).map(_._1.getIdentifier.getName).mkString(", ")
                )
              )
              .withFix(new ConnectPortIndent(instance, connectMap) {
                override def getText: String = SB.message("annotation.not.connected.port.fix.all")
              })
              .range(annotateRange)
              .create()
          }
      }
    }

  }
}
