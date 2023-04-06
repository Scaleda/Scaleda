package top.criwits.scaleda
package verilog.editor.formatter

import verilog.VerilogLanguage
import verilog.parser.VerilogLexer
import verilog.psi.nodes.block.SeqBlockPsiNode

import com.intellij.formatting._
import com.intellij.lang.ASTNode
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.formatter.common.AbstractBlock

import java.util

class VerilogBlock(
    val parentBlock: Block,
    val node: ASTNode,
    val alignment: Alignment,
    val wrap: Wrap,
    val indent: Indent,
    val settings: CodeStyleSettings,
    val spacingBuilder: SpacingBuilder
) extends AbstractBlock(node, wrap, alignment) {
  override def getIndent: Indent = indent

  override def getChildIndent: Indent = {
    val blockPsiNode = node.getPsi
    blockPsiNode match {
      case _: SeqBlockPsiNode => Indent.getNormalIndent
      case _                  => Indent.getNoneIndent
    }
  }

  override def buildChildren(): util.List[Block] = {
    val blocks    = new util.ArrayList[Block]
    var childNode = node.getFirstChildNode
    while (childNode != null) {
      if (childNode.getElementType != VerilogLanguage.getTokenType(VerilogLexer.White_space)) {
        val block = new VerilogBlock(
          this,
          childNode,
          null,
          null,
          getChildIndent,
          settings,
          spacingBuilder
        )
        blocks.add(block)
      }
      childNode = childNode.getTreeNext
    }

    blocks
  }

  override def getSpacing(child1: Block, child2: Block): Spacing = {
    spacingBuilder.getSpacing(this, child1, child2)
  }

  override def isLeaf: Boolean = {
    node.getFirstChildNode == null
  }
}
