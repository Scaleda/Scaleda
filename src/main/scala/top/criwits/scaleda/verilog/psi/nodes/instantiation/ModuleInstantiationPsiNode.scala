package top.criwits.scaleda
package verilog.psi.nodes.instantiation

import verilog.psi.nodes.ReferenceHolder
import verilog.psi.nodes.module.{ModuleDeclarationPsiNode, ModuleIdentifierPsiNode}
import verilog.references.ModuleInstantiationReference

import com.intellij.lang.ASTNode
import com.intellij.openapi.util.TextRange
import com.intellij.psi.util.PsiTreeUtil
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode
import top.criwits.scaleda.verilog.psi.nodes.instantiation.ModuleInstantiationPsiNode.{NAMED, NONE, ORDERED}
import top.criwits.scaleda.verilog.psi.nodes.signal.PortDeclarationPsiNode

import scala.jdk.CollectionConverters._

class ModuleInstantiationPsiNode(node: ASTNode)
  extends ANTLRPsiNode(node) with ReferenceHolder[ModuleIdentifierPsiNode] {
  override def getHoldPsiNode: ModuleIdentifierPsiNode =
    PsiTreeUtil.findChildOfType(this, classOf[ModuleIdentifierPsiNode])

  override def getHoldPsiNodeRelativeTextRange: TextRange =
    getHoldPsiNode.getTextRange.shiftLeft(this.getTextOffset)

  override def getReference = new ModuleInstantiationReference(this)

  def getConnectionType: (ModuleInstantiationPsiNode.Value, Option[IndexedSeq[AnyRef]]) = {
    val list = PsiTreeUtil.findChildOfType(this, classOf[ListOfPortConnectionsPsiNode])
    if (list == null) return (NONE, None)

    val namedPorts   = PsiTreeUtil.getChildrenOfType(list, classOf[NamedPortConnectionPsiNode])
    val orderedPorts = PsiTreeUtil.getChildrenOfType(list, classOf[OrderedPortConnectionPsiNode])

    if (namedPorts != null && namedPorts.nonEmpty) return (NAMED, Some(namedPorts.toIndexedSeq))
    if (orderedPorts != null && orderedPorts.nonEmpty && (orderedPorts.length != 1 || orderedPorts.head.getSignal != null)) return (ORDERED, Some(orderedPorts.toIndexedSeq))

    (NONE, None)
  }

  def getConnectMap: Iterable[(PortDeclarationPsiNode, Option[String])] = {
    val reference = getReference
    val result = reference.multiResolve(true)
    if (result.isEmpty) return Iterable.empty
    val module = result.head.getElement.asInstanceOf[ModuleDeclarationPsiNode]
    val ports = module.getPorts

    val connType = getConnectionType
    connType._1 match {
      case NONE => ports.map(port => (port, None)) // nothing connected
      case ORDERED =>
        val connectedSignals = connType._2.get.map(_.asInstanceOf[OrderedPortConnectionPsiNode])
        ports.zipWithIndex.map(port => (
          port._1,
          if (port._2 < connectedSignals.size) Some(connectedSignals(port._2).getText) else None
        ))
      case NAMED =>
        val connectedPorts = connType._2.get.map(_.asInstanceOf[NamedPortConnectionPsiNode])
        ports.map(port => (
          port,
          {
            val resoved = connectedPorts.filter(f => {
              val h = f.getHoldPsiNode
              if (h != null && h.textMatches(port.getIdentifier.getName)) true else false
            } )
            resoved.map(r => {
              val exp = r.getSignal
              if (exp == null) "" else exp.getText // FIXME: Strange case, expression is null
            }).headOption
          }
        ))
    }
  }
}

object ModuleInstantiationPsiNode extends Enumeration {
  val NONE, NAMED, ORDERED = Value
}
