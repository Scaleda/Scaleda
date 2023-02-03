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

  override def getDemoText: String = "`timescale 1ns / 1ps\n" +
"\n" +
"module full_adder #(\n" +
"    parameter SOURCE_WIDTH = 32, RESULT_WIDTH = 32\n" +
") (\n" +
"    input clk,\n" +
"    input rst,\n" +
"    input [SOURCE_WIDTH - 1:0] a,\n" +
"    input [SOURCE_WIDTH - 1:0] b,\n" +
"    output reg [RESULT_WIDTH - 1:0] c\n" +
");\n" +
"\n" +
"    wire [RESULT_WIDTH - 1:0] a_regulated;\n" +
"    wire [RESULT_WIDTH - 1:0] b_regulated;\n" +
"\n" +
"    regulator #(SOURCE_WIDTH, RESULT_WIDTH) u_regulator_a (\n" +
"        .in (a),\n" +
"        .out (a_regulated)\n" +
"    );\n" +
"\n" +
"    regulator #(SOURCE_WIDTH, RESULT_WIDTH) u_regulator_b (\n" +
"        .in (b),\n" +
"        .out (b_regulated)\n" +
"    );\n" +
"\n" +
"    always @(posedge clk or posedge rst) begin\n" +
"        if (rst) c <= RESULT_WIDTH'h0;\n" +
"        else begin\n" +
"            c <= a_regulated + b_regulated;\n" +
"            $display(\"Calculated result: %x\n\", c);\n" +
"        end\n" +
"    end\n" +
"\n" +
"endmodule\n"  // TODO

  override def getAdditionalHighlightingTagToDescriptorMap: util.Map[String, TextAttributesKey] = Map("" +
    "module_identifier" -> VerilogLexerSyntaxHighlighter.VERILOG_MODULE_IDENTIFIER).asJava

  override def getAttributeDescriptors: Array[AttributesDescriptor] = Array(
    new AttributesDescriptor("Directive", VerilogLexerSyntaxHighlighter.VERILOG_DIRECTIVE)
  )

  override def getColorDescriptors: Array[ColorDescriptor] = ColorDescriptor.EMPTY_ARRAY

  override def getDisplayName: String = "Verilog"
}
