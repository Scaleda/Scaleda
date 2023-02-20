package top.criwits.scaleda
package verilog.smart

import verilog.VerilogPSIFileRoot

import com.intellij.codeInsight.editorActions.enter.{EnterAfterUnmatchedBraceHandler, EnterHandlerDelegate}
import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.actionSystem.EditorActionHandler
import com.intellij.openapi.util.Ref
import com.intellij.openapi.util.text.StringUtil
import com.intellij.psi.PsiFile

class VerilogEnterAfterUnmatchedBraceHandler extends EnterAfterUnmatchedBraceHandler {
  override def isApplicable(file: PsiFile, caretOffset: Int): Boolean = file.isInstanceOf[VerilogPSIFileRoot]

  override def preprocessEnter(
      file: PsiFile,
      editor: Editor,
      caretOffsetRef: Ref[Integer],
      caretAdvance: Ref[Integer],
      dataContext: DataContext,
      originalHandler: EditorActionHandler
  ): EnterHandlerDelegate.Result = {
    super.preprocessEnter(file, editor, caretOffsetRef, caretAdvance, dataContext, originalHandler)
  }

  override def generateStringToInsert(editor: Editor, caretOffset: Int, maxRBraceCount: Int): String = {
    assert(maxRBraceCount > 0)

    val text           = editor.getDocument.getCharsSequence
    var bracesToInsert = 0
    var i              = caretOffset - 1
    while (i >= 0 && bracesToInsert < maxRBraceCount) {
      val c = text.charAt(i)
      if (c == '{') bracesToInsert += 1
      else {
        if (isStopChar(c)) i = -1
        else
          i -= 1
      }
    }
    return StringUtil.repeat("end", Math.max(bracesToInsert, 1))

    super.generateStringToInsert(editor, caretOffset, maxRBraceCount)
  }
}
