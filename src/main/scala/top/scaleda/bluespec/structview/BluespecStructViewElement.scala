package top.scaleda
package bluespec.structview

// import bluespec.psi.nodes.{always, clazz, function, initial, module, signal, task}

import com.intellij.ide.structureView.StructureViewTreeElement
import com.intellij.ide.util.treeView.smartTree.{SortableTreeElement, TreeElement}
import com.intellij.navigation.{ItemPresentation, NavigationItem}
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.{PsiElement, PsiNamedElement}

import scala.collection.mutable.ListBuffer
import scala.jdk.CollectionConverters._

class BluespecStructViewElement(val element: PsiElement)
    extends StructureViewTreeElement
    with SortableTreeElement {
  override def getValue: AnyRef = element

  override def getAlphaSortKey: String = element match {
    case item: PsiNamedElement => item.getName
    case _                     => "unknown key"
  }

  override def getPresentation: ItemPresentation = new BluespecItemPresentation(element)

  override def getChildren: Array[TreeElement] = {
    val treeElements = new ListBuffer[TreeElement]
    // val children = element match {
    //   case root: bluespec.BluespecPSIFileRoot => // Root node
    //     val modules = PsiTreeUtil
    //       .findChildrenOfAnyType(
    //         root,
    //         classOf[module.ModuleDeclarationPsiNode]
    //       )
    //       .asScala
    //     val classes = PsiTreeUtil
    //       .findChildrenOfAnyType(
    //         root,
    //         classOf[clazz.ClassDeclarationPsiNode]
    //       )
    //       .asScala
    //     val tasks = PsiTreeUtil
    //       .findChildrenOfAnyType(
    //         root,
    //         classOf[task.TaskDeclarationPsiNode]
    //       )
    //       .asScala
    //       .filter(t => PsiTreeUtil.getParentOfType(t, classOf[clazz.ClassMethodPsiNode]) == null)
    //       .filter(t => PsiTreeUtil.getParentOfType(t, classOf[module.ModuleItemPsiNode]) == null)
    //     val functions = PsiTreeUtil
    //       .findChildrenOfAnyType(
    //         root,
    //         classOf[function.FunctionDeclarationPsiNode]
    //       )
    //       .asScala
    //       .filter(t => PsiTreeUtil.getParentOfType(t, classOf[clazz.ClassMethodPsiNode]) == null)
    //       .filter(t => PsiTreeUtil.getParentOfType(t, classOf[module.ModuleItemPsiNode]) == null)
    //     tasks ++ functions ++ modules ++ classes
    //
    //   case module: module.ModuleDeclarationPsiNode => // Module node
    //     // always blocks
    //     val alwaysBlocks = PsiTreeUtil
    //       .findChildrenOfAnyType(
    //         element,
    //         classOf[always.AlwaysConstructPsiNode]
    //       )
    //       .asScala
    //     // initial blocks
    //     val initialBlocks = PsiTreeUtil.findChildrenOfAnyType(module, classOf[initial.InitialPsiNode]).asScala
    //
    //     // reg
    //     val regDeclarations = PsiTreeUtil.findChildrenOfAnyType(module, classOf[signal.VariableDeclarationPsiNode]).asScala
    //     val regs            = new ListBuffer[signal.VariableIdentifierPsiNode]
    //     regDeclarations
    //       .map(r => {
    //         regs.addAll(PsiTreeUtil.findChildrenOfAnyType(r, classOf[signal.VariableIdentifierPsiNode]).asScala)
    //       })
    //
    //     // net
    //     val netDeclarations = PsiTreeUtil.findChildrenOfAnyType(module, classOf[signal.NetDeclarationPsiNode]).asScala
    //     val nets            = new ListBuffer[signal.NetIdentifierPsiNode]
    //     netDeclarations
    //       .foreach(r => {
    //         nets.addAll(PsiTreeUtil.findChildrenOfAnyType(r, classOf[signal.NetIdentifierPsiNode]).asScala)
    //       })
    //
    //     // functions
    //     val functions = PsiTreeUtil.findChildrenOfAnyType(module, classOf[function.FunctionDeclarationPsiNode]).asScala
    //     // tasks
    //     val tasks = PsiTreeUtil.findChildrenOfAnyType(module, classOf[task.TaskDeclarationPsiNode]).asScala
    //
    //     functions ++ tasks ++ initialBlocks ++ alwaysBlocks ++ regs ++ nets
    //
    //   case clazz: clazz.ClassDeclarationPsiNode =>
    //     clazz.getParameterPorts ++ clazz.getParameters ++ clazz.getProperties ++
    //       clazz.getConstraints ++ clazz.getConstructors ++ clazz.getMethodsNoConstructor
    //
    //   case _ => Seq.empty
    // }
    //
    // children.foreach(t => treeElements.append(new BluespecStructViewElement(t)))
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
