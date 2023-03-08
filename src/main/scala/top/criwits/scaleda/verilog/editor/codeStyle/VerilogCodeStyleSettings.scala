package top.criwits.scaleda
package verilog.editor.codeStyle

import com.intellij.psi.codeStyle.{CodeStyleSettings, CustomCodeStyleSettings}
import top.criwits.scaleda.verilog.editor.codeStyle.VerilogCodeStyleSettings._

class VerilogCodeStyleSettings(container: CodeStyleSettings) extends CustomCodeStyleSettings("Verilog", container) {
  val LINE_BREAK_PENALTY: Int = 2
  val OVER_COLUMN_LIMIT_PENALTY: Int = 100
  val WRAP_SPACES: Int = 4

  val ASSIGNMENT_STATEMENT_ALIGNMENT = INFER
  val CASE_ITEMS_ALIGNMENT = INFER
  val CLASS_MEMBER_VARIABLE_ALIGNMENT = INFER
  val COMPACT_INDEXING_AND_SELECTION = true
  val DISTRIBUTION_ITEMS_ALIGNMENT = INFER
  val ENUM_ASSIGNMENT_STATEMENT_ALIGNMENT = INFER
  val EXPAND_COVERPOINTS = false
  val FORMAL_PARAMETERS_ALIGNMENT = INFER
  val FORMAL_PARAMETERS_INDENTATION = true
  val MODULE_NET_VARIABLE_ALIGNMENT = INFER
  val NAMED_PARAMETER_ALIGNMENT = INFER
  val NAMED_PARAMETER_INDENTATION = true
  val NAMED_PORT_ALIGNMENT = INFER
  val NAMED_PORT_INDENTATION = true
  val PORT_DECLARATIONS_ALIGNMENT = INFER
  val PORT_DECLARATIONS_INDENTATION = true
  val PORT_DRAPD = false
  val PORT_DRAUD = false
  val STRUCT_UNION_MEMBERS_ALIGNMENT = INFER
  val TRY_WRAP_LONG_LINES = false
}

object VerilogCodeStyleSettings extends Enumeration {
  val ALIGN = Value("align")
  val FLUSH_LEFT = Value("flush-left")
  val PRESERVE = Value("preserve")
  val INFER = Value("infer")
}