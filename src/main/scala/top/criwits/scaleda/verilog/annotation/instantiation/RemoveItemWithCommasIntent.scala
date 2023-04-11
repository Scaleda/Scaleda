package top.criwits.scaleda
package verilog.annotation.instantiation

import com.intellij.codeInsight.intention.impl.BaseIntentionAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.{PsiElement, PsiFile}
import org.jetbrains.annotations.Nls


class RemoveItemWithCommasIntent[T <: PsiElement](element: T, @Nls name: String) extends BaseIntentionAction {
    override def getFamilyName: String = name

    override def getText: String = name

    override def isAvailable(project: Project, editor: Editor, file: PsiFile): Boolean = true

    override def invoke(project: Project, editor: Editor, file: PsiFile): Unit = {
      val prev = PsiTreeUtil.prevVisibleLeaf(element)
      val next = PsiTreeUtil.nextVisibleLeaf(element)
      val prevHas = prev != null && prev.textMatches(",")
      val nextHas = next != null && next.textMatches(",")

      val doc = editor.getDocument

      if (!prevHas && !nextHas) {
        doc.replaceString(element.getTextRange.getStartOffset, element.getTextRange.getEndOffset, "")
      } else if (prevHas && nextHas) {
        doc.replaceString(prev.getTextRange.getStartOffset, element.getTextRange.getEndOffset, "") // remove previous comma
      } else if (prevHas) {
        doc.replaceString(prev.getTextRange.getStartOffset, element.getTextRange.getEndOffset, "")
      } else if (nextHas) {
        doc.replaceString(element.getTextRange.getStartOffset, next.getTextRange.getEndOffset, "")
      }
    }
  }
