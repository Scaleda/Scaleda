package top.criwits.scaleda
package verilog.psi.nodes.expression

import com.intellij.psi.PsiElement

trait IntEvaluable {
  def evaluate: Option[Int] = {
    this match {
      case element: PsiElement =>
        val children = element.getChildren
        children.foreach {
          case child: IntEvaluable => return child.asInstanceOf[IntEvaluable].evaluate
          case _ =>
        }
        None
      case _ => None
    }
  }
}
