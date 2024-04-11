package top.scaleda
package systemverilog.psi.nodes.module

import systemverilog.psi.nodes.signal.PortIdentifierPsiNode

import com.intellij.lang.ASTNode
import com.intellij.psi.util.PsiTreeUtil
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode

class ModuleHeadPsiNode(node: ASTNode) extends ANTLRPsiNode(node) {
  /**
   * Return port identifiers of this module
   * @return
   */
  def getPorts: Seq[PortIdentifierPsiNode] = {
    val listOfPorts = PsiTreeUtil.findChildOfType(this, classOf[AbstractListOfPortsPsiNode]) // list_of_ports or list_of_ports_declarations
    if (listOfPorts == null) Seq[PortIdentifierPsiNode]() else listOfPorts.getPortIdentifiers
  }

  def getModuleParameterPortList: Option[ModuleParameterPortListPsiNode] = {
    Option(PsiTreeUtil.getChildOfType(this, classOf[ModuleParameterPortListPsiNode]))
  }

  def getModuleIdentifier: Option[ModuleIdentifierPsiNode] = Option(
    PsiTreeUtil.getChildOfType(this, classOf[ModuleIdentifierPsiNode])
  )
}
