package top.criwits.scaleda
package verilog.editor.formatter

import verilog.VerilogLanguage.getRuleType
import verilog.parser.{VerilogLexer, VerilogParser}

import com.intellij.formatting._
import com.intellij.lang.ASTNode
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiWhiteSpace
import com.intellij.psi.formatter.common.AbstractBlock
import top.criwits.scaleda.verilog.VerilogLanguage
import top.criwits.scaleda.verilog.psi.nodes.block.SeqBlockPsiNode

import java.util
import scala.collection.mutable.ListBuffer
import scala.jdk.CollectionConverters._

/**
 * [[Block]] for [[VerilogLanguage]]
 * @param parent Parent of this Block
 * @param node [[ASTNode]] representing this Block
 * @param wrap A [[Wrap]]
 * @param alignment An [[Alignment]]
 * @param indent An [[Indent]]
 * @param spacingBuilder A [[SpacingBuilder]]
 */
class VerilogBlock(
    val parent: VerilogBlock,
    val node: ASTNode,
    val wrap: Wrap,
    val alignment: Alignment,
    val indent: Indent,
    val spacingBuilder: SpacingBuilder
) extends ASTBlock {
  private val psi = node.getPsi
  private val childWrap: Wrap = psi match {
    case _ => null
  }
  private val childIndent: Indent = psi match {
    case _: SeqBlockPsiNode => Indent.getNormalIndent
    case _ => Indent.getNoneIndent
  }

  private var subBlocks: ListBuffer[Block] = null


  override def getSpacing(child1: Block, child2: Block): Spacing = spacingBuilder.getSpacing(this, child1, child2)
  override def isLeaf: Boolean = node.getFirstChildNode == null
  override def getNode: ASTNode = node
  override def getTextRange: TextRange = node.getTextRange

  override def getSubBlocks: util.List[Block] = {
    if (subBlocks == null) {
      subBlocks = ListBuffer.empty
      val children = node.getChildren(null)
      children.filter(!isWhiteSpaceOrEmpty(_)).foreach(child => subBlocks.addOne(makeSubBlock(child)))
    }
    subBlocks.asJava
  }

  private def isWhiteSpaceOrEmpty(node: ASTNode): Boolean = {
    node.getPsi.isInstanceOf[PsiWhiteSpace] || node.getElementType == VerilogLanguage.getTokenType(VerilogLexer.White_space) || node.getTextLength == 0
  }

  private def makeSubBlock(childNode: ASTNode): Block = {
    var childAlignment: Alignment = null
    new VerilogBlock(this, childNode, childWrap, childAlignment, childIndent, spacingBuilder)
  }

  override def getWrap: Wrap = wrap

  override def getIndent: Indent = indent

  override def getAlignment: Alignment = alignment

  override def getChildAttributes(newChildIndex: Int): ChildAttributes = new ChildAttributes(childIndent, null)

  override def isIncomplete: Boolean = false
}
