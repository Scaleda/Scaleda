package top.criwits.scaleda
package verilog.structview

import com.intellij.navigation.ItemPresentation
import com.intellij.psi.{PsiElement, PsiFile}

/**
 * Structure view element implementation for root node in verilog file.
 * @param element a [[top.criwits.scaleda.verilog.VerilogPSIFileRoot]]
 */
class VerilogStructViewRootElement(element: PsiFile) extends VerilogStructViewElement(element) {
  override def getPresentation: ItemPresentation = new VerilogRootPresentation(element)
}
