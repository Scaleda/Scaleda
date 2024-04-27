package top.scaleda.kernel.language
import org.antlr.v4.runtime.{CharStream, Lexer, Parser, ParserRuleContext, TokenStream}
import top.scaleda.systemverilog.parser.{SystemVerilogLexer, SystemVerilogParser}

object LangSystemVerilog extends AbstractLanguage {

  override def getName: String = "SystemVerilog"

  override def getExtensions: Seq[String] = Seq("sv")

  override def lexer(charStream: CharStream): Lexer = new SystemVerilogLexer(charStream)

  override def parser(tokenStream: TokenStream): Parser = new SystemVerilogParser(tokenStream)

  override def ast(parser: Parser): ParserRuleContext = parser.asInstanceOf[SystemVerilogParser].source_text()
}
