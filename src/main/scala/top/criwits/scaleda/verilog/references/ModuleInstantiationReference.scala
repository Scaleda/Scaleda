package top.criwits.scaleda
package verilog.references

import verilog.psi.factory.nodes.ModuleDeclarationPsiNode
import verilog.utils.FileUtils

import com.intellij.psi.{PsiElementResolveResult, PsiReferenceBase, ResolveResult}

class ModuleInstantiationReference(element: ModuleInstantiationPsiNode)
  extends PsiReferenceBase.Poly[ModuleInstantiationPsiNode](element, element.getHoldPsiNodeRelativeTextRange, true) {
  override def multiResolve(incompleteCode: Boolean): Array[ResolveResult] =
    FileUtils.getAllVerilogFiles(myElement.getProject)
      .flatMap(it => it.getAvailableNamedElementsScala)
      .filter(it => it.isInstanceOf[ModuleDeclarationPsiNode])
      .filter(x => x.getName == myElement.getHoldPsiNode.getName)
      .map(it => new PsiElementResolveResult(it))
      .toArray


  override def getVariants = Array()
}
