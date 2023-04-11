package top.criwits.scaleda
package verilog.psi.nodes.expression

import com.intellij.lang.ASTNode
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode

class NumberPsiNode(node: ASTNode) extends ANTLRPsiNode(node) with IntEvaluable{
}
