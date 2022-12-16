package top.criwits.scaleda
package verilog

import verilog.psi.factory.VerilogPsiLeafNodeFactory

import com.intellij.lang.DefaultASTFactoryImpl
import com.intellij.psi.tree.IElementType
import top.criwits.scaleda.verilog.VerilogLogger.Log

class VerilogASTFactory extends DefaultASTFactoryImpl {
  override def createLeaf(tpe: IElementType, text: CharSequence) = {
    Log.info(s"createLeaf(${tpe}, ${text})")
    VerilogPsiLeafNodeFactory.create(tpe, text)
  }
}
