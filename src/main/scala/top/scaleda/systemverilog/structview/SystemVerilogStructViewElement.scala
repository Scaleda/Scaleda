package top.scaleda
package systemverilog.structview

import systemverilog.SystemVerilogPSIFileRoot
import systemverilog.psi.nodes.clazz.{ClassConstructorPsiNode, ClassDeclarationPsiNode, ClassMethodPsiNode}
import systemverilog.psi.nodes.module.{ModuleDeclarationPsiNode, ModuleItemPsiNode}
import systemverilog.psi.nodes.signal.{
  NetDeclarationPsiNode,
  NetIdentifierPsiNode,
  VariableDeclarationPsiNode,
  VariableIdentifierPsiNode
}

import com.intellij.ide.structureView.StructureViewTreeElement
import com.intellij.ide.util.treeView.smartTree.{SortableTreeElement, TreeElement}
import com.intellij.navigation.{ItemPresentation, NavigationItem}
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.{PsiElement, PsiNamedElement}
import top.scaleda.systemverilog.psi.nodes.function.FunctionDeclarationPsiNode
import top.scaleda.systemverilog.psi.nodes.task.TaskDeclarationPsiNode

import scala.collection.mutable.ListBuffer
import scala.jdk.CollectionConverters._

class SystemVerilogStructViewElement(val element: PsiElement)
    extends StructureViewTreeElement
    with SortableTreeElement {
  override def getValue: AnyRef = element

  override def getAlphaSortKey: String = element match {
    case item: PsiNamedElement => item.getName
    case _                     => "unknown key"
  }

  override def getPresentation: ItemPresentation = new SystemVerilogItemPresentation(element)

  override def getChildren: Array[TreeElement] = {
    val treeElements = new ListBuffer[TreeElement]
    val children = element match {
      case root: SystemVerilogPSIFileRoot => // Root node
        val modules = PsiTreeUtil
          .findChildrenOfAnyType(
            root,
            classOf[ModuleDeclarationPsiNode]
          )
          .asScala
        val classes = PsiTreeUtil
          .findChildrenOfAnyType(
            root,
            classOf[ClassDeclarationPsiNode]
          )
          .asScala
        val tasks = PsiTreeUtil
          .findChildrenOfAnyType(
            root,
            classOf[TaskDeclarationPsiNode]
          )
          .asScala
          .filter(t => PsiTreeUtil.getParentOfType(t, classOf[ClassMethodPsiNode]) == null)
          .filter(t => PsiTreeUtil.getParentOfType(t, classOf[ModuleItemPsiNode]) == null)
        tasks ++ modules ++ classes

      case module: ModuleDeclarationPsiNode => // Module node
        // always blocks
        // val alwaysBlocks = PsiTreeUtil
        //   .findChildrenOfAnyType(
        //     element,
        //     classOf[AlwaysConstructPsiNode]
        //   )
        //   .asScala

        // reg
        val regDeclarations = PsiTreeUtil.findChildrenOfAnyType(module, classOf[VariableDeclarationPsiNode]).asScala
        val regs            = new ListBuffer[VariableIdentifierPsiNode]
        regDeclarations
          .map(r => {
            regs.addAll(PsiTreeUtil.findChildrenOfAnyType(r, classOf[VariableIdentifierPsiNode]).asScala)
          })

        // net
        val netDeclarations = PsiTreeUtil.findChildrenOfAnyType(module, classOf[NetDeclarationPsiNode]).asScala
        val nets            = new ListBuffer[NetIdentifierPsiNode]
        netDeclarations
          .foreach(r => {
            nets.addAll(PsiTreeUtil.findChildrenOfAnyType(r, classOf[NetIdentifierPsiNode]).asScala)
          })

        // functions
        val functions = PsiTreeUtil.findChildrenOfAnyType(module, classOf[FunctionDeclarationPsiNode]).asScala

        // alwaysBlocks ++ regs ++ nets
        functions ++ regs ++ nets

      case clazz: ClassDeclarationPsiNode =>
        clazz.getProperties ++ clazz.getConstraints ++ clazz.getConstructors ++ clazz.getMethods.filter(method => {
          PsiTreeUtil.getChildOfType(method, classOf[ClassConstructorPsiNode]) == null
        }) ++ clazz.getParameters

      case _ => Seq.empty
    }

    children.foreach(t => treeElements.append(new SystemVerilogStructViewElement(t)))
    treeElements.toArray
  }

  override def navigate(requestFocus: Boolean): Unit = element match {
    case item: NavigationItem => item.navigate(requestFocus)
    case _                    =>
  }

  override def canNavigate: Boolean = element match {
    case item: NavigationItem => item.canNavigate
    case _                    => false
  }

  override def canNavigateToSource: Boolean = element match {
    case item: NavigationItem => item.canNavigateToSource
    case _                    => false
  }
}
