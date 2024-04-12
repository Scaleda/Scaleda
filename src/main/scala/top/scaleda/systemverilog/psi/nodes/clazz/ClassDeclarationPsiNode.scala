package top.scaleda
package systemverilog.psi.nodes.clazz

import systemverilog.SystemVerilogPSIFileRoot
import systemverilog.parser.SystemVerilogLexer
import systemverilog.psi.SystemVerilogPsiLeafNodeFactory
import systemverilog.psi.nodes.{DocumentHolder, SimpleIdentifierPsiLeafNode, StructureViewNode}

import com.intellij.lang.ASTNode
import com.intellij.lang.documentation.DocumentationMarkup
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.{PsiElement, PsiNameIdentifierOwner}
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode
import org.jetbrains.annotations.Nls

import scala.jdk.CollectionConverters._

/** PSI node for parser rule class_declaration
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

  def getFile: SystemVerilogPSIFileRoot = {
    val file = PsiTreeUtil.getParentOfType(this, classOf[SystemVerilogPSIFileRoot])
    file
  }

  override def getDocument: String = {
    s"${DocumentationMarkup.DEFINITION_START}class ${getNameIdentifier.getText};${DocumentationMarkup.DEFINITION_END}"
  }

  private def getClassItemType[T <: PsiElement](clazz: Class[T]): Seq[T] = {
    val result =
      PsiTreeUtil.findChildrenOfType(this, clazz).asScala
    if (result == null) Seq[T]() else result.toSeq
  }

  def getClassItems: Seq[ClassItemPsiNode]          = getClassItemType(classOf[ClassItemPsiNode])
  def getProperties: Seq[ClassPropertyPsiNode]      = getClassItemType(classOf[ClassPropertyPsiNode])
  def getConstraints: Seq[ClassConstraintPsiNode]   = getClassItemType(classOf[ClassConstraintPsiNode])
  def getConstructors: Seq[ClassConstructorPsiNode] = getClassItemType(classOf[ClassConstructorPsiNode])
  def getMethods: Seq[ClassMethodPsiNode]           = getClassItemType(classOf[ClassMethodPsiNode])
}
