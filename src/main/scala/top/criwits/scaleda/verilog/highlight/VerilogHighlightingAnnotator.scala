package top.criwits.scaleda
package verilog.highlight

import verilog.psi.nodes.DirectivePsiNode

import com.intellij.ide.highlighter.JavaHighlightingColors
import com.intellij.lang.annotation.{AnnotationHolder, Annotator, HighlightSeverity}
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey
import com.intellij.psi.PsiElement
import top.criwits.scaleda.verilog.psi.nodes.module.ModuleIdentifierPsiNode
import top.criwits.scaleda.verilog.psi.nodes.signal.{NetIdentifierPsiNode, VariableIdentifierPsiNode}

/** Dynamic, parser-based highlighter!
  */
class VerilogHighlightingAnnotator extends Annotator {
  override def annotate(element: PsiElement, holder: AnnotationHolder): Unit = {
    implicit val annotationHolder: AnnotationHolder = holder

    element match {
      case _: DirectivePsiNode => highlight(element, VerilogLexerSyntaxHighlighter.VERILOG_DIRECTIVE)
      case _: ModuleIdentifierPsiNode => highlight(element, VerilogLexerSyntaxHighlighter.VERILOG_MODULE_IDENTIFIER)
      case _: NetIdentifierPsiNode => highlight(element, VerilogLexerSyntaxHighlighter.VERILOG_SIGNAL_NET)
      case _: VariableIdentifierPsiNode => highlight(element, VerilogLexerSyntaxHighlighter.VERILOG_SIGNAL_REG)
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
