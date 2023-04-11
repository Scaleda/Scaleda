package top.criwits.scaleda
package verilog.references

import verilog.psi.nodes.instantiation.ModuleInstantiationPsiNode
import verilog.utils.FileUtils

import com.intellij.psi.{PsiElement, PsiReferenceBase}

class ModuleInstantiationReference(element: ModuleInstantiationPsiNode)
    extends PsiReferenceBase[ModuleInstantiationPsiNode](element, element.getHoldPsiNodeRelativeTextRange) {

  override def resolve: PsiElement =
    FileUtils
      .getAllVerilogFiles(myElement.getProject)
      .flatMap(it => it.getModuleDeclarations)
      .find(x => (x.getNameIdentifier != null) && x.getNameIdentifier.textMatches(myElement.getHoldPsiNode.getName))
      .orNull

}
