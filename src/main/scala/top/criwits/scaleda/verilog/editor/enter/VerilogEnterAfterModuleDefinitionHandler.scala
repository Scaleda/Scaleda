package top.criwits.scaleda
package verilog.editor.enter

import verilog.VerilogPSIFileRoot
import verilog.psi.nodes.module.{ModuleDeclarationPsiNode, ModuleHeadPsiNode}

import com.intellij.codeInsight.editorActions.enter.EnterHandlerDelegate.Result
import com.intellij.codeInsight.editorActions.enter.EnterHandlerDelegateAdapter
import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.actionSystem.EditorActionHandler
import com.intellij.openapi.util.Ref
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.{PsiDocumentManager, PsiElement, PsiFile}
import top.criwits.scaleda.verilog.editor.formatter.{VerilogLineIndentProvider, VerilogSemanticEditorPosition}

import scala.jdk.CollectionConverters._

//noinspection DuplicatedCode
class VerilogEnterAfterModuleDefinitionHandler extends EnterHandlerDelegateAdapter {
  override def preprocessEnter(
                                file: PsiFile,
                                editor: Editor,
                                caretOffset: Ref[Integer],
                                caretAdvance: Ref[Integer],
                                dataContext: DataContext,
                                originalHandler: EditorActionHandler
                              ): Result = {

    if (!file.isInstanceOf[VerilogPSIFileRoot]) return Result.Continue

    if (caretOffset.isNull) return Result.Continue
    val offset = caretOffset.get


    val pos = VerilogSemanticEditorPosition.createEditorPosition(editor, offset)
    pos.moveAtEndOfPreviousLine()
    val startOffset = pos.getStartOffset
    if (startOffset == -1) return Result.Continue


    // Update PSI
    PsiDocumentManager.getInstance(file.getProject).commitDocument(editor.getDocument)
    val currentElement = file.findElementAt(offset)
    if (currentElement != null) {
      val currentParent = PsiTreeUtil.getParentOfType(currentElement, classOf[ModuleHeadPsiNode])
      if (currentParent != null) return Result.Continue
    }


    val element = file.findElementAt(startOffset)
    if (element == null) return Result.Continue

    val moduleHead = PsiTreeUtil.getParentOfType(element, classOf[ModuleHeadPsiNode])
    if (moduleHead != null) {
      // find parent
      val moduleDec = PsiTreeUtil.getParentOfType(moduleHead, classOf[ModuleDeclarationPsiNode])
      if (moduleDec == null) return Result.Continue

      // check if has `endmodule`
      val elements = PsiTreeUtil.findChildrenOfType(moduleDec, classOf[PsiElement]).asScala
      elements.foreach(element => if (element.textMatches("endmodule")) return Result.Continue)

      // no endmodule, will make `endmodule`
      val indentStringForEndmodule = VerilogLineIndentProvider.getIndentString(editor, moduleHead.getTextRange.getStartOffset, 0)
      val document = editor.getDocument
      document.insertString(offset, "\n" + indentStringForEndmodule + "endmodule")
      return Result.DefaultForceIndent
    }


    Result.Continue
  }
}
