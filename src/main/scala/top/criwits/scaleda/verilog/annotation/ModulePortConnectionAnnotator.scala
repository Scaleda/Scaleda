package top.criwits.scaleda
package verilog.annotation

import idea.{ScaledaBundle => SB}
import verilog.psi.nodes.instantiation.{ModuleInstantiationPsiNode, NamedPortConnectionPsiNode}
import verilog.psi.nodes.module.{ModuleDeclarationPsiNode, ModuleIdentifierPsiNode}
import verilog.psi.nodes.signal.PortDeclarationPsiNode

import com.intellij.lang.annotation.{AnnotationHolder, Annotator, HighlightSeverity}
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil

import scala.jdk.CollectionConverters._

class ModulePortConnectionAnnotator extends Annotator {
  override def annotate(element: PsiElement, holder: AnnotationHolder): Unit = {
    // check if is module instance
    if (!element.isInstanceOf[ModuleIdentifierPsiNode]) return
    val instance = PsiTreeUtil.getParentOfType(element, classOf[ModuleInstantiationPsiNode])
    if (instance == null) return

    val reference = instance.getReference
    val result    = reference.multiResolve(true)

    if (result.nonEmpty) {
      val module = result.head.getElement.asInstanceOf[ModuleDeclarationPsiNode]
      val ports = module.getPorts
      val connectedPorts = PsiTreeUtil.findChildrenOfType(instance, classOf[NamedPortConnectionPsiNode]).asScala

      val connectMap = ports.filter(_.getPortType match {
        case PortDeclarationPsiNode.INOUT | PortDeclarationPsiNode.INPUT => true
        case _ => false
      }).map(port => (
        port.getIdentifier.getName,
        connectedPorts.exists(_.getHoldPsiNode.textMatches(port.getIdentifier.getName))
      ))

      if (!connectMap.map(_._2).reduce(_ && _)) {
        holder.newAnnotation(
          HighlightSeverity.ERROR,
          SB.message("annotation.not.connected.port",
            module.getName,
            connectMap.filter(!_._2).map(_._1).mkString(", "))
        ).create()
      }
    }

  }
}
