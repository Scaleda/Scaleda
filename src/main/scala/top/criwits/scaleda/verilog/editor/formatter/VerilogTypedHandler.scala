package top.criwits.scaleda
package verilog.editor.formatter

import com.intellij.codeInsight.editorActions.TypedHandlerDelegate
import com.intellij.codeInsight.editorActions.TypedHandlerDelegate.Result
import com.intellij.openapi.editor.{Document, Editor}
import com.intellij.openapi.project.Project
import com.intellij.psi.{PsiElement, PsiFile}
import top.criwits.scaleda.verilog.{VerilogFileType, VerilogPSIFileRoot}

class VerilogTypedHandler extends TypedHandlerDelegate {
  override def charTyped(c: Char, project: Project, editor: Editor, file: PsiFile): Result = {
    if (!file.isInstanceOf[VerilogPSIFileRoot]) return Result.CONTINUE

    val offset = editor.getCaretModel.getOffset
    val doc    = editor.getDocument
    val text   = doc.getImmutableCharSequence

    type Task = (Document, Project, PsiElement, Int) => Unit

    @inline
    def hasPrefix(prefix: String): Boolean =
      prefix.length <= offset && offset <= text.length &&
        text.subSequence(offset - prefix.length, offset) == prefix

    Result.CONTINUE
  }

}
