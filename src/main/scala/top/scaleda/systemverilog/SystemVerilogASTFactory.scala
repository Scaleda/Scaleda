package top.scaleda
package systemverilog

import systemverilog.psi.factory.nodes.SystemVerilogPsiLeafNodeFactory
import com.intellij.lang.DefaultASTFactoryImpl
import com.intellij.psi.tree.IElementType

class SystemVerilogASTFactory extends DefaultASTFactoryImpl {
  override def createLeaf(tpe: IElementType, text: CharSequence) = {
    SystemVerilogLogger.Log.info(s"createLeaf($tpe, $text)")
    SystemVerilogPsiLeafNodeFactory.create(tpe, text)
  }
}
