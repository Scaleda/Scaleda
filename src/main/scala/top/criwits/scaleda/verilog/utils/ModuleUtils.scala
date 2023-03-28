package top.criwits.scaleda
package verilog.utils

import verilog.parser.{VerilogLexer, VerilogParser, VerilogParserBaseListener}

import org.antlr.v4.runtime.tree.IterativeParseTreeWalker
import org.antlr.v4.runtime.{CharStream, CharStreams, CommonTokenStream}

import java.io.{File, FileInputStream}
import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

object ModuleUtils {

  private def parseVerilogASTFromCharStream(charStream: CharStream): VerilogParser.Source_textContext = {
    val lexer  = new VerilogLexer(charStream)
    val tokens = new CommonTokenStream(lexer)
    val parser = new VerilogParser(tokens)
    val tree   = parser.source_text()
    tree
  }

  /** Parse a Verilog file and return AST
    * @param file verilog file
    * @return AST
    */
  def parseVerilogFileAST(file: File): VerilogParser.Source_textContext = {
    val stream     = new FileInputStream(file)
    val charStream = CharStreams.fromStream(stream)
    stream.close()
    parseVerilogASTFromCharStream(charStream)
  }

  /** Parse a Verilog code (text) and return AST
    * @param code source code text
    * @return AST
    */
  def parseVerilogCodeAST(code: String): VerilogParser.Source_textContext = {
    val charStream = CharStreams.fromString(code)
    parseVerilogASTFromCharStream(charStream)
  }

  /** Parse a Verilog file and return module names and instances in each module
    * @param tree AST
    * @return modules info
    */
  def parseVerilogASTModules(tree: VerilogParser.Source_textContext): Map[String, Seq[String]] = {
    val walker      = new IterativeParseTreeWalker
    val modulesInfo = new mutable.HashMap[String, Seq[String]]()
    val listener = new VerilogParserBaseListener() {
      var moduleNow = ""
      var instances = ArrayBuffer[String]()

      override def exitModule_head(ctx: VerilogParser.Module_headContext) = {
        moduleNow = ctx.module_identifier().identifier().Simple_identifier().getText
        println(s"start $moduleNow")
      }

      override def exitModule_instantiation(ctx: VerilogParser.Module_instantiationContext) = {
        val name = ctx.module_identifier().identifier().Simple_identifier().getText
        instances += name
        println(s"instantiation: ${name}")
      }

      override def exitModule_declaration(ctx: VerilogParser.Module_declarationContext) = {
        println(s"$moduleNow done: ${ctx.getText}")
        modulesInfo(moduleNow) = instances.toSeq
        instances.clear()
      }
    }
    walker.walk(listener, tree)
    modulesInfo.toMap
  }

  /** Parse a Verilog file and return module names and instances in each module
    * @param code Source Code
    * @return modules info
    */
  def parseVerilogCodeModules(code: String) = {
    val tree = parseVerilogCodeAST(code)
    parseVerilogASTModules(tree)
  }

  /** Parse a Verilog file and return module names and instances in each module
    * @param file Source code file
    * @return modules info
    */
  def parseVerilogFileModules(file: File) = {
    val tree = parseVerilogFileAST(file)
    parseVerilogASTModules(tree)
  }
}
