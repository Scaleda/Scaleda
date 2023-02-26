package top.criwits.scaleda
package verilog.references

import com.intellij.psi.{PsiElementResolveResult, PsiReferenceBase, ResolveResult}
import top.criwits.scaleda.verilog.psi.nodes.instantiation.ModuleInstantiationPsiNode
import top.criwits.scaleda.verilog.psi.nodes.module.ModuleDeclarationPsiNode
import top.criwits.scaleda.verilog.utils.FileUtils

class ModuleInstantiationReference(element: ModuleInstantiationPsiNode)
  extends PsiReferenceBase.Poly[ModuleInstantiationPsiNode](element, element.getHoldPsiNodeRelativeTextRange, true) {
  override def multiResolve(incompleteCode: Boolean): Array[ResolveResult] =
    FileUtils.getAllVerilogFiles(myElement.getProject)
      .flatMap(it => it.getModuleDeclarations)
      .filter(x => (x.getNameIdentifier != null) && x.getNameIdentifier.textMatches(myElement.getHoldPsiNode.getName))
      .map(it => new PsiElementResolveResult(it))
      .toArray


  override def getVariants = Array()
}
