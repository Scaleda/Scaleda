package top.criwits.scaleda
package verilog.editor.formatter

import verilog.psi.nodes.block.SeqBlockPsiNode
import verilog.psi.nodes.module.{ListOfPortDeclarationsPsiNode, ModuleDeclarationPsiNode, ModuleParameterPortListPsiNode}

import com.intellij.formatting._
import com.intellij.lang.ASTNode
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiWhiteSpace
import com.intellij.psi.codeStyle.CodeStyleSettings
import top.criwits.scaleda.verilog.psi.nodes.instantiation.ModuleInstancePsiNode

import java.util
import scala.collection.mutable.ListBuffer
import scala.jdk.CollectionConverters._

/** Verilog AST Block
  * @param parentBlock parent of current block
  * @param node corresponding AST node
  * @param alignment alignment
  * @param wrap wrap
  * @param indent indent
  * @param settings code style settings
  * @param spacingBuilder spacing builder
  */
class VerilogBlock(
    val parentBlock: Block,
    val node: ASTNode,
    val alignment: Alignment,
    val wrap: Wrap,
    val indent: Indent,
    val settings: CodeStyleSettings,
    val spacingBuilder: SpacingBuilder
) extends ASTBlock {
  private var subBlocks: ListBuffer[Block] = _
  private val currentPsi                   = node.getPsi
  private val childWrap = currentPsi match {
    case _: SeqBlockPsiNode => Wrap.createWrap(WrapType.ALWAYS, false)
    case _                  => null
  }

  private def childIndent(newChildIndex: Int): Indent = {
    val indent = currentPsi match {
      // Ignore head and tail
      case _: SeqBlockPsiNode | _: ListOfPortDeclarationsPsiNode | _: ModuleDeclarationPsiNode => {
        if (newChildIndex == 0 || newChildIndex == node.getChildren(null).length - 1) Indent.getNoneIndent
        else Indent.getNormalIndent
      }
      // Ignore head two and tail
      case _: ModuleParameterPortListPsiNode | _: ModuleInstancePsiNode => {
        if (newChildIndex == 0 || newChildIndex == 1 || newChildIndex == node.getChildren(null).length - 1)
          Indent.getNoneIndent
        else Indent.getNormalIndent
      }

      // For other block (unknown cases) use no indent
      case _ => Indent.getNoneIndent
    }
    indent
  }

  override def getNode: ASTNode = node

  override def getTextRange: TextRange = node.getTextRange

  override def getSubBlocks: util.List[Block] = {
    if (subBlocks == null) {
      subBlocks = new ListBuffer[Block]
      val children = node.getChildren(null)
      children.zipWithIndex.foreach { case (child, index) =>
        if (child.getPsi != null && !child.getPsi.isInstanceOf[PsiWhiteSpace] && child.getTextLength > 0) {
          subBlocks += makeSubBlock(child, index)
        }
      }
    }

    subBlocks.toList.asJava
  }

  private def makeSubBlock(childNode: ASTNode, index: Int): Block = {
    new VerilogBlock(
      this,
      childNode,
      null,
      childWrap,
      childIndent(index),
      settings,
      spacingBuilder
    )
  }

  override def getWrap: Wrap = wrap

  override def getIndent: Indent = indent

  override def getAlignment: Alignment = alignment

  override def getSpacing(child1: Block, child2: Block): Spacing = {
    spacingBuilder.getSpacing(this, child1, child2)
  }

  override def getChildAttributes(newChildIndex: Int): ChildAttributes = {
    new ChildAttributes(childIndent(newChildIndex), null)
  }

  override def isIncomplete: Boolean = false

  override def isLeaf: Boolean = {
    node.getFirstChildNode == null
  }
}
