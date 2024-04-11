package top.scaleda
package systemverilog.structview

import systemverilog.SystemVerilogPSIFileRoot
import systemverilog.psi.nodes.module.ModuleDeclarationPsiNode

import com.intellij.ide.structureView.{StructureViewModel, StructureViewModelBase, StructureViewTreeElement}

/** Structure view model of Verilog language.
  * @param root PSI File for the source file
  */
class SystemVerilogStructViewModel(root: SystemVerilogPSIFileRoot)
    extends StructureViewModelBase(root, new SystemVerilogStructViewRootElement(root))
    with StructureViewModel.ElementInfoProvider {

  /** For those PSI nodes they can show plus (+) symbol for they contain further elements.
    *
    *  - [[VerilogPSIFileRoot]] -- File root
    *  - [[ModuleDeclarationPsiNode]] -- Module declaration
    *
    * @param element Tree element
    * @return
    */
  override def isAlwaysShowsPlus(element: StructureViewTreeElement): Boolean =
    element.getValue.isInstanceOf[SystemVerilogPSIFileRoot] ||
      element.getValue.isInstanceOf[ModuleDeclarationPsiNode]

  override def isAlwaysLeaf(element: StructureViewTreeElement): Boolean = !isAlwaysShowsPlus(element)
}
