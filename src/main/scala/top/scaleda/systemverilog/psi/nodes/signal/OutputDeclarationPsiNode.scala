package top.scaleda
package systemverilog.psi.nodes.signal

import systemverilog.psi.nodes.module.AbstractListOfPortIdentifiersPsiNode

import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode

import java.util

abstract class AbstractDeclarationPsiNode(node: ASTNode) extends ANTLRPsiNode(node) {
  def getTypeText: String = {
    val typeChildren: Array[PsiElement] =
      util.Arrays.copyOf(this.getChildren, this.getChildren.length - 1).filterNot(_.isInstanceOf[RangeValuePsiNode])
    typeChildren.map(_.getText).foldLeft("")(_ + " " + _).trim
  }

  def getListOfPortIdentifiers: Option[AbstractListOfPortIdentifiersPsiNode] = {
    Option(PsiTreeUtil.getChildOfType(this, classOf[AbstractListOfPortIdentifiersPsiNode]))
  }
}

class OutputDeclarationPsiNode(node: ASTNode) extends AbstractDeclarationPsiNode(node) // input_declaration ::= 'input' range list_of_port_declarations
class InputDeclarationPsiNode(node: ASTNode)  extends AbstractDeclarationPsiNode(node) // output_declaration ::= 'output' range list_of_port_declarations
class InoutDeclarationPsiNode(node: ASTNode)  extends AbstractDeclarationPsiNode(node) // inout_declaration ::= 'inout' range list_of_port_identifiers
