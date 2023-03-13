package top.criwits.scaleda
package verilog.psi.nodes.instantiation

import verilog.psi.nodes.signal.PortDeclarationPsiNode

import top.criwits.scaleda.verilog.psi.nodes.expression.ExpressionPsiNode

/**
 * Abstract base class for port connection
 */
abstract class PortConnectionPsiNode {
  def getPort: PortDeclarationPsiNode
  def getSignal: ExpressionPsiNode
}
