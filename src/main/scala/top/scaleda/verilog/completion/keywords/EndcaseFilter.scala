package top.scaleda
package verilog.completion.keywords

import com.intellij.psi.PsiElement
import com.intellij.psi.filters.ElementFilter
import com.intellij.psi.util.PsiTreeUtil
import top.scaleda.verilog.psi.nodes.block.{CaseBodyPsiNode, CaseStatementPsiNode}

class EndcaseFilter extends ElementFilter{
  override def isAcceptable(element: Any, context: PsiElement): Boolean = {
    if ( context == null ) return false
    val caseStatement = PsiTreeUtil.getParentOfType(context,classOf[CaseStatementPsiNode])
    if (caseStatement == null) return false
    if (!caseStatement.getLastChild.textMatches("endcase")) {
      val body = PsiTreeUtil.getParentOfType(context, classOf[CaseBodyPsiNode])
      if (body == null) return false
      return true
    }

    false
  }

  override def isClassAcceptable(hintClass: Class[_]): Boolean = true
}
