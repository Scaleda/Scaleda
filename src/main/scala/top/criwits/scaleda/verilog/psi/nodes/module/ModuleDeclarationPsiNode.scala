package top.criwits.scaleda
package verilog.psi.nodes.module

import verilog.parser.VerilogLexer

import top.criwits.scaleda.verilog.psi.nodes.{SimpleIdentifierPsiLeafNode, StructureViewNode}
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
    /* with ScopeNode */ {
//
//    override fun getNameIdentifier(): ModuleIdentifierPsiNode? {
//        return PsiTreeUtil
//            .findChildOfType(this, ModuleIdentifierPsiNode::class.java)
//    }
//
//    override fun getName(): String? {
//        return nameIdentifier?.text
//    }
//
//    override fun getTextOffset(): Int {
//        return nameIdentifier?.startOffsetInParent ?: 0
//    }
//
//    @Throws(IncorrectOperationException::class)
//    override fun setName(s: String): PsiElement? {
//        return PsiTreeUtil
//            .findChildOfType(this.nameIdentifier, SimpleIdentifierPsiLeafNode::class.java)
//            ?.replace(VerilogPsiLeafNodeFactory.create(VerilogLexer.Simple_identifier, s))
//    }

//    override fun getAvailableNamedElements(): Collection<PsiNamedElement> {
//        return listOf(
//            PsiTreeUtil.findChildrenOfType(this, NetIdentifierPsiNode::class.java),
//            PsiTreeUtil.findChildrenOfType(this, ParameterIdentifierPsiNode::class.java),
//            PsiTreeUtil.findChildrenOfType(this, PortIdentifierPsiNode::class.java)
//                .filter {
//                    PsiTreeUtil.getParentOfType(it, ModuleInstantiationPsiNode::class.java) == null
//                },
//            PsiTreeUtil.findChildrenOfType(this, VariableIdentifierPsiNode::class.java)
//        )
//            .flatten()
//            .filterIsInstance(PsiNamedElement::class.java)
//    }


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

  def getPorts: Iterable[PortDeclarationPsiNode] = {
    PsiTreeUtil.findChildrenOfAnyType(this, classOf[PortDeclarationPsiNode]).asScala
  }

  def getFile: VerilogPSIFileRoot = {
    val file = PsiTreeUtil.getParentOfType(this, classOf[VerilogPSIFileRoot])
    file
  }
}
