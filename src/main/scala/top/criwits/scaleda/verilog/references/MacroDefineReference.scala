package top.criwits.scaleda
package verilog.references

import verilog.psi.nodes.instantiation.MacroUsePsiNode
import verilog.utils.FileUtils

import com.intellij.psi.{PsiElementResolveResult, PsiReferenceBase, ResolveResult}

class MacroDefineReference(element: MacroUsePsiNode)
    extends PsiReferenceBase.Poly[MacroUsePsiNode](element, element.getHoldPsiNodeRelativeTextRange, true) {
  override def multiResolve(incompleteCode: Boolean): Array[ResolveResult] =
    FileUtils
      .getAllVerilogFiles(myElement.getProject)
      .flatMap(it => it.getMacroDeclarations)
      .filter(x => (x.getNameIdentifier != null) && x.getNameIdentifier.textMatches(myElement.getHoldPsiNode.getName))
      .map(it => new PsiElementResolveResult(it))
      .toArray

  override def getVariants = Array()
}
