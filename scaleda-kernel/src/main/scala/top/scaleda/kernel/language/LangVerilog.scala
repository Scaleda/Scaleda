package top.scaleda.kernel.language
import org.antlr.v4.runtime.{CharStream, Lexer, Parser, ParserRuleContext, TokenStream}
import top.scaleda.verilog.parser.{VerilogLexer, VerilogParser}

object LangVerilog extends AbstractLanguage {

  override def getName: String = "Verilog"

  override def getExtensions: Seq[String] = Seq("v")

  override def lexer(charStream: CharStream): Lexer = new VerilogLexer(charStream)

  override def parser(tokenStream: TokenStream): Parser = new VerilogParser(tokenStream)

  override def ast(parser: Parser): ParserRuleContext = parser.asInstanceOf[VerilogParser].source_text()
}
