package top.criwits.scaleda
package verilog.completion

import verilog.psi.factory.nodes.DirectivePsiNode
import verilog.{VerilogFileType, VerilogKeywords}

import com.intellij.codeInsight.completion.{CompletionContributor, CompletionType}
import com.intellij.patterns.PlatformPatterns
import com.intellij.patterns.StandardPatterns.not

class VerilogCompletionContributor extends CompletionContributor {
extend(CompletionType.BASIC,
  not(PlatformPatterns.psiElement.inside(classOf[DirectivePsiNode])),
  new KeywordCompletionProvider(VerilogKeywords.VERILOG_KEYWORDS, VerilogFileType.DefaultIcon))
}
