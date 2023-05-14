package top.scaleda
package verilog.editor.codeStyle

import com.intellij.psi.codeStyle.{CodeStyleSettings, CustomCodeStyleSettings}
import top.scaleda.verilog.editor.codeStyle.VerilogCodeStyleSettings.INFER

class VerilogCodeStyleSettings(container: CodeStyleSettings) extends CustomCodeStyleSettings("Verilog", container) {
  var LINE_BREAK_PENALTY: Int = 2
  var OVER_COLUMN_LIMIT_PENALTY: Int = 100
  var WRAP_SPACES: Int = 4

  var ASSIGNMENT_STATEMENT_ALIGNMENT = INFER
  var CASE_ITEMS_ALIGNMENT = INFER
  var CLASS_MEMBER_VARIABLE_ALIGNMENT = INFER
  var COMPACT_INDEXING_AND_SELECTION = true
  var DISTRIBUTION_ITEMS_ALIGNMENT = INFER
  var ENUM_ASSIGNMENT_STATEMENT_ALIGNMENT = INFER
  var EXPAND_COVERPOINTS = false
  var FORMAL_PARAMETERS_ALIGNMENT = INFER
  var FORMAL_PARAMETERS_INDENTATION = true
  var MODULE_NET_VARIABLE_ALIGNMENT = INFER
  var NAMED_PARAMETER_ALIGNMENT = INFER
  var NAMED_PARAMETER_INDENTATION = true
  var NAMED_PORT_ALIGNMENT = INFER
  var NAMED_PORT_INDENTATION = true
  var PORT_DECLARATIONS_ALIGNMENT = INFER
  var PORT_DECLARATIONS_INDENTATION = true
  var PORT_DRAPD = false
  var PORT_DRAUD = false
  var STRUCT_UNION_MEMBERS_ALIGNMENT = INFER
  var TRY_WRAP_LONG_LINES = false
}

object VerilogCodeStyleSettings extends Enumeration {
  val ALIGN = Value("align")
  val FLUSH_LEFT = Value("flush-left")
  val PRESERVE = Value("preserve")
  val INFER = Value("infer")
}