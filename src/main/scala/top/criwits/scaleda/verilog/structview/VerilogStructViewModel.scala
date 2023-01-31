package top.criwits.scaleda
package verilog.structview

import verilog.VerilogPSIFileRoot

import com.intellij.ide.structureView.{StructureViewModel, StructureViewModelBase, StructureViewTreeElement}

class VerilogStructViewModel(root: VerilogPSIFileRoot)
    extends StructureViewModelBase(root, ???)
    with StructureViewModel.ElementInfoProvider {
  override def isAlwaysShowsPlus(element: StructureViewTreeElement): Boolean = ???

  override def isAlwaysLeaf(element: StructureViewTreeElement): Boolean = ???
}
