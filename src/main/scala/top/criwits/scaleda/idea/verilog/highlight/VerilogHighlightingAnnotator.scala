package top.criwits.scaleda
package idea.verilog.highlight

import verilog.psi.factory.nodes.DirectivePsiNode

import com.intellij.ide.highlighter.JavaHighlightingColors
import com.intellij.lang.annotation.{AnnotationHolder, Annotator, HighlightSeverity}
import com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey
import com.intellij.psi.PsiElement

class VerilogHighlightingAnnotator extends Annotator {
  override def annotate(element: PsiElement, holder: AnnotationHolder): Unit =
    if (element.isInstanceOf[DirectivePsiNode]) {
      // val annotation = holder.createInfoAnnotation(element.getNode, "")
      // annotation.setTextAttributes(VerilogHighlightingAnnotator.DIRECTIVE)
      holder.newAnnotation(HighlightSeverity.INFORMATION, "")
        .range(element.getNode)
        .textAttributes(VerilogHighlightingAnnotator.DIRECTIVE)
        .create()
    }
}

object VerilogHighlightingAnnotator {
  private final val DIRECTIVE = createTextAttributesKey(
    "VERILOG_DIRECTIVE",
    JavaHighlightingColors.ANNOTATION_NAME_ATTRIBUTES)
}