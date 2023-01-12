package top.criwits.scaleda
package tcl

import tcl.psi.factory.nodes.TclPsiLeafNodeFactory

import com.intellij.lang.DefaultASTFactoryImpl
import com.intellij.psi.tree.IElementType

class TclASTFactory extends DefaultASTFactoryImpl {
  override def createLeaf(tpe: IElementType, text: CharSequence) = {
    TclLogger.Log.info(s"createLeaf($tpe, $text)")
    TclPsiLeafNodeFactory.create(tpe, text)
  }
}
