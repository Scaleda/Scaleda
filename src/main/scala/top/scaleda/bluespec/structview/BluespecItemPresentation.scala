package top.scaleda
package bluespec.structview

import idea.utils.Icons
import bluespec.psi.nodes.StructureViewNode

import com.intellij.icons.AllIcons
import com.intellij.navigation.ItemPresentation
import com.intellij.psi.PsiElement

import javax.swing.Icon

class BluespecItemPresentation(val element: PsiElement) extends ItemPresentation {
  override def getPresentableText: String = element match {
    case node: StructureViewNode => node.getElementName
    case _                       => "(unknown)"
  }

  override def getIcon(unused: Boolean): Icon = element match {
    // case _: clazz.ClassDeclarationPsiNode         => AllIcons.Nodes.Class
    // case _: clazz.ClassPropertyPsiNode            => AllIcons.Nodes.Property
    // case _: clazz.ClassConstraintPsiNode          => AllIcons.Nodes.Constant
    // case _: clazz.ClassConstructorPsiNode         => AllIcons.Nodes.ClassInitializer
    // case _: clazz.ClassMethodPsiNode              => AllIcons.Nodes.Method
    // case _: parameter.ParameterDeclarationPsiNode     => AllIcons.Nodes.Parameter
    // case _: parameter.ParameterPortDeclarationPsiNode => AllIcons.Nodes.NewParameter
    // case _: module.ModuleDeclarationPsiNode        => Icons.bluespec
    // case _: signal.VariableIdentifierPsiNode       => Icons.verilogReg
    // case _: signal.NetIdentifierPsiNode            => Icons.verilogWire
    // case _: initial.InitialPsiNode                  => AllIcons.Nodes.ClassInitializer
    // case _: always.AlwaysConstructPsiNode          => Icons.verilogAlways
    // case _: task.TaskDeclarationPsiNode          => AllIcons.Actions.Execute
    // case _: function.FunctionDeclarationPsiNode      => AllIcons.Nodes.Function
    case _                                  => AllIcons.General.TodoQuestion // should never reach!
  }
}
