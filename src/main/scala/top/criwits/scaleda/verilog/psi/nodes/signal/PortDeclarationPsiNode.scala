package top.criwits.scaleda
package verilog.psi.nodes.signal

import com.intellij.lang.ASTNode
import top.criwits.scaleda.verilog.psi.nodes.signal.PortDeclarationPsiNode.{INPUT, OUTPUT, INOUT, OUTPUT_REG}

class PortDeclarationPsiNode(node: ASTNode) extends SignalDeclarationPsiNode(node) {
  def getPortType: PortDeclarationPsiNode.PortType = {
    val children = this.getFirstChild.getChildren //TODO better

    if (children.exists(_.textMatches("input"))) return INPUT

    if (children.exists(_.textMatches("inout"))) return INOUT

    if (children.exists(_.textMatches("output"))) {
      if (children.exists(_.textMatches("reg"))) return OUTPUT_REG
      else return OUTPUT
    }

    // TODO: In this case, should search source code!!!

    INPUT // FIXME: ?
  }
}

object PortDeclarationPsiNode extends Enumeration {
  type PortType = Value
  val INPUT, INOUT, OUTPUT, OUTPUT_REG = Value
}