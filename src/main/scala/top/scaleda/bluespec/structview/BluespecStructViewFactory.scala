package top.scaleda
package bluespec.structview

import com.intellij.ide.structureView.{StructureViewBuilder, StructureViewModel, TreeBasedStructureViewBuilder}
import com.intellij.lang.PsiStructureViewFactory
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiFile

/** Factory for structure view of System Verilog language, with EP `lang.psiStructureViewFactory` in `plugin.xml`.
  *
  * Factory overrides [[createStructureViewModel]] function and provide the [[BluespecStructViewModel]] class.
  */
class BluespecStructViewFactory extends PsiStructureViewFactory {
  override def getStructureViewBuilder(psiFile: PsiFile): StructureViewBuilder = new TreeBasedStructureViewBuilder {
    override def createStructureViewModel(editor: Editor): StructureViewModel = new BluespecStructViewModel(
      psiFile.asInstanceOf[bluespec.BluespecPSIFileRoot]
    )
  }
}
