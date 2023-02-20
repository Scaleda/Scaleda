package top.criwits.scaleda
package verilog.formatter

import com.intellij.formatting._
import com.intellij.lang.ASTNode
import com.intellij.psi.TokenType
import com.intellij.psi.formatter.common.AbstractBlock

import java.util
import scala.collection.mutable.ListBuffer
import scala.jdk.CollectionConverters._

class VerilogBlock(node: ASTNode, wrap: Wrap, alignment: Alignment, spacingBuilder: SpacingBuilder)
    extends AbstractBlock(node, wrap, alignment) {
  override def buildChildren(): util.List[Block] = {
    val blocks = new ListBuffer[Block]

    var child = node.getFirstChildNode
    while (child != null) {
      if (child.getElementType != TokenType.WHITE_SPACE) {
        blocks.addOne(
          new VerilogBlock(child, Wrap.createWrap(WrapType.NONE, false), Alignment.createAlignment(), spacingBuilder)
        )
      }
      child = child.getTreeNext
    }

    blocks.asJava
  }

  override def getSpacing(child1: Block, child2: Block): Spacing = ???

  override def isLeaf: Boolean = ???
}
