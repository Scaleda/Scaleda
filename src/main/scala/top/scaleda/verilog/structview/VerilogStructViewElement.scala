package top.scaleda
package verilog.structview

import verilog.VerilogPSIFileRoot
import verilog.psi.nodes.always.AlwaysConstructPsiNode
import verilog.psi.nodes.condition.ConditionalStatementInnerPsiNode
import verilog.psi.nodes.module.ModuleDeclarationPsiNode
import verilog.psi.nodes.signal.{
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

class VerilogStructViewElement(val element: PsiElement) extends StructureViewTreeElement with SortableTreeElement {
  override def getValue: AnyRef = element

  override def getAlphaSortKey: String = element match {
    case item: PsiNamedElement => item.getName
    case _                     => "unknown key"
  }

  override def getPresentation: ItemPresentation = new VerilogItemPresentation(element)

  override def getChildren: Array[TreeElement] = {
    val treeElements = new ListBuffer[TreeElement]
    val children = element match {
      case root: VerilogPSIFileRoot => // Root node
        PsiTreeUtil
          .findChildrenOfAnyType(
            element,
            classOf[ModuleDeclarationPsiNode]
          )
          .asScala

      case module: ModuleDeclarationPsiNode => // Module node
        // always blocks
        val alwaysBlocks = PsiTreeUtil
          .findChildrenOfAnyType(
            element,
            classOf[AlwaysConstructPsiNode]
          )
          .asScala

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

        alwaysBlocks ++ regs ++ nets

      // FIXME: not work belows
      case always: AlwaysConstructPsiNode => // Always node
        always.getChildren.filter(_.isInstanceOf[ConditionalStatementInnerPsiNode]).toSeq

      case conditionalStatement: ConditionalStatementInnerPsiNode => // if...
        conditionalStatement.getChildren.filter(_.isInstanceOf[ConditionalStatementInnerPsiNode]).toSeq

      case _ => Seq.empty
    }

    children.foreach(t => treeElements.append(new VerilogStructViewElement(t)))
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
