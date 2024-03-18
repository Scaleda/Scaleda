package top.scaleda
package systemverilog.psi.factory

import systemverilog.SystemVerilogLanguage
import systemverilog.parser.SystemVerilogParser

import com.intellij.lang.ASTNode
import com.intellij.psi.tree.IElementType
import org.antlr.intellij.adaptor.lexer.PSIElementTypeFactory
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode
import systemverilog.psi.factory.nodes.SourceTextPsiNode

import scala.collection.mutable

object SystemVerilogPsiNodeFactory {
  private val LANGUAGE = SystemVerilogLanguage
  private val ruleIElementTypeClassMap =
    new mutable.HashMap[IElementType, Class[_ <: ANTLRPsiNode]]

  private def getRuleIElementType(ruleIndex: Int) =
    PSIElementTypeFactory.getRuleIElementTypes(LANGUAGE).get(ruleIndex)

  def create(astNode: ASTNode): ANTLRPsiNode = {
    if (ruleIElementTypeClassMap.contains(astNode.getElementType))
      try {
        return ruleIElementTypeClassMap(astNode.getElementType)
          .getConstructor(classOf[ASTNode])
          .newInstance(astNode)
      } catch {
        case ignored: ReflectiveOperationException =>
      }
    new ANTLRPsiNode(astNode)
  }

  ruleIElementTypeClassMap.put(
    getRuleIElementType(SystemVerilogParser.RULE_source_text),
    classOf[SourceTextPsiNode]
  )
}
