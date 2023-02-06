package top.criwits.scaleda
package verilog.completion.keywords

import verilog.psi.nodes.instantiation.ModuleInstantiationPsiNode
import verilog.psi.nodes.module.ModuleDeclarationPsiNode

import com.intellij.psi.filters.ElementFilter
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.{PsiComment, PsiElement}

import scala.jdk.CollectionConverters._

class EndmoduleFilter extends ElementFilter {
  override def isAcceptable(element: Any, context: PsiElement): Boolean = {
    if (context == null || context.isInstanceOf[PsiComment]) return false
    val moduleDef = PsiTreeUtil.getParentOfType(context, classOf[ModuleDeclarationPsiNode])
    if (moduleDef == null) return false

    val hasEndmodule =
      PsiTreeUtil.findChildrenOfAnyType(moduleDef, classOf[PsiElement]).asScala.exists(_.textMatches("endmodule"))
    if (hasEndmodule) return false

    try {
      val p = context.getParent.getParent.getParent
      return p match {
        case _: ModuleInstantiationPsiNode => return true
        case _                             => return false
      }
    } catch {
      case _: Throwable => return false
    }

    false
  }

  override def isClassAcceptable(hintClass: Class[_]): Boolean = true
}
