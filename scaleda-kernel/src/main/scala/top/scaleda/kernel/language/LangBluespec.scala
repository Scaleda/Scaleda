package top.scaleda.kernel.language
import org.antlr.v4.runtime.{CharStream, Lexer, Parser, ParserRuleContext, TokenStream}
import top.scaleda.bluespec.parser.{BluespecLexer, BluespecParser}

object LangBluespec extends AbstractLanguage {

  override def getName: String = "Bluespec"

  override def getExtensions: Seq[String] = Seq("bsv")

  override def lexer(charStream: CharStream): Lexer = new BluespecLexer(charStream)

  override def parser(tokenStream: TokenStream): Parser = new BluespecParser(tokenStream)

  override def ast(parser: Parser): ParserRuleContext = parser.asInstanceOf[BluespecParser].top()
}
