package top.criwits.scaleda
package verilog.structview

import verilog.VerilogPSIFileRoot
import verilog.psi.factory.nodes._

import com.intellij.ide.structureView.StructureViewTreeElement
import com.intellij.ide.util.treeView.smartTree.{SortableTreeElement, TreeElement}
import com.intellij.navigation.{ItemPresentation, NavigationItem}
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.{PsiElement, PsiNamedElement, PsiRecursiveElementVisitor}
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode
import top.criwits.scaleda.verilog.psi.factory.nodes.always.AlwaysConstructPsiNode
import top.criwits.scaleda.verilog.psi.factory.nodes.module.ModuleDeclarationPsiNode
import top.criwits.scaleda.verilog.psi.factory.nodes.singal.{NetDeclarationPsiNode, NetIdentifierPsiNode, RegDeclarationPsiNode, VariableIdentifierPsiNode}

import java.util
import java.util.Collections
import java.util.function.Consumer
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
        PsiTreeUtil.findChildrenOfAnyType(
          element,
          classOf[ModuleDeclarationPsiNode]
        ).asScala

      case module: ModuleDeclarationPsiNode => // Module node
        // always blocks
        val alwaysBlocks = PsiTreeUtil.findChildrenOfAnyType(
          element,
//          classOf[RegDeclarationPsiNode],
//          classOf[NetDeclarationPsiNode],
          classOf[AlwaysConstructPsiNode]
        ).asScala

        // reg
        val regDeclarations = PsiTreeUtil.findChildrenOfAnyType(element, classOf[RegDeclarationPsiNode]).asScala
        val regs = regDeclarations.map(r => {
          PsiTreeUtil.findChildrenOfAnyType(r, classOf[VariableIdentifierPsiNode]).asScala
        }).reduce(_ ++ _)

        // net
        val netDeclarations = PsiTreeUtil.findChildrenOfAnyType(element, classOf[NetDeclarationPsiNode]).asScala
        val nets = netDeclarations.map(r => {
          PsiTreeUtil.findChildrenOfAnyType(r, classOf[NetIdentifierPsiNode]).asScala
        }).reduce(_ ++ _)

        (alwaysBlocks ++ regs ++ nets)

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
