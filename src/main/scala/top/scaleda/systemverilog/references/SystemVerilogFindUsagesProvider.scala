package top.scaleda
package systemverilog.references

import systemverilog.psi.nodes.module.ModuleIdentifierPsiNode
import systemverilog.psi.nodes.signal.SignalIdentifierPsiNode

import com.intellij.lang.cacheBuilder.WordsScanner
import com.intellij.lang.findUsages.FindUsagesProvider
import com.intellij.psi.PsiElement

class SystemVerilogFindUsagesProvider extends FindUsagesProvider {
  override def canFindUsagesFor(psiElement: PsiElement): Boolean = {
    psiElement.isInstanceOf[SignalIdentifierPsiNode] ||
      psiElement.isInstanceOf[ModuleIdentifierPsiNode]
  }

  override def getWordsScanner: WordsScanner = null

  override def getHelpId(psiElement: PsiElement): String = null

  override def getType(element: PsiElement): String = {
    if (element.isInstanceOf[SignalIdentifierPsiNode]) {
      "Signal"
    } else if (element.isInstanceOf[ModuleIdentifierPsiNode]) {
      "Module"
    } else {
      "Unknown"
    }
  }

  override def getDescriptiveName(element: PsiElement): String = element.getText

  override def getNodeText(element: PsiElement, useFullName: Boolean): String = element.getText
}
