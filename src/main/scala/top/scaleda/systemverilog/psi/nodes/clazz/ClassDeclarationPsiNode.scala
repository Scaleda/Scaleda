package top.scaleda
package systemverilog.psi.nodes.clazz

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

/** PSI node for parser rule class_declaration
  * class_declaration
  * : class_header timeunits_declaration? class_item* 'endclass' class_name?
  * | attribute_instance* class_keyword lifetime? class_identifier '(' '.*' ')' ';' timeunits_declaration? class_item* 'endclass' class_name?
  * | 'extern' class_header
  * ;
  *
  * @param node AST node
  */
class ClassDeclarationPsiNode(node: ASTNode)
    extends ANTLRPsiNode(node)
    with PsiNameIdentifierOwner
    with StructureViewNode
    with DocumentHolder {

  override def getNameIdentifier: PsiElement = {
    PsiTreeUtil.getChildrenOfType(this, classOf[ClassIdentifierPsiNode]).head
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

  // def getPorts: Seq[PortIdentifierPsiNode] = getClassHead.map(_.getPorts).getOrElse(Seq[PortIdentifierPsiNode]())

  def getFile: SystemVerilogPSIFileRoot = {
    val file = PsiTreeUtil.getParentOfType(this, classOf[SystemVerilogPSIFileRoot])
    file
  }

  override def getDocument: String = {
    s"${DocumentationMarkup.DEFINITION_START} Class: TODO ${DocumentationMarkup.DEFINITION_END}"
  }

  // private def generateClassPreview: String = {
  //   def generatePorts: String = {
  //     if (getClassHead.isEmpty) return "()" // todo more elegantly
  //     val ports = getClassHead.get.getPorts
  //     if (ports.isEmpty) "()"
  //     else {
  //       val portList = ports
  //         .map(port =>
  //           s"${port.getTypeText}${port.getRange.map(r => " " + r.generateText).getOrElse("")} ${port.getName}"
  //         )
  //         .mkString(",\n  ")
  //       s"""(
  //          |  ${portList}
  //          |)""".stripMargin
  //     }
  //   }
  //   def generateParams: String = {
  //     val params = getParameters
  //     if (params.isEmpty) ""
  //     else {
  //       val paramList = params
  //         .map(param =>
  //           s"${param.getDeclaration.getTypeText} ${param.getName}${param.getInitialValue.map(value => s" = ${value.getText}").getOrElse("")}"
  //         )
  //         .mkString(",\n  ")
  //       s""" #(
  //            |  ${paramList}
  //            |)""".stripMargin
  //     }
  //   }
  //
  //   s"class ${getName}${generateParams} ${generatePorts}"
  // }

  def getClassItems: Seq[ClassItemPsiNode] = {
    val result = PsiTreeUtil.getChildrenOfType(this, classOf[ClassItemPsiNode])
    if (result == null) Seq[ClassItemPsiNode]() else result.toSeq
  }

  // def getChildrenInClassBody[T <: PsiElement](clazz: Class[T]): Seq[T] = {
  //   getClassItems.map(item => PsiTreeUtil.findChildrenOfType(item, clazz).asScala.toSeq).foldLeft(Seq[T]())(_ ++ _)
  // }
}
