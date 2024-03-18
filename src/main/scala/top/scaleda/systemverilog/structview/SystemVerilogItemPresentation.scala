package top.scaleda
package systemverilog.structview

import idea.utils.Icons
import systemverilog.psi.nodes.StructureViewNode
// import systemverilog.psi.nodes.always.AlwaysConstructPsiNode
import systemverilog.psi.nodes.module.ModuleDeclarationPsiNode
import systemverilog.psi.nodes.signal.{NetIdentifierPsiNode, VariableIdentifierPsiNode}

import com.intellij.icons.AllIcons
import com.intellij.navigation.ItemPresentation
import com.intellij.psi.PsiElement

import javax.swing.Icon

class SystemVerilogItemPresentation(val element: PsiElement) extends ItemPresentation {
  override def getPresentableText: String = element match {
    case node: StructureViewNode => node.getElementName
    case _ => "(unknown)"
  }

  override def getIcon(unused: Boolean): Icon = element match {
    case _: ModuleDeclarationPsiNode => Icons.verilogModule
    case _: VariableIdentifierPsiNode => Icons.verilogReg
    case _: NetIdentifierPsiNode => Icons.verilogWire
    // case _: AlwaysConstructPsiNode => Icons.verilogAlways
    case _ => AllIcons.General.TodoQuestion // should never reach!
  }
}
