package top.scaleda
package verilog.structview

import com.intellij.icons.AllIcons
import com.intellij.navigation.ItemPresentation
import com.intellij.psi.PsiElement
import top.scaleda.idea.utils.Icons
import top.scaleda.verilog.psi.nodes.StructureViewNode
import top.scaleda.verilog.psi.nodes.always.AlwaysConstructPsiNode
import top.scaleda.verilog.psi.nodes.module.ModuleDeclarationPsiNode
import top.scaleda.verilog.psi.nodes.signal.{NetDeclarationPsiNode, NetIdentifierPsiNode, VariableDeclarationPsiNode, VariableIdentifierPsiNode}

import javax.swing.Icon

class VerilogItemPresentation(val element: PsiElement) extends ItemPresentation {
  override def getPresentableText: String = element match {
    case node: StructureViewNode => node.getElementName
    case _ => "(unknown)"
  }

  override def getIcon(unused: Boolean): Icon = element match {
    case _: ModuleDeclarationPsiNode => Icons.verilogModule
    case _: VariableIdentifierPsiNode => Icons.verilogReg
    case _: NetIdentifierPsiNode => Icons.verilogWire
    case _: AlwaysConstructPsiNode => Icons.verilogAlways
    case _ => AllIcons.General.TodoQuestion // should never reach!
  }
}
