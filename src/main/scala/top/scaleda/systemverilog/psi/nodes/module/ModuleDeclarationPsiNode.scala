package top.scaleda
package systemverilog.psi.nodes.module

import systemverilog.SystemVerilogPSIFileRoot
import systemverilog.parser.SystemVerilogLexer
import systemverilog.psi.SystemVerilogPsiLeafNodeFactory
import systemverilog.psi.nodes.signal.PortIdentifierPsiNode
import systemverilog.psi.nodes.signal.parameter.ParameterIdentifierPsiNode
import systemverilog.psi.nodes.{DocumentHolder, SimpleIdentifierPsiLeafNode, StructureViewNode}

import com.intellij.lang.ASTNode
import com.intellij.lang.documentation.DocumentationMarkup
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.{PsiElement, PsiNameIdentifierOwner}
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode
import org.jetbrains.annotations.Nls

import scala.jdk.CollectionConverters._

/** PSI node for parser rule module_declaration
  * module_declaration
  * : module_header timeunits_declaration? module_item* 'endmodule' module_name?
  * | attribute_instance* module_keyword lifetime? module_identifier '(' '.*' ')' ';' timeunits_declaration? module_item* 'endmodule' module_name?
  * | 'extern' module_header
  * ;
  *
  * @param node AST node
  */
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

  //noinspection DuplicatedCode
  override def setName(name: String): PsiElement = {
    val child = PsiTreeUtil.findChildOfType(this.getNameIdentifier, classOf[SimpleIdentifierPsiLeafNode])
    if (child == null) null
    else {
      child.replace(SystemVerilogPsiLeafNodeFactory.create(SystemVerilogLexer.SIMPLE_IDENTIFIER, name))
    }
  }

  //noinspection ScalaWrongPlatformMethodsUsage
  override def getElementName: String = getName // for struct view

  def getPorts: Seq[PortIdentifierPsiNode] = getModuleHead.map(_.getPorts).getOrElse(Seq[PortIdentifierPsiNode]())

  def getFile: SystemVerilogPSIFileRoot = {
    val file = PsiTreeUtil.getParentOfType(this, classOf[SystemVerilogPSIFileRoot])
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
  // module head = 'module' + id + param + (port_declarations | port)
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
    getModuleHead.get.getModuleParameterPortList
      .map(_.getParameterIdentifiers)
      .getOrElse(Seq[ParameterIdentifierPsiNode]())
  }
}
