package top.criwits.scaleda
package verilog.structview

import verilog.VerilogPSIFileRoot

import com.intellij.ide.structureView.{StructureViewModel, StructureViewModelBase, StructureViewTreeElement}
import top.criwits.scaleda.verilog.psi.factory.nodes.module.ModuleDeclarationPsiNode

/** Structure view model of Verilog language.
  * @param root PSI File for the source file
  */
class VerilogStructViewModel(root: VerilogPSIFileRoot)
    extends StructureViewModelBase(root, new VerilogStructViewRootElement(root))
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
    element.getValue.isInstanceOf[VerilogPSIFileRoot] ||
      element.getValue.isInstanceOf[ModuleDeclarationPsiNode]

  override def isAlwaysLeaf(element: StructureViewTreeElement): Boolean = !isAlwaysShowsPlus(element)
}
