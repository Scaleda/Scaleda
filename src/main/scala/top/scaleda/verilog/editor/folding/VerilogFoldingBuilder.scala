package top.scaleda
package verilog.editor.folding

import verilog.VerilogPSIFileRoot
import verilog.editor.folding.VerilogFoldingBuilder.folding
import verilog.parser.VerilogParser
import verilog.psi.nodes.block.{CaseBodyPsiNode, CaseStatementPsiNode, SeqBlockPsiNode}
import verilog.psi.nodes.module.{ListOfPortDeclarationsPsiNode, ModuleDeclarationPsiNode}

import com.intellij.lang.ASTNode
import com.intellij.lang.folding.{CustomFoldingBuilder, FoldingDescriptor}
import com.intellij.openapi.editor.Document
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import org.antlr.intellij.adaptor.lexer.RuleIElementType

import java.util
import scala.jdk.CollectionConverters._

class VerilogFoldingBuilder extends CustomFoldingBuilder {
  override def buildLanguageFoldRegions(
      descriptors: util.List[FoldingDescriptor],
      root: PsiElement,
      document: Document,
      quick: Boolean
  ): Unit = {
    if (!root.isInstanceOf[VerilogPSIFileRoot]) return
    folding.foreach(fold => {
      val elements = PsiTreeUtil.findChildrenOfAnyType(root, fold._2).asScala
      fold._3(descriptors, elements, document)
    })
  }

  override def getLanguagePlaceholderText(node: ASTNode, range: TextRange): String = {
    val nodeType = node.getElementType
    if (!nodeType.isInstanceOf[RuleIElementType]) return "..."

    folding.foreach(fold => {
      if (fold._1 == nodeType.asInstanceOf[RuleIElementType].getRuleIndex) {
        return fold._4(node)
      }
    })

    "..."
  }

  override def isRegionCollapsedByDefault(node: ASTNode): Boolean = false
}

object VerilogFoldingBuilder {
  val folding: Array[
    (
        Int,
        Class[_ <: PsiElement],
        (util.List[FoldingDescriptor], Iterable[PsiElement], Document) => Unit,
        ASTNode => String
    )
  ] = Array(
    (VerilogParser.RULE_seq_block, classOf[SeqBlockPsiNode], foldBlock, (_: ASTNode) => "begin...end"),
    (
      VerilogParser.RULE_module_declaration,
      classOf[ModuleDeclarationPsiNode],
      foldBlock,
      (node: ASTNode) => {
        f"module ${node.getPsi.asInstanceOf[ModuleDeclarationPsiNode].getNameIdentifier.getText} ...endmodule"
      }
    ),
    (
      VerilogParser.RULE_list_of_port_declarations,
      classOf[ListOfPortDeclarationsPsiNode],
      foldBlock,
      (_: ASTNode) => "(...)"
    ),
    (
      VerilogParser.RULE_case_body, classOf[CaseBodyPsiNode], foldBlock,  (_: ASTNode) => "..."
    ),
    (
      VerilogParser.RULE_function_case_body, classOf[CaseBodyPsiNode], foldBlock,  (_: ASTNode) => "..."
    ),
    (
      VerilogParser.RULE_generate_case_body, classOf[CaseBodyPsiNode], foldBlock,  (_: ASTNode) => "..."
    ),

//    (
//      VerilogParser.RULE_function_case_statement, classOf[CaseStatementPsiNode], foldBlock, (node: ASTNode) => f"case (${node.getPsi.asInstanceOf[CaseStatementPsiNode].getExpression match {
//      case Some(expression) => expression.getText
//      case None => "???"
//    }}) ...endcase"
//    ),
//    (
//      VerilogParser.RULE_generate_case_statement, classOf[CaseStatementPsiNode], foldBlock, (node: ASTNode) => f"case (${node.getPsi.asInstanceOf[CaseStatementPsiNode].getExpression match {
//      case Some(expression) => expression.getText
//      case None => "???"
//    }}) ...endcase"
//    ),


    // TODO: Add more!
  )

  private def foldBlock(
      descriptors: util.List[FoldingDescriptor],
      elements: Iterable[PsiElement],
      document: Document
  ): Unit = {
    elements.foreach(element => {
      if (element != null && element.getTextLength > 0) {
        descriptors.add(new FoldingDescriptor(element, element.getTextRange))
      }
    })
  }
}
