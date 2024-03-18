package top.scaleda
package systemverilog.psi.nodes.expression

import com.intellij.lang.ASTNode
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode

class PrimaryPsiNode(node: ASTNode) extends ANTLRPsiNode(node) with IntEvaluable {
  override def evaluate: Option[Int] = {
    // If my children contains NumberPsiNode then return it's value
    val children = getChildren
    if (children.exists(_.isInstanceOf[NumberPsiNode])) {
      return children.filter(_.isInstanceOf[NumberPsiNode]).head.asInstanceOf[NumberPsiNode].evaluate
    }

    // TODO: add more cases, like `( expression )`

    // Otherwise return None
    None
  }
}
