package top.criwits.scaleda
package verilog.completion

import verilog.psi.factory.nodes.{DirectivePsiNode, NamedPortConnectionPsiNode, StatementPsiNode}

import com.intellij.codeInsight.completion.{CompletionContributor, CompletionType}
import com.intellij.patterns.PlatformPatterns
import com.intellij.patterns.StandardPatterns.{and, not}
import top.criwits.scaleda.verilog.psi.factory.nodes.module.PortIdentifierPsiNode
import top.criwits.scaleda.verilog.{VerilogFileType, VerilogKeywords}

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
  // TODO: more contributors
  // private val namedPortConnectionPattern = PlatformPatterns.psiElement()
  //   .inside(classOf[NamedPortConnectionPsiNode])
  //   .inside(classOf[PortIdentifierPsiNode])
  // extend(CompletionType.BASIC,
  //   namedPortConnectionPattern,
  //   new NamedPortConnectionCompletionProvider)
  // extend(CompletionType.BASIC, not(namedPortConnectionPattern), new LocalReferenceCompletionProvider)
}
