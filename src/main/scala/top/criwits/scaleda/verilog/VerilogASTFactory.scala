package top.criwits.scaleda
package verilog

import VerilogLogger.Log

import com.intellij.lang.DefaultASTFactoryImpl
import com.intellij.psi.tree.IElementType
import top.criwits.scaleda.verilog.psi.VerilogPsiLeafNodeFactory

class VerilogASTFactory extends DefaultASTFactoryImpl {
  override def createLeaf(tpe: IElementType, text: CharSequence) = {
    Log.debug(s"createLeaf(${tpe}, ${text})")
    VerilogPsiLeafNodeFactory.create(tpe, text)
  }
}
