package top.scaleda
package systemverilog

import tcl.psi.factory.nodes.TclPsiLeafNodeFactory
import com.intellij.lang.DefaultASTFactoryImpl
import com.intellij.psi.tree.IElementType
import top.scaleda.tcl.psi.factory.nodes.TclPsiLeafNodeFactory

class SystemVerilogASTFactory extends DefaultASTFactoryImpl {
  override def createLeaf(tpe: IElementType, text: CharSequence) = {
    TclLogger.Log.info(s"createLeaf($tpe, $text)")
    TclPsiLeafNodeFactory.create(tpe, text)
  }
}
