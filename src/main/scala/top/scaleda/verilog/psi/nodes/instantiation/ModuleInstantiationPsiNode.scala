package top.scaleda
package verilog.psi.nodes.instantiation

import verilog.psi.nodes.ReferenceHolder
import verilog.psi.nodes.expression.ExpressionPsiNode
import verilog.psi.nodes.instantiation.ModuleInstantiationPsiNode.{NAMED, NONE, ORDERED}
import verilog.psi.nodes.module.{ModuleDeclarationPsiNode, ModuleIdentifierPsiNode}
import verilog.psi.nodes.signal.PortIdentifierPsiNode
import verilog.references.ModuleInstantiationReference

import com.intellij.lang.ASTNode
import com.intellij.openapi.util.TextRange
import com.intellij.psi.util.PsiTreeUtil
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode

class ModuleInstantiationPsiNode(node: ASTNode)
    extends ANTLRPsiNode(node)
    with ReferenceHolder[ModuleIdentifierPsiNode] {
  override def getHoldPsiNode: ModuleIdentifierPsiNode =
    PsiTreeUtil.findChildOfType(this, classOf[ModuleIdentifierPsiNode])

  override def getHoldPsiNodeRelativeTextRange: TextRange =
    getHoldPsiNode.getTextRange.shiftLeft(this.getTextOffset)

  override def getReference = new ModuleInstantiationReference(this)

  def getPortConnectionList: ListOfPortConnectionsPsiNode =
    PsiTreeUtil.findChildOfType(this, classOf[ListOfPortConnectionsPsiNode])

  def getConnectionType: (ModuleInstantiationPsiNode.Value, Option[Seq[AnyRef]]) = {
    val list = getPortConnectionList
    if (list == null) return (NONE, None)

    val namedPorts   = list.getNamedPorts
    val orderedPorts = list.getOrderedPorts

    if (namedPorts.nonEmpty) return (NAMED, Some(namedPorts.toIndexedSeq))
    if (orderedPorts.nonEmpty && (orderedPorts.length != 1 || orderedPorts.head.getExpression != null))
      return (ORDERED, Some(orderedPorts.toIndexedSeq))

    (NONE, None)
  }

  /** Render the connect map for this instance.
    * @return the connect map. like:
    * <code>
    *   Seq(
    *     (Identifier(a), None),             // NOT CONNECTED
    *     (Identifier(b), Some(expression)), // CONNECTED TO expression
    *     (Identifier(c), Some(null))        // STUB
    *   )
    * </code>
    */
  def getConnectMap: Seq[(PortIdentifierPsiNode, Option[ExpressionPsiNode])] = {
    val reference = getReference
    val result    = reference.resolve
    if (result == null) return Seq()
    val module = result.asInstanceOf[ModuleDeclarationPsiNode]
    val ports  = module.getModuleHead.getPorts

    val connType = getConnectionType

    connType._1 match {
      case NONE => ports.map(port => (port, None)) // nothing connected
      case ORDERED =>
        val connectedSignals = connType._2.get.map(_.asInstanceOf[OrderedPortConnectionPsiNode])
        ports.zipWithIndex.map(port =>
          (
            port._1,
            if (port._2 < connectedSignals.size) Some(connectedSignals(port._2).getExpression) else None
          )
        )
      case NAMED =>
        val connectedPorts = connType._2.get.map(_.asInstanceOf[NamedPortConnectionPsiNode])
        ports.map(port =>
          (
            port,
            connectedPorts
              .find(conn => {
                val resolve = conn.getReference.resolve
                resolve != null && resolve.asInstanceOf[PortIdentifierPsiNode].getName == port.getName
              })
              .map(_.getExpression)
          )
        )
    }
  }
}

object ModuleInstantiationPsiNode extends Enumeration {
  val NONE, NAMED, ORDERED = Value
}
