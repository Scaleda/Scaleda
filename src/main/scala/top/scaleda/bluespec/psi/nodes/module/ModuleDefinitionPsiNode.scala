package top.scaleda
package bluespec.psi.nodes.module

import bluespec.parser.BluespecLexer
import bluespec.psi.nodes.{DocumentHolder, StructureViewNode}
import bluespec.psi
import bluespec.psi.nodes

import com.intellij.lang.ASTNode
import com.intellij.lang.documentation.DocumentationMarkup
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.{PsiElement, PsiNameIdentifierOwner}
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode
import org.jetbrains.annotations.Nls

import scala.jdk.CollectionConverters._

/** PSI node for parser rule moduleDef
  *
  * @param node AST node
  */
class ModuleDefinitionPsiNode(node: ASTNode)
    extends ANTLRPsiNode(node)
    with PsiNameIdentifierOwner
    with StructureViewNode
    with DocumentHolder {

  override def getNameIdentifier: PsiElement =
    PsiTreeUtil.findChildOfType(this, classOf[ModuleIdentifierPsiNode])

  @Nls
  override def getName: String = getNameIdentifier match {
    case element: PsiElement => element.getText
    case null                => "(unknown)"
  }

  //noinspection DuplicatedCode
  override def setName(name: String): PsiElement = {
    val child = PsiTreeUtil.findChildOfType(this.getNameIdentifier, classOf[nodes.IdentifierPsiLeafNode])
    if (child == null) null
    else {
      child.replace(psi.BluespecPsiLeafNodeFactory.create(BluespecLexer.Identifier, name))
    }
  }

  //noinspection ScalaWrongPlatformMethodsUsage
  override def getElementName: String = getName // for struct view

  def getFile: bluespec.BluespecPSIFileRoot = PsiTreeUtil.getParentOfType(this, classOf[bluespec.BluespecPSIFileRoot])

  override def getDocument: String =
    s"${DocumentationMarkup.DEFINITION_START}${generateModulePreview}${DocumentationMarkup.DEFINITION_END}"

  private def generateModulePreview: String = {
    s"module ${getName}"
  }

  def getModuleStmts: Seq[ModuleStmtPsiNode] = {
    val result = PsiTreeUtil.getChildrenOfType(this, classOf[ModuleStmtPsiNode])
    if (result == null) Seq[ModuleStmtPsiNode]() else result.toSeq
  }

  def getChildrenInModuleBody[T <: PsiElement](clazz: Class[T]): Seq[T] = {
    getModuleStmts.map(item => PsiTreeUtil.findChildrenOfType(item, clazz).asScala.toSeq).foldLeft(Seq[T]())(_ ++ _)
  }
}
