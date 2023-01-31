package top.criwits.scaleda
package verilog.structview

import com.intellij.ide.structureView.StructureViewTreeElement
import com.intellij.ide.util.treeView.smartTree.{SortableTreeElement, TreeElement}
import com.intellij.navigation.{ItemPresentation, NavigationItem}
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.{PsiElement, PsiNamedElement, PsiRecursiveElementVisitor}
import org.antlr.intellij.adaptor.xpath.XPath
import top.criwits.scaleda.tcl.psi.factory.nodes.IdentifierPsiNode
import top.criwits.scaleda.verilog.psi.factory.nodes.RegDeclarationPsiNode
import top.criwits.scaleda.verilog.{VerilogLanguage, VerilogPSIFileRoot}

import scala.collection.mutable.ListBuffer

class VerilogStructViewElement(val element: PsiElement) extends StructureViewTreeElement with SortableTreeElement {
  override def getValue: AnyRef = element

  override def getAlphaSortKey: String = element match {
    case item: PsiNamedElement => item.getName
    case _ => "unknown key"
  }

  override def getPresentation: ItemPresentation = new VerilogItemPresentation(element)

  override def getChildren: Array[TreeElement] = {
    val treeElements = new ListBuffer[TreeElement]
    element match {
      case root: VerilogPSIFileRoot =>
        new PsiRecursiveElementVisitor() {
          override def visitElement(element: PsiElement): Unit = {
            element match {
              case node: RegDeclarationPsiNode =>
                // Register declaration
                val reg = PsiTreeUtil.findChildOfAnyType(element, classOf[IdentifierPsiNode])
                if (reg != null) {
                  treeElements.append(new VerilogStructViewElement(reg))
                }
              case _ =>
            }
            super.visitElement(element)
          }
        }.visitElement(element)
      case _ =>
    }

    treeElements.toArray
  }

  override def navigate(requestFocus: Boolean): Unit = element match {
    case item: NavigationItem => item.navigate(requestFocus)
    case _ =>
  }

  override def canNavigate: Boolean = element match {
    case item: NavigationItem => item.canNavigate
    case _ => false
  }

  override def canNavigateToSource: Boolean = element match {
    case item: NavigationItem => item.canNavigateToSource
    case _ => false
  }
}
