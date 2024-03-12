package top.scaleda
package verilog.annotation.misc

import idea.ScaledaBundle
import verilog.psi.nodes.block.SeqBlockPsiNode

import com.intellij.lang.annotation.{AnnotationHolder, Annotator, HighlightSeverity}
import com.intellij.psi.PsiElement

class IncompleteSeqBlockAnnotator extends Annotator {
  override def annotate(element: PsiElement, holder: AnnotationHolder): Unit = {
    if (!element.isInstanceOf[SeqBlockPsiNode]) return
    val seqBlock = element.asInstanceOf[SeqBlockPsiNode]
    if (seqBlock == null) return

    if (!seqBlock.getLastChild.textMatches("end")) {
        holder.newAnnotation(HighlightSeverity.ERROR, ScaledaBundle.message("annotation.incomplete.seqblock"))
            .range(seqBlock.getTextRange)
            .create()
    }

  }
}
