package top.scaleda
package verilog.completion.keywords

import com.intellij.codeInsight.completion._
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.filters.position.FilterPattern
import com.intellij.psi.filters.{AndFilter, ElementFilter}
import com.intellij.util.ProcessingContext
import com.intellij.util.ui.EmptyIcon

/** Keyword completion contributor
  */
class VerilogKeywordCompletionContributor extends CompletionContributor {

  private def registerFor(
      filters: Seq[ElementFilter],
      keywords: String*
  ): Unit = extend(
    CompletionType.BASIC,
    PlatformPatterns.psiElement.and(new FilterPattern(new AndFilter(filters: _*))),
    new CompletionProvider[CompletionParameters] {
      override def addCompletions(
          parameters: CompletionParameters,
          context: ProcessingContext,
          resultSet: CompletionResultSet
      ): Unit =
        for {
          keyword <- keywords
          element = LookupElementBuilder
            .create(keyword)
            .withBoldness(true)
            .withIcon(EmptyIcon.create(16, 16)) // TODO: Insert handler
        } resultSet.addElement(element)
    }
  )

  private def registerStandardCompletion(
      filter: ElementFilter,
      keywords: String*
  ): Unit = registerFor(
    Seq(filter),
    keywords: _*
  )

  registerStandardCompletion(new ModuleFilter, "module", "macromodule")
  registerStandardCompletion(new ModuleParameterFilter, "parameter")
  registerStandardCompletion(new ModulePortDirectionFilter, "input", "output", "inout")
  registerStandardCompletion(new EndmoduleFilter, "endmodule")

  registerStandardCompletion(
    new ModuleItemFilter,
    "always",
    "initial",
    "generate",
    "reg",
    "wire",
    "integer",
    "real",
    "realtime",
    "time",
    "genvar",
    "parameter",
    "localparam",
    "assign",
    "function"
  )

  registerStandardCompletion(new ParameterAttributeFilter, "signed", "integer", "real", "realtime", "time")
  registerStandardCompletion(new OutputRegFilter, "reg")
  registerStandardCompletion(new PosNegedgeFilter, "posedge", "negedge")
  registerStandardCompletion(new EndcaseFilter, "endcase")

}
