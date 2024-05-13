package top.scaleda
package tcl.psi.factory

import tcl.TclLanguage
import tcl.parser.TclParser
import tcl.psi.factory.nodes.{IdentifierPsiNode, SourceTextPsiNode}

import com.intellij.lang.ASTNode
import com.intellij.psi.tree.IElementType
import org.antlr.intellij.adaptor.lexer.PSIElementTypeFactory
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode

import scala.collection.mutable

object TclPsiNodeFactory {
  private val LANGUAGE = TclLanguage.INSTANCE
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

  // ruleIElementTypeClassMap.put(
  //   // TODO: check this
  //   getRuleIElementType(TclParser.RULE_declaracion),
  //   classOf[IdentifierPsiNode]
  // )
  ruleIElementTypeClassMap.put(
    getRuleIElementType(TclParser.RULE_source_text),
    classOf[SourceTextPsiNode]
  )
}
