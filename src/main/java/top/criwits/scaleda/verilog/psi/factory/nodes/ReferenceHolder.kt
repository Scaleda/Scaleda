package top.criwits.scaleda.verilog.psi.factory.nodes

import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement

interface ReferenceHolder<T : PsiElement> {

    fun getHoldPsiNode(): T?
    fun getHoldPsiNodeRelativeTextRange(): TextRange?

}