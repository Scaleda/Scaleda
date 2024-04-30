// package top.scaleda
// package verilog.editor.formatter
//
// import verilog.VerilogLanguage
// import verilog.parser.VerilogLexer
//
// import com.intellij.formatting._
// import com.intellij.psi.codeStyle.CodeStyleSettings
//
// Deprecated method FormattingModelBuilder.createModel(...) is overridden
//
// final class VerilogFormattingModelBuilder extends FormattingModelBuilder {
//   override def createModel(formattingContext: FormattingContext): FormattingModel = {
//     val settings       = formattingContext.getCodeStyleSettings
//     val spacingBuilder = createSpacingBuilder(settings)
//
//     FormattingModelProvider.createFormattingModelForPsiFile(
//       formattingContext.getContainingFile,
//       new VerilogBlock(
//         null,
//         formattingContext.getNode,
//         null,
//         null,
//         Indent.getNoneIndent,
//         settings,
//         spacingBuilder
//       ),
//       settings
//     )
//   }
//
//   private def createSpacingBuilder(settings: CodeStyleSettings): SpacingBuilder = {
//     new SpacingBuilder(settings, VerilogLanguage)
//   }
// }
