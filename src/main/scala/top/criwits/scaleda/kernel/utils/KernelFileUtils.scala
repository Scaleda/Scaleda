package top.criwits.scaleda
package kernel.utils

import kernel.project.config.ProjectConfig
import verilog.parser.{VerilogLexer, VerilogParser, VerilogParserBaseVisitor}

import org.antlr.v4.runtime.{CharStreams, CommonTokenStream}

import java.io.{File, FileInputStream, FileOutputStream, FilenameFilter}
import java.util
import scala.collection.mutable
import scala.collection.mutable.ListBuffer
import scala.io.Source

object KernelFileUtils {
  def getAllSourceFiles(
      sourceDir: File = new File(new File(ProjectConfig.projectBase.get).getAbsolutePath, ProjectConfig.config.source),
      suffixing: Set[String] = Set("v")
  ): Seq[File] =
    sourceDir
      .listFiles(new FilenameFilter {
        override def accept(file: File, s: String) =
          suffixing
            .map(suffix => s.endsWith(s".${suffix}"))
            .reduceOption((a, b) => a || b)
            .getOrElse(false)
      })
      .toList

  def getAllTestFiles(): Seq[File] = getAllSourceFiles(
    new File(new File(ProjectConfig.projectBase.get).getAbsolutePath, ProjectConfig.config.test)
  )

  def getAbsolutePath(path: String, projectBase: Option[String] = ProjectConfig.projectBase): Option[String] = {
    val file = new File(path)
    file.isAbsolute match {
      case true =>
        projectBase match {
          case Some(base) =>
            Some(new File(new File(base), path).getAbsolutePath)
          case None => None
        }
      case false => Some(file.getAbsolutePath)
    }
  }

  def getModuleTitle(verilogFile: File): Seq[String] = {
    val stream     = new FileInputStream(verilogFile)
    val charStream = CharStreams.fromStream(stream)
    stream.close()
    val lexer  = new VerilogLexer(charStream)
    val tokens = new CommonTokenStream(lexer)
    val parser = new VerilogParser(tokens)
    val tree   = parser.source_text()

    class ModuleIdentifierVisitor extends VerilogParserBaseVisitor[String] {
      val title = new ListBuffer[String]

      override def visitModule_identifier(ctx: VerilogParser.Module_identifierContext): String = {
        val identifier = ctx.identifier()
        if (identifier != null) {
          val simpleIdentifier = identifier.Simple_identifier()
          if (simpleIdentifier != null) {
            val moduleName = simpleIdentifier.getText
            title += simpleIdentifier.getText
            moduleName
          } else null
        } else null
      }
    }

    val visitor = new ModuleIdentifierVisitor
    visitor.visit(tree)
    visitor.title.toSeq
  }

  def getModuleFile(module: String, testbench: Boolean = false): Option[File] = {
    (if (testbench) getAllTestFiles() else getAllSourceFiles()).foreach(f => {
      val matched = getModuleTitle(f).filter(_ == module)
      if (matched.nonEmpty) {
        return Some(f) // FIXME
      } else None
    })
    None
  }

  def insertAfterModuleHead(original: File, output: File, moduleName: String, insert: String): Unit = {
    val stream     = new FileInputStream(original)
    val charStream = CharStreams.fromStream(stream)
    stream.close()
    val lexer  = new VerilogLexer(charStream)
    val tokens = new CommonTokenStream(lexer)
    val parser = new VerilogParser(tokens)
    val tree   = parser.source_text()

    class ModuleHeadVisitor(val moduleName: String) extends VerilogParserBaseVisitor[Int] {
      var headerEndAt: Int = -1

      override def visitModule_head(ctx: VerilogParser.Module_headContext): Int= {
        val identifier = ctx.module_identifier()
        if (identifier != null) {
          val _identifier = identifier.identifier()
          if (_identifier != null) {
            val simpleIdentifier = _identifier.Simple_identifier()
            if (simpleIdentifier != null) {
              if (simpleIdentifier.getText == moduleName) {
                val line = ctx.getStop.getStopIndex
                headerEndAt = line
                return line
              }
            }
          }
        }

        -1
      }
    }

    val visitor = new ModuleHeadVisitor(moduleName)
    visitor.visit(tree)

    val source = Source.fromFile(original)
    val newText =new mutable.StringBuilder(source.mkString).insert(visitor.headerEndAt + 1,
      insert).toString()
    source.close()

    val outputStream = new FileOutputStream(output)
    outputStream.write(newText.getBytes())
    outputStream.close()
  }
}
