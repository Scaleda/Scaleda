package top.criwits.scaleda
package verilog.psi.nodes.signal

import verilog.psi.nodes.signal.PortDeclarationPsiNode.{INOUT, INPUT, OUTPUT, OUTPUT_REG}

import com.intellij.lang.ASTNode

class PortDeclarationPsiNode(node: ASTNode) extends SignalDeclarationPsiNode(node) {
  def getPortType: PortDeclarationPsiNode.PortType = {
    val children = this.getChildren //TODO better

    if (children.exists(_.textMatches("input"))) return INPUT

    if (children.exists(_.textMatches("inout"))) return INOUT

    if (children.exists(_.textMatches("output"))) {
      if (children.exists(_.textMatches("reg"))) return OUTPUT_REG
      else return OUTPUT
    }

    // TODO: In this case, should search source code!!!
    // TODO: signed?
    INPUT
  }

  override def getTypeText: String = getPortType match {
    case INPUT      => "input"
    case INOUT      => "inout"
    case OUTPUT     => "output"
    case OUTPUT_REG => "output reg"
  }
}

object PortDeclarationPsiNode extends Enumeration {
  type PortType = Value
  val INPUT, INOUT, OUTPUT, OUTPUT_REG = Value
}
