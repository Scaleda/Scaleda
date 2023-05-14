package top.scaleda
package verilog.psi.nodes.module

import verilog.psi.nodes.signal.PortIdentifierPsiNode

import com.intellij.lang.ASTNode
import com.intellij.psi.util.PsiTreeUtil
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode

class ModuleHeadPsiNode(node: ASTNode) extends ANTLRPsiNode(node) {
  def getPorts: Seq[PortIdentifierPsiNode] = {
    val listOfPorts = PsiTreeUtil.findChildOfType(this, classOf[AbstractListOfPortsPsiNode])
    if (listOfPorts == null) Seq[PortIdentifierPsiNode]() else listOfPorts.getPortIdentifiers
  }

  def getModuleParameterPortList: Option[ModuleParameterPortListPsiNode] = {
    Option(PsiTreeUtil.getChildOfType(this, classOf[ModuleParameterPortListPsiNode]))
  }

  def getModuleIdentifier: Option[ModuleIdentifierPsiNode] = Option(
    PsiTreeUtil.getChildOfType(this, classOf[ModuleIdentifierPsiNode])
  )
}
