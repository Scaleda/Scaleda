package top.criwits.scaleda
package verilog.smart

import verilog.VerilogPSIFileRoot
import verilog.psi.nodes.module.ModuleDeclarationPsiNode

import com.intellij.codeInsight.editorActions.enter.EnterHandlerDelegate.Result
import com.intellij.codeInsight.editorActions.enter.{EnterHandlerDelegate, EnterHandlerDelegateAdapter}
import com.intellij.codeInsight.highlighting.{BraceMatcher, BraceMatchingUtil}
import com.intellij.lang.Language
import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.actionSystem.EditorActionHandler
import com.intellij.openapi.editor.highlighter.{EditorHighlighter, HighlighterIterator}
import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.util.Ref
import com.intellij.psi.tree.IElementType
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.{PsiDocumentManager, PsiElement, PsiFile}

/** ... begin <caret> [Enter] =>
  * ... begin
  *     <caret>
  * end
  */
class VerilogEnterBeginHandler extends EnterHandlerDelegateAdapter {
  override def preprocessEnter(
      file: PsiFile,
      editor: Editor,
      caretOffset: Ref[Integer],
      caretAdvance: Ref[Integer],
      dataContext: DataContext,
      originalHandler: EditorActionHandler
  ): EnterHandlerDelegate.Result = {
    if (!file.isInstanceOf[VerilogPSIFileRoot]) {
      return Result.Continue
    }

    // Update PSI First
    PsiDocumentManager.getInstance(file.getProject).commitDocument(editor.getDocument);

    val offset  = caretOffset.get.intValue
    val element = file.findElementAt(offset).getPrevSibling

    if (
      PsiTreeUtil.getParentOfType(element, classOf[ModuleDeclarationPsiNode]) != null && element
        .isInstanceOf[PsiElement] && element.textMatches("begin")
    ) {
      // now we need to check environment!
      if (getUnmatchedBeginsCount(editor, offset, file.getFileType) > 0) {
        // should insert `end`
        val document = editor.getDocument
        document.insertString(offset, "\nend")

        // TODO: format!
        return Result.DefaultForceIndent
      } else return Result.Continue
    }

    Result.Continue
  }

  /**
   * Magical code copied from IDEA source code, not know how it work, but it works
   * @param editor the [[Editor]]
   * @param offset the offset
   * @param fileType the [[FileType]]
   * @return
   */
  private def getUnmatchedBeginsCount(editor: Editor, offset: Int, fileType: FileType): Int = {
    if (offset == 0) return -1
    val chars = editor.getDocument.getCharsSequence
//    if (chars.charAt(offset - 1) != '{') return -1

    val highlighter = editor.getHighlighter
    var iterator = highlighter.createIterator(offset - 1)
    val braceMatcher = BraceMatchingUtil.getBraceMatcher(fileType, iterator)

    if (!braceMatcher.isLBraceToken(iterator, chars, fileType) || !braceMatcher.isStructuralBrace(iterator, chars, fileType)) return -1

    val language = iterator.getTokenType.getLanguage

    iterator = highlighter.createIterator(0)
    var lBracesBeforeOffset = 0
    var rBracesAfterOffset = 0

    while (
      !iterator.atEnd
    ) {
      val tokenType = iterator.getTokenType
      if (tokenType.getLanguage == language && braceMatcher.isStructuralBrace(iterator, chars, fileType)) {
        val beforeOffset = iterator.getStart < offset
        if (braceMatcher.isLBraceToken(iterator, chars, fileType)) if (beforeOffset) lBracesBeforeOffset += 1
        else rBracesAfterOffset -= 1
        else if (braceMatcher.isRBraceToken(iterator, chars, fileType)) if (beforeOffset) { // If there are more right braces then left - code before is invalid but let's not break the code after.
          if (lBracesBeforeOffset > 0) lBracesBeforeOffset -= 1
        }
        else {
          rBracesAfterOffset += 1
          if (rBracesAfterOffset == lBracesBeforeOffset) { // Do not calculate further. We've completed all scopes before cursor.
            return 0
          }
        }

      }
      iterator.advance()
    }

    return lBracesBeforeOffset - rBracesAfterOffset

  }

}
