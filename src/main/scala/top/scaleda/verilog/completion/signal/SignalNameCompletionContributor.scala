package top.scaleda
package verilog.completion.signal

import com.intellij.codeInsight.completion.{CompletionContributor, CompletionType}
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.filters.position.FilterPattern

class SignalNameCompletionContributor extends CompletionContributor {
  extend(
    CompletionType.BASIC,
    PlatformPatterns.psiElement.and(new FilterPattern(new RightExpressionFilter)),
    new SignalNameCompletionProvider
  )

  extend(
    CompletionType.BASIC,
    PlatformPatterns.psiElement.and(new FilterPattern(new LeftExpressionFilter)),
    new SignalNameCompletionProvider(net = false, reg = true, input = false, output = false, outputReg = true)
  )

}
