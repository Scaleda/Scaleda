package top.scaleda
package systemverilog.structview

import systemverilog.SystemVerilogPSIFileRoot

import top.scaleda.systemverilog.psi.nodes.clazz.ClassDeclarationPsiNode
// import systemverilog.psi.nodes.always.AlwaysConstructPsiNode
// import systemverilog.psi.nodes.condition.ConditionalStatementInnerPsiNode
import systemverilog.psi.nodes.module.ModuleDeclarationPsiNode
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
            element,
            classOf[ModuleDeclarationPsiNode]
          )
          .asScala
        val classes = PsiTreeUtil
          .findChildrenOfAnyType(
            element,
            classOf[ClassDeclarationPsiNode]
          )
          .asScala
        modules ++ classes

      case module: ModuleDeclarationPsiNode => // Module node
        // always blocks
        // val alwaysBlocks = PsiTreeUtil
        //   .findChildrenOfAnyType(
        //     element,
        //     classOf[AlwaysConstructPsiNode]
        //   )
        //   .asScala

        // reg
        val regDeclarations = PsiTreeUtil.findChildrenOfAnyType(element, classOf[VariableDeclarationPsiNode]).asScala
        val regs            = new ListBuffer[VariableIdentifierPsiNode]
        regDeclarations
          .map(r => {
            regs.addAll(PsiTreeUtil.findChildrenOfAnyType(r, classOf[VariableIdentifierPsiNode]).asScala)
          })

        // net
        val netDeclarations = PsiTreeUtil.findChildrenOfAnyType(element, classOf[NetDeclarationPsiNode]).asScala
        val nets            = new ListBuffer[NetIdentifierPsiNode]
        netDeclarations
          .foreach(r => {
            nets.addAll(PsiTreeUtil.findChildrenOfAnyType(r, classOf[NetIdentifierPsiNode]).asScala)
          })

        // alwaysBlocks ++ regs ++ nets
        regs ++ nets

      case clazz: ClassDeclarationPsiNode => Seq.empty

      // FIXME: not work belows
      // case always: AlwaysConstructPsiNode => // Always node
      //   always.getChildren.filter(_.isInstanceOf[ConditionalStatementInnerPsiNode]).toSeq
      //
      // case conditionalStatement: ConditionalStatementInnerPsiNode => // if...
      //   conditionalStatement.getChildren.filter(_.isInstanceOf[ConditionalStatementInnerPsiNode]).toSeq

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
