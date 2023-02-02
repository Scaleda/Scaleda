package top.criwits.scaleda
package idea.settings.editor

import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighter
import com.intellij.openapi.options.colors.{AttributesDescriptor, ColorDescriptor, ColorSettingsPage}
import top.criwits.scaleda.idea.utils.Icons
import top.criwits.scaleda.verilog.highlight.VerilogLexerSyntaxHighlighter

import java.util
import javax.swing.Icon
import scala.jdk.CollectionConverters._

class VerilogColorSettingsPage extends ColorSettingsPage {
  override def getIcon: Icon = Icons.verilog

  override def getHighlighter: SyntaxHighlighter = new VerilogLexerSyntaxHighlighter

  override def getDemoText: String = "" // TODO

  override def getAdditionalHighlightingTagToDescriptorMap: util.Map[String, TextAttributesKey] = Map("" +
    "module_identifier" -> VerilogLexerSyntaxHighlighter.VERILOG_MODULE_IDENTIFIER).asJava

  override def getAttributeDescriptors: Array[AttributesDescriptor] = Array(
    new AttributesDescriptor("Directive", VerilogLexerSyntaxHighlighter.VERILOG_DIRECTIVE)
  )

  override def getColorDescriptors: Array[ColorDescriptor] = ColorDescriptor.EMPTY_ARRAY

  override def getDisplayName: String = "Verilog"
}
