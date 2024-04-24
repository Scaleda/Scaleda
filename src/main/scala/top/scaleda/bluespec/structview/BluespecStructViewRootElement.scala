package top.scaleda
package bluespec.structview

import bluespec.BluespecPSIFileRoot

import com.intellij.navigation.ItemPresentation
import com.intellij.psi.PsiFile

/**
 * Structure view element implementation for root node in verilog file.
 * @param element a [[BluespecPSIFileRoot]]
 */
class BluespecStructViewRootElement(element: PsiFile) extends BluespecStructViewElement(element) {
  override def getPresentation: ItemPresentation = new BluespecRootPresentation(element)
}
