package top.criwits.scaleda
package verilog.completion

import com.intellij.codeInsight.completion.{CompletionParameters, CompletionProvider, CompletionResultSet}
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.util.ProcessingContext

import javax.swing.Icon

class KeywordCompletionProvider(val keywords: Array[String], val icon: Icon)
  extends CompletionProvider[CompletionParameters] {
  override def addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet): Unit =
    keywords.foreach(keyword => result.addElement(LookupElementBuilder.create(keyword).bold().withIcon(icon)))
}
