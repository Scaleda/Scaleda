package top.scaleda
package systemverilog.structview

import systemverilog.SystemVerilogPSIFileRoot

import com.intellij.ide.structureView.{StructureViewBuilder, StructureViewModel, TreeBasedStructureViewBuilder}
import com.intellij.lang.PsiStructureViewFactory
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiFile

/** Factory for structure view of System Verilog language, with EP `lang.psiStructureViewFactory` in `plugin.xml`.
  *
  * Factory overrides [[createStructureViewModel]] function and provide the [[SystemVerilogStructViewModel]] class.
  */
class SystemVerilogStructViewFactory extends PsiStructureViewFactory {
  override def getStructureViewBuilder(psiFile: PsiFile): StructureViewBuilder = new TreeBasedStructureViewBuilder {
    override def createStructureViewModel(editor: Editor): StructureViewModel = new SystemVerilogStructViewModel(
      psiFile.asInstanceOf[SystemVerilogPSIFileRoot]
    )
  }
}
