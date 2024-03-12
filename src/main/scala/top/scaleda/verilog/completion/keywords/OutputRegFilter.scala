package top.scaleda
package verilog.completion.keywords

import verilog.psi.nodes.signal.OutputDeclarationPsiNode

import com.intellij.psi.filters.ElementFilter
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.{PsiComment, PsiElement}

import scala.jdk.CollectionConverters._

class OutputRegFilter extends ElementFilter {
  override def isAcceptable(element: Any, context: PsiElement): Boolean = {
    if (context == null || context.isInstanceOf[PsiComment]) return false

    val portDeclarationParent = PsiTreeUtil.getParentOfType(context, classOf[OutputDeclarationPsiNode])
    if (portDeclarationParent == null) return false

    val children = PsiTreeUtil.getChildrenOfAnyType(portDeclarationParent, classOf[PsiElement]).asScala
    children.foreach(child => if (child.textMatches("reg")) return false)

    true
  }

  override def isClassAcceptable(hintClass: Class[_]): Boolean = true
}
