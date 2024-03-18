package top.scaleda
package systemverilog.highlight

import com.intellij.lang.annotation.{AnnotationHolder, Annotator, HighlightSeverity}
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.psi.PsiElement

/** Dynamic, parser-based highlighter!
  */
class SystemVerilogHighlightingAnnotator extends Annotator {
  override def annotate(element: PsiElement, holder: AnnotationHolder): Unit = {
    implicit val annotationHolder: AnnotationHolder = holder

    element match {
      // case _: DirectivePsiNode        => highlight(element, VerilogLexerSyntaxHighlighter.VERILOG_DIRECTIVE)
      // case _: ModuleIdentifierPsiNode => highlight(element, VerilogLexerSyntaxHighlighter.VERILOG_MODULE_IDENTIFIER)
      // case n: PortIdentifierPsiNode =>
      //   n.getPortType match {
      //     case PortDeclarationPsiNode.INPUT  => highlight(element, VerilogLexerSyntaxHighlighter.VERILOG_PORT_INPUT)
      //     case PortDeclarationPsiNode.INOUT  => highlight(element, VerilogLexerSyntaxHighlighter.VERILOG_PORT_INOUT)
      //     case PortDeclarationPsiNode.OUTPUT => highlight(element, VerilogLexerSyntaxHighlighter.VERILOG_PORT_OUTPUT)
      //     case PortDeclarationPsiNode.OUTPUT_REG =>
      //       highlight(element, VerilogLexerSyntaxHighlighter.VERILOG_PORT_OUTPUT_REG)
      //     case _ =>
      //   }
      //
      // case _: NetIdentifierPsiNode      => highlight(element, VerilogLexerSyntaxHighlighter.VERILOG_SIGNAL_NET)
      // case _: VariableIdentifierPsiNode => highlight(element, VerilogLexerSyntaxHighlighter.VERILOG_SIGNAL_REG)
      //
      // case n: HierarchicalIdentifierPsiNode =>
      //   n.resolveReferenceIdentifier match {
      //     case _: NetIdentifierPsiNode =>
      //       highlight(element, VerilogLexerSyntaxHighlighter.VERILOG_SIGNAL_NET)
      //     case _: VariableIdentifierPsiNode =>
      //       highlight(element, VerilogLexerSyntaxHighlighter.VERILOG_SIGNAL_REG)
      //     case m: PortIdentifierPsiNode =>
      //       m.getPortType match {
      //         case PortDeclarationPsiNode.INPUT => highlight(element, VerilogLexerSyntaxHighlighter.VERILOG_PORT_INPUT)
      //         case PortDeclarationPsiNode.INOUT => highlight(element, VerilogLexerSyntaxHighlighter.VERILOG_PORT_INOUT)
      //         case PortDeclarationPsiNode.OUTPUT =>
      //           highlight(element, VerilogLexerSyntaxHighlighter.VERILOG_PORT_OUTPUT)
      //         case PortDeclarationPsiNode.OUTPUT_REG =>
      //           highlight(element, VerilogLexerSyntaxHighlighter.VERILOG_PORT_OUTPUT_REG)
      //         case _ =>
      //       }
      //     case _ =>
      //   }

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
