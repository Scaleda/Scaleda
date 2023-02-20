package top.criwits.scaleda
package verilog.formatter

import com.intellij.formatting.{Block, FormattingContext, FormattingModel, FormattingModelBuilder}
import com.intellij.lang.ASTNode
import com.intellij.psi.util.PsiUtilCore

class VerilogIndentBuilder extends FormattingModelBuilder {
  override def createModel(formattingContext: FormattingContext): FormattingModel = {
    val file = formattingContext.getContainingFile
    val settings = formattingContext.getCodeStyleSettings
    ???
  }

  def createBlock(context: VerilogFormattingContext, node: ASTNode): Block = {
//    val nodeType = PsiUtilCore.getElementType(node)
    ???
  }
}
