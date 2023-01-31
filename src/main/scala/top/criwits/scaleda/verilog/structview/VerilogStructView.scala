package top.criwits.scaleda
package verilog.structview

import com.intellij.ide.structureView.{StructureViewBuilder, StructureViewModel, TreeBasedStructureViewBuilder}
import com.intellij.lang.PsiStructureViewFactory
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiFile

class VerilogStructView extends PsiStructureViewFactory {
  override def getStructureViewBuilder(psiFile: PsiFile): StructureViewBuilder = new TreeBasedStructureViewBuilder {
    override def createStructureViewModel(editor: Editor): StructureViewModel = ???
  }
}
