package top.scaleda.kernel.language
import org.antlr.v4.runtime.{CharStream, Lexer, Parser, ParserRuleContext, TokenStream}
import top.scaleda.tcl.parser.{TclLexer, TclParser}

object LangTcl extends AbstractLanguage {

  override def getName: String = "Tcl"

  override def getExtensions: Seq[String] = Seq("tcl")

  override def lexer(charStream: CharStream): Lexer = new TclLexer(charStream)

  override def parser(tokenStream: TokenStream): Parser = new TclParser(tokenStream)

  override def ast(parser: Parser): ParserRuleContext = parser.asInstanceOf[TclParser].source_text()
}
