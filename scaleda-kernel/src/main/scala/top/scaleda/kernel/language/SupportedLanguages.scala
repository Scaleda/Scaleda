package top.scaleda.kernel.language

object SupportedLanguages {
  val all: Seq[AbstractLanguage] = Seq(
    LangVerilog,
    LangSystemVerilog,
    LangBluespec
  )
}
