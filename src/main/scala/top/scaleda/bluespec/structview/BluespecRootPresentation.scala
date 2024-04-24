package top.scaleda
package bluespec.structview

import idea.utils.Icons

import com.intellij.navigation.ItemPresentation
import com.intellij.psi.PsiFile

import javax.swing.Icon

class BluespecRootPresentation(val element: PsiFile) extends ItemPresentation {
  override def getPresentableText: String     = element.getVirtualFile.getName
  override def getIcon(unused: Boolean): Icon = Icons.bluespec
}
