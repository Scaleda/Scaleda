package top.scaleda
package verilog.psi.nodes.module

import verilog.psi.nodes.signal.PortIdentifierPsiNode

import com.intellij.lang.ASTNode
import com.intellij.psi.util.PsiTreeUtil

class ListOfPortsPsiNode(node: ASTNode) extends AbstractListOfPortsPsiNode(node) {

  /** This is designed for Verilog-1995 style port declaration, where only identifiers are written in module head,
    * all their 'de facto' definitions are inside module body.
    * @return a sequence of port declarations POINTING TO THE ONES INSIDE MODULE BODY
    */
  override def getPortIdentifiers: Seq[PortIdentifierPsiNode] = {
    val ports = PsiTreeUtil.getChildrenOfType(this, classOf[PortPsiNode])
    ports
      .map(port => {
        port.getPortExpression
          .flatMap(_.getPortReference)
          .flatMap(ref => Option(ref.getReference.resolve()))
          .map(_.asInstanceOf[PortIdentifierPsiNode])
      })
      .filter(_.isDefined)
      .map(_.get)
      .toSeq
  }
}
