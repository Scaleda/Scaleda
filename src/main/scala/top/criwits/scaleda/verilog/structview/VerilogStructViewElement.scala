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

import java.util
import java.util.Collections
import java.util.function.Consumer
import scala.collection.mutable.ListBuffer

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
        )

      case module: ModuleDeclarationPsiNode => // Module node
        PsiTreeUtil.findChildrenOfAnyType(
          element,
          classOf[RegDeclarationPsiNode],
          classOf[NetDeclarationPsiNode],
          classOf[AlwaysConstructPsiNode]
        )
      case _ => Collections.emptyList()
    }

    children.forEach((t: ANTLRPsiNode) => treeElements.append(new VerilogStructViewElement(t)))
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
