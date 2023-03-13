package top.criwits.scaleda
package verilog.psi.nodes.expression

import verilog.psi.nodes.signal.SignalIdentifierPsiNode

import com.intellij.lang.ASTNode
import com.intellij.psi.util.PsiTreeUtil
import org.antlr.intellij.adaptor.psi.ANTLRPsiNode

import scala.jdk.CollectionConverters._

class ExpressionPsiNode(node: ASTNode) extends ANTLRPsiNode(node) {
  def getHierarchical = PsiTreeUtil.findChildrenOfType(this, classOf[HierarchicalIdentifierPsiNode]).asScala

  def getUsedSignals = getHierarchical
    .map(_.getReference.resolve())
    .filter(_.isInstanceOf[SignalIdentifierPsiNode])
    .map(_.asInstanceOf[SignalIdentifierPsiNode])
}
