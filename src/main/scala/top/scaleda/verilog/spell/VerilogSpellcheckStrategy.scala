package top.scaleda
package verilog.spell

import com.intellij.psi.{PsiElement, PsiNameIdentifierOwner}
import com.intellij.spellchecker.tokenizer.{SpellcheckingStrategy, Tokenizer}
import top.scaleda.verilog.VerilogLanguage

// TODO: not working
class VerilogSpellcheckStrategy extends SpellcheckingStrategy {
  override def getTokenizer(element: PsiElement): Tokenizer[_ <: PsiElement] = {
    val node = element.getNode
    if (node != null && VerilogLanguage.literalTokenSet.contains(node.getElementType)) return SpellcheckingStrategy.TEXT_TOKENIZER
    if (node.isInstanceOf[PsiNameIdentifierOwner]) return VerilogIdentifierOwnerTokenizer.INSTANCE
    SpellcheckingStrategy.EMPTY_TOKENIZER
  }
}
