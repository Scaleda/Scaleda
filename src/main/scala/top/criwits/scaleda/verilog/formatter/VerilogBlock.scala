package top.criwits.scaleda
package verilog.formatter

import verilog.VerilogLanguage.getRuleType
import verilog.parser.{VerilogLexer, VerilogParser}

import com.intellij.formatting._
import com.intellij.lang.ASTNode
import com.intellij.openapi.util.TextRange
import com.intellij.psi.formatter.common.AbstractBlock
import top.criwits.scaleda.verilog.VerilogLanguage

import java.util
import scala.collection.mutable.ListBuffer
import scala.jdk.CollectionConverters._

class VerilogBlock(
    val parent: VerilogBlock,
    val node: ASTNode,
    val wrap: Wrap,
    val alignment: Alignment,
    val indent: Indent,
    val spacingBuilder: SpacingBuilder
) extends ASTBlock {

  private val psi = node.getPsi
  private val childWrap: Wrap = null
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
    node.getElementType == VerilogLanguage.getTokenType(VerilogLexer.White_space) || node.getTextLength == 0
  }

  private def makeSubBlock(node: ASTNode): Block = {
    ???
  }

  override def getWrap: Wrap = ???

  override def getIndent: Indent = ???

  override def getAlignment: Alignment = ???

  override def getChildAttributes(newChildIndex: Int): ChildAttributes = ???

  override def isIncomplete: Boolean = ???
}
