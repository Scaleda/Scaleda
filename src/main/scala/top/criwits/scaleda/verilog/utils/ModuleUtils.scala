package top.criwits.scaleda
package verilog.utils

import kernel.utils.KernelFileUtils
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
      }

      override def exitModule_instantiation(ctx: VerilogParser.Module_instantiationContext) = {
        val name = ctx.module_identifier().identifier().Simple_identifier().getText
        instances += name
      }

      override def exitModule_declaration(ctx: VerilogParser.Module_declarationContext) = {
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

  /** Get maybe top module from source sets, will parse all source code
    * @param sources source set
    * @return optional top module
    */
  def parseSourceSetTopModules(sources: Set[String]): Set[String] = {
    val files = KernelFileUtils.getAllSourceFiles(sources)
    val fileModuleInfo: Map[String, Seq[String]] =
      files.map(parseVerilogFileModules).reduceOption(_ ++ _).getOrElse(Map())
    class ModuleNode(val module: String, val children: Set[String]) {
      var parent: ModuleNode = this

      override def equals(obj: Any) = obj match {
        case o: ModuleNode => o.module == module
        case _             => super.equals(obj)
      }

      override def toString: String = s"Module[$module]"
    }
    val modules = fileModuleInfo.map(a => new ModuleNode(a._1, a._2.toSet))
    // Use simple Union-Find algorithm
    def find(node: ModuleNode): ModuleNode = if (node.parent == node) node else find(node.parent)
    def merge(a: ModuleNode, b: ModuleNode) = {
      val p = find(a)
      a.parent = p
      b.parent = p
    }
    var tops: Set[ModuleNode] = modules.map(find).toSet
    var lastTopCount          = 0
    while (tops.size > 1 && lastTopCount != tops.size) {
      // find children and merge them
      tops.foreach(t => t.children.map(name => modules.find(_.module == name).foreach(c => merge(t, c))))
      lastTopCount = tops.size
      tops = modules.map(find).toSet
    }
    tops.map(_.module)
  }
}
