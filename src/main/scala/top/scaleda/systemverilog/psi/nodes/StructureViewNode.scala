package top.scaleda
package systemverilog.psi.nodes

import com.intellij.psi.PsiElement

trait StructureViewNode extends PsiElement { def getElementName: String }
