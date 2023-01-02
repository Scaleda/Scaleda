package top.criwits.scaleda
package kernel.shell

import scopt.OParser

import java.io.File

object ShellRunMode extends Enumeration {
  val None, Simulation = Value
}

case class ShellArgs
(workingDir: File = new File("."),
 runMode: ShellRunMode.Value = ShellRunMode.None,
 runSimulation: String = "")

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
        cmd("sim")
          .action((_, c) => c.copy(runMode = ShellRunMode.Simulation))
          .children(
            opt[String]('s', "simulator")
              .action((x, c) => c.copy(runSimulation = x))
              .text(s"Run simulation"),
//            .text(s"Run simulation, available: ${Simulator.simulators.keys.mkString(", ")}"),
          ),
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
