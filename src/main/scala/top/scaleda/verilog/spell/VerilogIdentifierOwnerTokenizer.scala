package top.scaleda
package verilog.spell

import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNameIdentifierOwner
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.spellchecker.inspections.PlainTextSplitter
import com.intellij.spellchecker.tokenizer.{TokenConsumer, Tokenizer}

object VerilogIdentifierOwnerTokenizer {
  val INSTANCE = new VerilogIdentifierOwnerTokenizer
}

class VerilogIdentifierOwnerTokenizer extends Tokenizer[PsiNameIdentifierOwner] {
  override def tokenize(element: PsiNameIdentifierOwner, consumer: TokenConsumer): Unit = {
    val identifier = element.getNameIdentifier
    if (identifier == null) return
    var parent: PsiElement = element
    val range = identifier.getTextRange
    if (range.isEmpty) return
    var offset = range.getStartOffset - parent.getTextRange.getStartOffset
    if (offset < 0) {
      parent = PsiTreeUtil.findCommonParent(identifier, element)
      offset = range.getStartOffset - parent.getTextRange.getStartOffset
    }
    val text = identifier.getText
    consumer.consumeToken(parent, text, true, offset, TextRange.allOf(text), PlainTextSplitter.getInstance)
  }
}