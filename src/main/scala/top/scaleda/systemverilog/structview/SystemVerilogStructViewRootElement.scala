package top.scaleda
package systemverilog.structview

import com.intellij.navigation.ItemPresentation
import com.intellij.psi.PsiFile

/**
 * Structure view element implementation for root node in verilog file.
 * @param element a [[top.scaleda.systemverilog.SystemVerilogPSIFileRoot]]
 */
class SystemVerilogStructViewRootElement(element: PsiFile) extends SystemVerilogStructViewElement(element) {
  override def getPresentation: ItemPresentation = new SystemVerilogRootPresentation(element)
}
