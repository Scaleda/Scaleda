package top.scaleda
package systemverilog.structview

import idea.utils.Icons

import com.intellij.navigation.ItemPresentation
import com.intellij.psi.PsiFile

import javax.swing.Icon

class SystemVerilogRootPresentation(val element: PsiFile) extends ItemPresentation {
  override def getPresentableText: String     = element.getVirtualFile.getName
  override def getIcon(unused: Boolean): Icon = Icons.verilog
}
