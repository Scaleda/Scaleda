package top.scaleda
package bluespec.structview

import bluespec.psi.nodes.module

import com.intellij.ide.structureView.{StructureViewModel, StructureViewModelBase, StructureViewTreeElement}

/** Structure view model of System Verilog language.
  * @param root PSI File for the source file
  */
class BluespecStructViewModel(root: bluespec.BluespecPSIFileRoot)
    extends StructureViewModelBase(root, new BluespecStructViewRootElement(root))
    with StructureViewModel.ElementInfoProvider {

  /** For those PSI nodes they can show plus (+) symbol for they contain further elements.
    *
    *  - [[bluespec.BluespecPSIFileRoot]] -- File root
    *  - [[module.ModuleDeclarationPsiNode]] -- Module declaration
    *
    * @param element Tree element
    * @return
    */
  override def isAlwaysShowsPlus(element: StructureViewTreeElement): Boolean =
    element.getValue match {
      case _: bluespec.BluespecPSIFileRoot => true
      case _: module.ModuleDeclarationPsiNode => true
      // case _: clazz.ClassDeclarationPsiNode  => true
      case _                           => false
    }

  override def isAlwaysLeaf(element: StructureViewTreeElement): Boolean = !isAlwaysShowsPlus(element)
}
