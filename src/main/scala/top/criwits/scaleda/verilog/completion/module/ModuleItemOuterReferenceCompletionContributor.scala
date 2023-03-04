package top.criwits.scaleda
package verilog.completion.module

import com.intellij.codeInsight.completion.{CompletionContributor, CompletionType}
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.filters.position.FilterPattern

class ModuleItemOuterReferenceCompletionContributor extends CompletionContributor {
  extend(
    CompletionType.BASIC,
    PlatformPatterns.psiElement.and(new FilterPattern(new ModuleInstantiationFilter)),
    new ModuleInstantiationProvider
  )

  extend(
    CompletionType.BASIC,
    PlatformPatterns.psiElement.and(new FilterPattern(new ModulePortFilter)),
    new ModulePortProvider
  )

  extend(
    CompletionType.BASIC,
    PlatformPatterns.psiElement.and(new FilterPattern(new ModuleInstanceNameFilter)),
    new ModuleInstanceNameProvider
  )
}
