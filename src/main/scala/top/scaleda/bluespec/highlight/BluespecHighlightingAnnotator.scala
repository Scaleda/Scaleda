package top.scaleda
package bluespec.highlight

import com.intellij.lang.annotation.{AnnotationHolder, Annotator, HighlightSeverity}
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.psi.PsiElement
import top.scaleda.bluespec.psi.nodes.module.ModuleIdentifierPsiNode

/** Dynamic, parser-based highlighter!
  */
class BluespecHighlightingAnnotator extends Annotator {
  override def annotate(element: PsiElement, holder: AnnotationHolder): Unit = {
    implicit val annotationHolder: AnnotationHolder = holder

    element match {
      // case _: DirectivePsiNode        => highlight(element, VerilogLexerSyntaxHighlighter.VERILOG_DIRECTIVE)
      case _: ModuleIdentifierPsiNode => highlight(element, BluespecLexerSyntaxHighlighter.BLUESPEC_MODULE_IDENTIFIER)
      // case _: VariableIdentifierPsiNode => highlight(element, VerilogLexerSyntaxHighlighter.VERILOG_SIGNAL_REG)
      case _ =>
    }
  }

  private def highlight(element: PsiElement, attribute: TextAttributesKey)(implicit
      holder: AnnotationHolder
  ): Unit = {
    holder
      .newSilentAnnotation(HighlightSeverity.INFORMATION)
      .range(element.getNode)
      .textAttributes(attribute)
      .create()
  }

}
