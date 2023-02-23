package top.criwits.scaleda
package verilog.formatter

import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.highlighter.HighlighterIterator
import com.intellij.psi.tree.IElementType
import org.jetbrains.annotations.NotNull

/**
 * Copied from IDEA Shell plugin
 */
object VerilogSemanticEditorPosition {
  def createEditorPosition(editor: Editor, offset: Int) = new VerilogSemanticEditorPosition(editor, offset)
}

final class VerilogSemanticEditorPosition private(val editor: Editor, val offset: Int) {
  final private val myIterator = editor.getHighlighter.createIterator(offset)

  def moveBefore(): Unit = {
    if (!myIterator.atEnd) myIterator.retreat()
  }

  def moveBeforeOptionalMix(elements: IElementType*): Unit = {
    while (isAtAnyOf(elements: _*)) myIterator.retreat()
  }

  def moveAfterOptionalMix(elements: IElementType*): Unit = {
    while (isAtAnyOf(elements: _*)) myIterator.advance()
  }

  def isAtAnyOf(syntaxElements: IElementType*): Boolean = {
    if (!myIterator.atEnd) {
      val currElement = myIterator.getTokenType
      for (element <- syntaxElements) {
        if (element == currElement) return true
      }
    }
    false
  }

  def isAt(elementType: IElementType): Boolean = !myIterator.atEnd && (myIterator.getTokenType == elementType)

  def getStartOffset: Int = if (!myIterator.atEnd) myIterator.getStart else -1
}

