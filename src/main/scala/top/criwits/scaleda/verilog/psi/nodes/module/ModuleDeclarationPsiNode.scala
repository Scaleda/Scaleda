package top.criwits.scaleda
package verilog.psi.nodes.module

import verilog.parser.VerilogLexer

import top.criwits.scaleda.verilog.psi.nodes.{DocumentHolder, SimpleIdentifierPsiLeafNode, StructureViewNode}
import com.intellij.lang.ASTNode
import com.intellij.psi.{PsiElement, PsiNameIdentifierOwner}
import com.intellij.psi.util.PsiTreeUtil
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode
import org.jetbrains.annotations.Nls
import top.criwits.scaleda.verilog.VerilogPSIFileRoot
import top.criwits.scaleda.verilog.psi.VerilogPsiLeafNodeFactory
import top.criwits.scaleda.verilog.psi.nodes.signal.PortDeclarationPsiNode

import scala.jdk.CollectionConverters._

class ModuleDeclarationPsiNode(node: ASTNode)
  extends ANTLRPsiNode(node)
    with PsiNameIdentifierOwner
    with StructureViewNode
    with DocumentHolder
{
  override def getNameIdentifier: PsiElement = {
    val identifier = PsiTreeUtil.findChildOfAnyType(this, classOf[ModuleIdentifierPsiNode])
    identifier // FIXME: if null?
  }

  @Nls
  override def getName: String = getNameIdentifier match {
    case element: PsiElement => element.getText
    case null => "(unknown)"
  }


  override def setName(name: String): PsiElement = {
    val child = PsiTreeUtil.findChildOfType(this.getNameIdentifier, classOf[SimpleIdentifierPsiLeafNode])
    if (child == null) null else {
      child.replace(VerilogPsiLeafNodeFactory.create(VerilogLexer.Simple_identifier, name))
    }
  }

  override def getElementName: String = getName

  def getPorts: Array[PortDeclarationPsiNode] = {
    import scala.jdk.CollectionConverters._
    val list = PsiTreeUtil.findChildrenOfType(this, classOf[ListOfPortDeclarationsPsiNode]).asScala
    if (list.isEmpty) return Array.empty[PortDeclarationPsiNode]

    val result  =PsiTreeUtil.getChildrenOfType(list.head, classOf[PortDeclarationPsiNode])
    if (result == null) Array.empty[PortDeclarationPsiNode] else result
  }

  def getFile: VerilogPSIFileRoot = {
    val file = PsiTreeUtil.getParentOfType(this, classOf[VerilogPSIFileRoot])
    file
  }

  override def getDocument: String = ???
}
