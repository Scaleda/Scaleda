package top.criwits.scaleda
package verilog

import verilog.psi.factory.VerilogPsiLeafNodeFactory

import com.intellij.lang.DefaultASTFactoryImpl
import com.intellij.psi.tree.IElementType

class VerilogASTFactory extends DefaultASTFactoryImpl {
  override def createLeaf(`type`: IElementType, text: CharSequence) =
    VerilogPsiLeafNodeFactory.create(`type`, text)
}
