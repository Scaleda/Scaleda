package top.criwits.scaleda
package verilog.references

import com.intellij.lang.cacheBuilder.{DefaultWordsScanner, WordsScanner}
import com.intellij.lang.findUsages.FindUsagesProvider
import com.intellij.psi.{PsiElement, PsiNamedElement}
import com.intellij.psi.tree.TokenSet
import org.antlr.intellij.adaptor.lexer.ANTLRLexerAdaptor
import top.criwits.scaleda.verilog.VerilogLanguage
import top.criwits.scaleda.verilog.parser.VerilogLexer
import top.criwits.scaleda.verilog.psi.nodes.IdentifierPsiNode
import top.criwits.scaleda.verilog.psi.nodes.module.ModuleIdentifierPsiNode
import top.criwits.scaleda.verilog.psi.nodes.signal.SignalIdentifierPsiNode

class VerilogFindUsagesProvider extends FindUsagesProvider {
  override def canFindUsagesFor(psiElement: PsiElement): Boolean = {
    psiElement.isInstanceOf[IdentifierPsiNode]
  }

  override def getWordsScanner: WordsScanner = new DefaultWordsScanner(
    new ANTLRLexerAdaptor(VerilogLanguage, new VerilogLexer(null)),
    TokenSet.create(VerilogLanguage.getTokenType(VerilogLexer.Simple_identifier)),
    TokenSet.create(VerilogLanguage.getTokenType(VerilogLexer.Block_comment), VerilogLanguage.getTokenType(VerilogLexer.One_line_comment)),
    TokenSet.create(
      VerilogLanguage.getTokenType(VerilogLexer.Real_number),
      VerilogLanguage.getTokenType(VerilogLexer.Decimal_number),
      VerilogLanguage.getTokenType(VerilogLexer.Binary_number),
      VerilogLanguage.getTokenType(VerilogLexer.Octal_number),
      VerilogLanguage.getTokenType(VerilogLexer.Hex_number),
      VerilogLanguage.getTokenType(VerilogLexer.String)
    )
  )

  override def getHelpId(psiElement: PsiElement): String = null

  override def getType(element: PsiElement): String = "一眼丁真" // FIXME

  override def getDescriptiveName(element: PsiElement): String = element.getText

  override def getNodeText(element: PsiElement, useFullName: Boolean): String = element.getText
}
