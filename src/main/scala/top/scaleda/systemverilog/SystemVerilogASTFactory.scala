package top.scaleda
package systemverilog

import com.intellij.lang.DefaultASTFactoryImpl
import com.intellij.psi.tree.IElementType
import top.scaleda.systemverilog.psi.SystemVerilogPsiLeafNodeFactory

class SystemVerilogASTFactory extends DefaultASTFactoryImpl {
  override def createLeaf(tpe: IElementType, text: CharSequence) = {
    SystemVerilogLogger.Log.info(s"createLeaf($tpe, $text)")
    SystemVerilogPsiLeafNodeFactory.create(tpe, text)
  }
}
