package top.scaleda
package bluespec

import com.intellij.lang.DefaultASTFactoryImpl
import com.intellij.psi.tree.IElementType

class BluespecASTFactory extends DefaultASTFactoryImpl {
  override def createLeaf(tpe: IElementType, text: CharSequence) = {
    BluespecLogger.Log.info(s"createLeaf($tpe, $text)")
    psi.BluespecPsiLeafNodeFactory.create(tpe, text)
  }
}
