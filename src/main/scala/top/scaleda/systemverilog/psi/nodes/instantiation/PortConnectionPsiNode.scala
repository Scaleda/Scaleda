package top.scaleda
package systemverilog.psi.nodes.instantiation

import systemverilog.psi.nodes.expression.ExpressionPsiNode
import systemverilog.psi.nodes.signal.PortDeclarationPsiNode

/**
 * Abstract base class for port connection
 */
abstract class PortConnectionPsiNode {
  def getPort: PortDeclarationPsiNode
  def getSignal: ExpressionPsiNode
}
