package top.scaleda
package systemverilog

import kernel.language.LangSystemVerilog

import com.intellij.lang.Language

class SystemVerilogLanguage extends Language(LangSystemVerilog.getName)
object SystemVerilogLanguage {
  val INSTANCE = new SystemVerilogLanguage
}
