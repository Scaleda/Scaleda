package top.criwits.scaleda
package verilog.completion

import verilog.psi.factory.nodes.{DirectivePsiNode, NamedPortConnectionPsiNode, PortIdentifierPsiNode, StatementPsiNode}
import verilog.{VerilogFileType, VerilogKeywords}

import com.intellij.codeInsight.completion.{CompletionContributor, CompletionType}
import com.intellij.patterns.PlatformPatterns
import com.intellij.patterns.StandardPatterns.{and, not}

class VerilogCompletionContributor extends CompletionContributor {
  extend(CompletionType.BASIC,
    not(PlatformPatterns.psiElement.inside(classOf[DirectivePsiNode])),
    new KeywordCompletionProvider(VerilogKeywords.VERILOG_KEYWORDS, VerilogFileType.DefaultIcon))
  extend(CompletionType.BASIC,
    PlatformPatterns.psiElement.inside(classOf[DirectivePsiNode]).afterLeaf("`"),
    new KeywordCompletionProvider(Array[String]("timescale", "include", "default_nettype"),
      VerilogFileType.DefaultIcon))
  extend(CompletionType.BASIC,
    and(PlatformPatterns.psiElement.afterLeaf(";"),
      not(PlatformPatterns.psiElement.inside(classOf[StatementPsiNode]))),
    new ModuleItemOuterReferenceCompletionProvider)
  // private val namedPortConnectionPattern = PlatformPatterns.psiElement()
  //   .inside(classOf[NamedPortConnectionPsiNode])
  //   .inside(classOf[PortIdentifierPsiNode])
  // extend(CompletionType.BASIC,
  //   namedPortConnectionPattern,
  //   new NamedPortConnectionCompletionProvider)
}
