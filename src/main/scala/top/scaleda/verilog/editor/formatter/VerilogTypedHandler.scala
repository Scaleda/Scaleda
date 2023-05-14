package top.scaleda
package verilog.editor.formatter

import verilog.parser.VerilogLexer
import verilog.psi.nodes.block.SeqBlockPsiNode
import verilog.{VerilogLanguage, VerilogPSIFileRoot}

import com.intellij.codeInsight.editorActions.TypedHandlerDelegate
import com.intellij.codeInsight.editorActions.TypedHandlerDelegate.Result
import com.intellij.openapi.editor.{Document, Editor}
import com.intellij.openapi.project.Project
import com.intellij.psi.tree.IElementType
import com.intellij.psi.{PsiElement, PsiFile}

import scala.reflect.{ClassTag, classTag}

/**
 * @todo not working
 */
class VerilogTypedHandler extends TypedHandlerDelegate {
  override def charTyped(c: Char, project: Project, editor: Editor, file: PsiFile): Result = {
    if (!file.isInstanceOf[VerilogPSIFileRoot]) return Result.CONTINUE

    val offset = editor.getCaretModel.getOffset
    val doc    = editor.getDocument
    val text   = doc.getImmutableCharSequence

    type Task = (Document, Project, PsiElement, Int) => Unit


    @inline
    def hasPrefix(prefix: String): Boolean = {
      prefix.length <= offset && offset <= text.length &&
        text.subSequence(offset - prefix.length, offset) == prefix
    }

    def indentKeyword[T <: PsiElement: ClassTag](
        file: PsiFile,
        keywordType: IElementType
    )(document: Document, project: Project, element: PsiElement, offset: Int): Unit = {
      VerilogLineIndentProvider.indentElement(file)(document, project, element, offset)(element =>
        element.getNode.getElementType == keywordType && classTag[T].runtimeClass.isInstance(element)
      )

    }

    val task = if (c == 'd' && hasPrefix("end")) {
      indentKeyword[SeqBlockPsiNode](file, VerilogLanguage.getTokenType(VerilogLexer.K_end)) _
    } else null

    if (task == null) {
      Result.CONTINUE
    } else {
      task(doc, project, file.findElementAt(offset - 1), offset)
      Result.STOP
    }

  }

}
