package top.criwits.scaleda
package verilog.structview

import com.intellij.icons.AllIcons
import com.intellij.navigation.ItemPresentation
import com.intellij.psi.PsiElement
import top.criwits.scaleda.idea.utils.Icons
import top.criwits.scaleda.verilog.psi.factory.nodes.{AlwaysConstructPsiNode, ModuleDeclarationPsiNode, NetDeclarationPsiNode, RegDeclarationPsiNode}

import javax.swing.Icon

class VerilogItemPresentation(val element: PsiElement) extends ItemPresentation {
  override def getPresentableText: String = element.getNode.getText // FIXME

  override def getIcon(unused: Boolean): Icon = element match {
    case _: ModuleDeclarationPsiNode => Icons.verilogModule
//    case _: RegDeclarationPsiNode =>
//    case _: NetDeclarationPsiNode =>
//    case _: AlwaysConstructPsiNode => TODO
    case _ => AllIcons.General.TodoQuestion // should never reach!
  }
}
