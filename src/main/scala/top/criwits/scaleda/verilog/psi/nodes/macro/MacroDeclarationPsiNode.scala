package top.criwits.scaleda
package verilog.psi.nodes.`macro`

import verilog.parser.VerilogLexer
import verilog.psi.VerilogPsiLeafNodeFactory
import verilog.psi.nodes.{SimpleIdentifierPsiLeafNode, StructureViewNode}

import com.intellij.lang.ASTNode
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.{PsiElement, PsiNameIdentifierOwner}
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode
import org.jetbrains.annotations.Nls

/**
  * Macro declaration node.<br/>
  * define_directive: 'define' create_defined_flag create_defined_term ;
  */
class MacroDeclarationPsiNode(node: ASTNode)
    extends ANTLRPsiNode(node)
    with PsiNameIdentifierOwner
    with StructureViewNode {
  override def getNameIdentifier: PsiElement = {
    val identifier = PsiTreeUtil.findChildOfAnyType(this, classOf[MacroIdentifierPsiNode])
    identifier // FIXME: if null?
  }

  @Nls
  override def getName: String = getNameIdentifier match {
    case element: PsiElement => element.getText
    case null                => "(unknown)"
  }

  override def setName(name: String): PsiElement = {
    val child = PsiTreeUtil.findChildOfType(this.getNameIdentifier, classOf[SimpleIdentifierPsiLeafNode])
    if (child == null) null
    else {
      child.replace(VerilogPsiLeafNodeFactory.create(VerilogLexer.Simple_identifier, name))
    }
  }

  override def getElementName: String = getName
}
