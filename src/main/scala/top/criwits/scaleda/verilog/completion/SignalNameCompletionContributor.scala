package top.criwits.scaleda
package verilog.completion

import verilog.psi.nodes.expression.{ExpressionPsiNode, HierarchicalIdentifierPsiNode}

import com.intellij.codeInsight.completion.{CompletionContributor, CompletionType}
import com.intellij.patterns.PlatformPatterns
import com.intellij.patterns.StandardPatterns.{and, not}
import com.intellij.psi.filters.position.FilterPattern
import top.criwits.scaleda.verilog.completion.signal.RightExpressionFilter
import top.criwits.scaleda.verilog.psi.nodes.DirectivePsiNode
import top.criwits.scaleda.verilog.psi.nodes.module.ModuleDeclarationPsiNode

class SignalNameCompletionContributor extends CompletionContributor {
  extend(
    CompletionType.BASIC,
    PlatformPatterns.psiElement.and(new FilterPattern(new RightExpressionFilter)),
    new SignalNameCompletionProvider
  )
}
