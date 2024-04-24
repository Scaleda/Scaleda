package top.scaleda
package systemverilog.structview

import idea.utils.Icons
import systemverilog.psi.nodes.StructureViewNode
import systemverilog.psi.nodes.always.AlwaysConstructPsiNode
import systemverilog.psi.nodes.clazz._
import systemverilog.psi.nodes.function.FunctionDeclarationPsiNode
import systemverilog.psi.nodes.initial.InitialPsiNode
import systemverilog.psi.nodes.module.ModuleDeclarationPsiNode
import systemverilog.psi.nodes.parameter.{ParameterDeclarationPsiNode, ParameterPortDeclarationPsiNode}
import systemverilog.psi.nodes.signal.{NetIdentifierPsiNode, VariableIdentifierPsiNode}
import systemverilog.psi.nodes.task.TaskDeclarationPsiNode

import com.intellij.icons.AllIcons
import com.intellij.navigation.ItemPresentation
import com.intellij.psi.PsiElement

import javax.swing.Icon

class SystemVerilogItemPresentation(val element: PsiElement) extends ItemPresentation {
  override def getPresentableText: String = element match {
    case node: StructureViewNode => node.getElementName
    case _                       => "(unknown)"
  }

  override def getIcon(unused: Boolean): Icon = element match {
    case _: ClassDeclarationPsiNode         => AllIcons.Nodes.Class
    case _: ClassPropertyPsiNode            => AllIcons.Nodes.Property
    case _: ClassConstraintPsiNode          => AllIcons.Nodes.Constant
    case _: ClassConstructorPsiNode         => AllIcons.Nodes.ClassInitializer
    case _: ClassMethodPsiNode              => AllIcons.Nodes.Method
    case _: ParameterDeclarationPsiNode     => AllIcons.Nodes.Parameter
    case _: ParameterPortDeclarationPsiNode => AllIcons.Nodes.NewParameter
    case _: ModuleDeclarationPsiNode        => Icons.systemVerilog
    case _: VariableIdentifierPsiNode       => Icons.verilogReg
    case _: NetIdentifierPsiNode            => Icons.verilogWire
    case _: InitialPsiNode                  => AllIcons.Nodes.ClassInitializer
    case _: AlwaysConstructPsiNode          => Icons.verilogAlways
    case _: TaskDeclarationPsiNode          => AllIcons.Actions.Execute
    case _: FunctionDeclarationPsiNode      => AllIcons.Nodes.Function
    case _                                  => AllIcons.General.TodoQuestion // should never reach!
  }
}
