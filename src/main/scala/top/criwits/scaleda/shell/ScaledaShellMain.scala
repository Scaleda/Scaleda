package top.criwits.scaleda
package shell

import scopt.OParser

import java.io.File

case class ShellArgs
(workingDir: File = new File("."))

object ScaledaShellMain {
  def main(args: Array[String]): Unit = {
    println(s"Scaleda shell! args: ${args.mkString(" ")}")
    val builder = OParser.builder[ShellArgs]
    val parser = {
      import builder._
      OParser.sequence(
        programName("scaleda"),
        head("scaleda", "0.1"),
        opt[File]('C', "workdir")
          .action((x, c) => c.copy(workingDir = x))
          .text("Working directory"),
        help("help").text("Prints this usage text"),
      )
    }
    OParser.parse(parser, args, ShellArgs()) match {
      case Some(config) => {
        println(s"config: ${config}")
      }
      case _ => {}
    }
  }
}
