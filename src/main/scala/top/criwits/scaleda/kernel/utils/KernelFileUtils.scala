package top.criwits.scaleda
package kernel.utils

import kernel.project.config.ProjectConfig
import verilog.parser.{VerilogLexer, VerilogParser, VerilogParserBaseVisitor}

import org.antlr.v4.runtime.{CharStreams, CommonTokenStream}

import java.io._
import java.nio.ByteBuffer
import java.nio.channels.FileChannel
import java.security.{MessageDigest, NoSuchAlgorithmException}
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

      override def visitModule_head(ctx: VerilogParser.Module_headContext): Int = {
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

    val source  = Source.fromFile(original)
    val newText = new mutable.StringBuilder(source.mkString).insert(visitor.headerEndAt + 1, insert).toString()
    source.close()

    val outputStream = new FileOutputStream(output)
    outputStream.write(newText.getBytes())
    outputStream.close()
  }

  def getFileMD5(file: File) = {
    var in: FileInputStream = null
    try {
      in = new FileInputStream(file)
      val ch = in.getChannel
      MD5(ch.map(FileChannel.MapMode.READ_ONLY, 0, file.length))
    } catch {
      case e: FileNotFoundException =>
        ""
      case e: IOException =>
        ""
    } finally if (in != null)
      try {
        in.close()
      } catch {
        case e: IOException =>
        // 关闭流产生的错误一般都可以忽略
      }
  }

  private val hexDigits = Array('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f')

  private def MD5(buffer: ByteBuffer) = {
    var s = ""
    try {
      val md = MessageDigest.getInstance("MD5")
      md.update(buffer)
      val tmp = md.digest // MD5 的计算结果是一个 128 位的长整数，

      // 用字节表示就是 16 个字节
      val str = new Array[Char](16 * 2) // 每个字节用 16 进制表示的话，使用两个字符，

      // 所以表示成 16 进制需要 32 个字符
      var k = 0 // 表示转换结果中对应的字符位置

      for (i <- 0 until 16) { // 从第一个字节开始，对 MD5 的每一个字节
        // 转换成 16 进制字符的转换
        val byte0 = tmp(i) // 取第 i 个字节

        str({
          k += 1;
          k - 1
        }) = hexDigits(byte0 >>> 4 & 0xf) // 取字节中高 4 位的数字转换, >>>,

        // 逻辑右移，将符号位一起右移
        str({
          k += 1;
          k - 1
        }) = hexDigits(byte0 & 0xf) // 取字节中低 4 位的数字转换

      }
      s = new String(str) // 换后的结果转换为字符串
    } catch {
      case e: NoSuchAlgorithmException =>
        e.printStackTrace()
    }
    s
  }
}
