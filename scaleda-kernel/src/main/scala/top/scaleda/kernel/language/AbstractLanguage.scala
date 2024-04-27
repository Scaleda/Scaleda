package top.scaleda.kernel.language

import org.antlr.v4.runtime.{CharStream, Lexer, Parser, ParserRuleContext, TokenStream}

trait AbstractLanguage {
  def getName: String
  def getDescription: String = getName
  def getExtensions: Seq[String]
  def getDefaultExtension: String = getExtensions.headOption.getOrElse("")
  def lexer(charStream: CharStream): Lexer
  def parser(tokenStream: TokenStream): Parser
  def ast(parser: Parser): ParserRuleContext
}
