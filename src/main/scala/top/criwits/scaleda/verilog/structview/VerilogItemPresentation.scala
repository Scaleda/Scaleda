package top.criwits.scaleda
package verilog.structview

import com.intellij.icons.AllIcons
import com.intellij.navigation.ItemPresentation
import com.intellij.psi.PsiElement

import javax.swing.Icon

class VerilogItemPresentation(val element: PsiElement) extends ItemPresentation {
  override def getPresentableText: String = element.getNode.getText

  override def getIcon(unused: Boolean): Icon = AllIcons.General.Information
}
