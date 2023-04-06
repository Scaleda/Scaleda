package top.criwits.scaleda
package verilog.editor.enter

import verilog.{VerilogLanguage, VerilogPSIFileRoot}
import verilog.psi.nodes.module.ModuleDeclarationPsiNode

import com.intellij.codeInsight.editorActions.enter.EnterHandlerDelegate.Result
import com.intellij.codeInsight.editorActions.enter.{EnterHandlerDelegate, EnterHandlerDelegateAdapter}
import com.intellij.codeInsight.highlighting.BraceMatchingUtil
import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.actionSystem.EditorActionHandler
import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.util.Ref
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.{PsiDocumentManager, PsiElement, PsiFile}
import top.criwits.scaleda.verilog.editor.brace.VerilogBraceMatcherProvider
import top.criwits.scaleda.verilog.editor.formatter.{VerilogLineIndentProvider, VerilogSemanticEditorPosition}
import top.criwits.scaleda.verilog.parser.VerilogLexer

/** ... begin <caret> [Enter] =>
  * ... begin
  *     <caret>
  * end
  */
class VerilogEnterAfterBeginHandler extends EnterHandlerDelegateAdapter {

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

    //
//    println(file.asInstanceOf[VerilogPSIFileRoot].getInstantiatedModules.mkString("Array(", ", ", ")"))

    if (caretOffset.isNull) return Result.Continue
    val offset = caretOffset.get

    val pos = VerilogSemanticEditorPosition.createEditorPosition(editor, offset)
    pos.moveAtEndOfPreviousLine()

    if (pos.isAt(VerilogLanguage.getTokenType(VerilogLexer.K_begin))) {
      if (VerilogBraceMatcherProvider.getUnmatchedBracesCount(editor, offset, file.getFileType) > 0) {
        val indentStringForEnd = VerilogLineIndentProvider.getIndentString(editor, pos.getStartOffset, 0)
        val document = editor.getDocument
        document.insertString(offset, "\n" + indentStringForEnd + "end")
        return Result.DefaultForceIndent
      }

    }
    Result.Continue
  }


}
