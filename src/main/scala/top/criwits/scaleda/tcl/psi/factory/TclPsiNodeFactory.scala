package top.criwits.scaleda
package tcl.psi.factory

import tcl.TclLanguage
import tcl.parser.TclParser
import verilog.psi.factory.nodes._
import verilog.references.ModuleInstantiationPsiNode

import com.intellij.lang.ASTNode
import com.intellij.psi.tree.IElementType
import org.antlr.intellij.adaptor.lexer.PSIElementTypeFactory
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode

import scala.collection.mutable

object TclPsiNodeFactory {
  private val LANGUAGE = TclLanguage
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

  try ruleIElementTypeClassMap.put(
    // TODO: check this
    getRuleIElementType(TclParser.RULE_indice),
    classOf[IdentifierPsiNode]
  )
  ruleIElementTypeClassMap.put(
    getRuleIElementType(TclParser.RULE_reg_declaration),
    classOf[RegDeclarationPsiNode]
  )
  ruleIElementTypeClassMap.put(
    getRuleIElementType(TclParser.RULE_net_declaration),
    classOf[NetDeclarationPsiNode]
  )
  ruleIElementTypeClassMap.put(
    getRuleIElementType(TclParser.RULE_port_identifier),
    classOf[PortIdentifierPsiNode]
  )
  ruleIElementTypeClassMap.put(
    getRuleIElementType(TclParser.RULE_net_identifier),
    classOf[NetIdentifierPsiNode]
  )
  ruleIElementTypeClassMap.put(
    getRuleIElementType(TclParser.RULE_module_identifier),
    classOf[ModuleIdentifierPsiNode]
  )
  ruleIElementTypeClassMap.put(
    getRuleIElementType(TclParser.RULE_module_instantiation),
    classOf[ModuleInstantiationPsiNode]
  )
  ruleIElementTypeClassMap.put(
    getRuleIElementType(TclParser.RULE_module_declaration),
    classOf[ModuleDeclarationPsiNode]
  )
  ruleIElementTypeClassMap.put(
    getRuleIElementType(TclParser.RULE_variable_identifier),
    classOf[VariableIdentifierPsiNode]
  )
  ruleIElementTypeClassMap.put(
    getRuleIElementType(TclParser.RULE_parameter_identifier),
    classOf[ParameterIdentifierPsiNode]
  )
  ruleIElementTypeClassMap.put(
    getRuleIElementType(TclParser.RULE_statement),
    classOf[StatementPsiNode]
  )
  ruleIElementTypeClassMap.put(
    getRuleIElementType(TclParser.RULE_named_port_connection),
    classOf[NamedPortConnectionPsiNode]
  )
  ruleIElementTypeClassMap.put(
    getRuleIElementType(TclParser.RULE_directive),
    classOf[DirectivePsiNode]
  )
  ruleIElementTypeClassMap.put(
    getRuleIElementType(TclParser.RULE_source_text),
    classOf[SourceTextPsiNode]
  )

}
