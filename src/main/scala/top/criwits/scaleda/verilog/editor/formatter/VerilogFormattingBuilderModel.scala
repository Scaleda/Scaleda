package top.criwits.scaleda
package verilog.editor.formatter

import verilog.VerilogLanguage
import verilog.parser.VerilogLexer

import com.intellij.formatting._
import com.intellij.psi.codeStyle.CodeStyleSettings

class VerilogFormattingBuilderModel extends FormattingModelBuilder {
  override def createModel(formattingContext: FormattingContext): FormattingModel = {
    val settings       = formattingContext.getCodeStyleSettings
    val spacingBuilder = createSpacingBuilder(settings)

    FormattingModelProvider.createFormattingModelForPsiFile(
      formattingContext.getContainingFile,
      new VerilogBlock(
        null,
        formattingContext.getNode,
        null,
        null,
        Indent.getSmartIndent(Indent.Type.CONTINUATION),
        spacingBuilder
      ),
      settings
    )
  }

  def createSpacingBuilder(settings: CodeStyleSettings): SpacingBuilder = {
    val commonSettings = settings.getCommonSettings(VerilogLanguage);

    val spacingBeforeComma = 0;
    val spacingBeforeColon = 0;
    val spacingAfterColon  = 1;

    new SpacingBuilder(settings, VerilogLanguage)
//      .before(VerilogLanguage.getTokenType(VerilogLexer.Colon))
//      .spacing(spacingBeforeColon, spacingBeforeColon, 0, false, 0)
//      .after(VerilogLanguage.getTokenType(VerilogLexer.Colon))
//      .spacing(spacingAfterColon, spacingAfterColon, 0, false, 0)
    // TODO: Add more!
  }
}
