package top.scaleda
package verilog.psi.nodes.module

import verilog.VerilogPSIFileRoot
import verilog.parser.VerilogLexer
import verilog.psi.VerilogPsiLeafNodeFactory
import verilog.psi.nodes.signal.PortDeclarationPsiNode
import verilog.psi.nodes.signal.parameter.ParameterIdentifierPsiNode
import verilog.psi.nodes.{SimpleIdentifierPsiLeafNode, StructureViewNode}

import com.intellij.lang.ASTNode
import com.intellij.lang.documentation.DocumentationMarkup
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.{PsiElement, PsiNameIdentifierOwner}
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode
import org.jetbrains.annotations.Nls
import top.scaleda.verilog.psi.nodes.DocumentHolder

import scala.jdk.CollectionConverters._

class ModuleDeclarationPsiNode(node: ASTNode)
    extends ANTLRPsiNode(node)
    with PsiNameIdentifierOwner
    with StructureViewNode
    with DocumentHolder {
  override def getNameIdentifier: PsiElement = {
    val head = getModuleHead
    if (head.isEmpty) null
    else {
      val name = head.get.getModuleIdentifier
      if (name.isEmpty) null
      else name.get
    }
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

//  def getPorts: Array[PortDeclarationPsiNode] = {
//    import scala.jdk.CollectionConverters._
//    val list = PsiTreeUtil.findChildrenOfType(this, classOf[ListOfPortDeclarationsPsiNode]).asScala
//    if (list.isEmpty) return Array.empty[PortDeclarationPsiNode]
//
//    val result = PsiTreeUtil.getChildrenOfType(list.head, classOf[PortDeclarationPsiNode])
//    if (result == null) Array.empty[PortDeclarationPsiNode] else result
//  }

  def getFile: VerilogPSIFileRoot = {
    val file = PsiTreeUtil.getParentOfType(this, classOf[VerilogPSIFileRoot])
    file
  }

  override def getDocument: String = {
    s"${DocumentationMarkup.DEFINITION_START}${generateModulePreview}${DocumentationMarkup.DEFINITION_END}"
  }

  private def generateModulePreview: String = {
    def generatePorts: String = {
      if (getModuleHead.isEmpty) return "()" // todo more elegantly
      val ports = getModuleHead.get.getPorts
      if (ports.isEmpty) "()"
      else {
        val portList = ports
          .map(port =>
            s"${port.getTypeText}${port.getRange.map(r => " " + r.generateText).getOrElse("")} ${port.getName}"
          )
          .mkString(",\n  ")
        s"""(
           |  ${portList}
           |)""".stripMargin
      }
    }
    def generateParams: String = {
      val params = getParameters
      if (params.isEmpty) ""
      else {
        val paramList = params
          .map(param =>
            s"${param.getDeclaration.getTypeText} ${param.getName}${param.getInitialValue.map(value => s" = ${value.getText}").getOrElse("")}"
          )
          .mkString(",\n  ")
        s""" #(
             |  ${paramList}
             |)""".stripMargin
      }
    }

    s"module ${getName}${generateParams} ${generatePorts}"
  }

//  def getModuleHead: ModuleHeadPsiNode = {
  // if a module is under typing then it probably hasn't head!
  // module head = 'module' + id + param + port
  def getModuleHead: Option[ModuleHeadPsiNode] = {
    Option(PsiTreeUtil.getChildOfType(this, classOf[ModuleHeadPsiNode]))
  }

  def getModuleItems: Seq[ModuleItemPsiNode] = {
    val result = PsiTreeUtil.getChildrenOfType(this, classOf[ModuleItemPsiNode])
    if (result == null) Seq[ModuleItemPsiNode]() else result.toSeq
  }

  def getChildrenInModuleBody[T <: PsiElement](clazz: Class[T]): Seq[T] = {
    getModuleItems.map(item => PsiTreeUtil.findChildrenOfType(item, clazz).asScala.toSeq).foldLeft(Seq[T]())(_ ++ _)
  }

  def getParameters: Seq[ParameterIdentifierPsiNode] = {
    if (getModuleHead.isEmpty) return Seq[ParameterIdentifierPsiNode]()
    getModuleHead.get.getModuleParameterPortList.map(_.getParameterIdentifiers).getOrElse(Seq[ParameterIdentifierPsiNode]())
  }
}
