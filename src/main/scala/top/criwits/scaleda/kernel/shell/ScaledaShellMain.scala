package top.criwits.scaleda
package kernel.shell

import kernel.project.config.ProjectConfig
import kernel.toolchain.Toolchain
import kernel.utils.KernelLogger

import scopt.OParser

import java.io.File

object ShellRunMode extends Enumeration {
  val None, Simulation, ListProfiles, Serve = Value
}

case class ShellArgs
(workingDir: File = new File("."),
 runMode: ShellRunMode.Value = ShellRunMode.None,
 runSimulation: String = "iverilog")

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
        cmd("serve")
          .action((_, c) => c.copy(runMode = ShellRunMode.Serve)),
        cmd("profiles")
          .action((_, c) => c.copy(runMode = ShellRunMode.ListProfiles)),
        cmd("sim")
          .action((_, c) => c.copy(runMode = ShellRunMode.Simulation))
          .children(
            opt[String]('s', "simulator")
              .action((x, c) => c.copy(runSimulation = x))
            // .text(s"Run simulation, available: ${Simulator.simulators.keys.mkString(", ")}"),
            ,
            // opt[String]("top")
            //   .action((x, c) => c.copy(simulationConfig = c.simulationConfig.copy(topModule = x)))
            //   .text("Specify top module")
          ),
        cmd("synth"),
        cmd("impl"),
        cmd("debug"),
        cmd("program"),
        help("help").text("Prints this usage text"),
      )
    }
    OParser.parse(parser, args, ShellArgs()) match {
      case Some(shellConfig) => {
        println(s"shell config: ${shellConfig}")
        val projectConfigFile = new File(shellConfig.workingDir, ProjectConfig.defaultConfigFile)
        if (projectConfigFile.exists() && !projectConfigFile.isDirectory) {
          import ProjectConfig.config
          ProjectConfig.setConfig(projectConfigFile)
          println(s"project config: ${config}")
        } else {
          println("no project config detected!")
        }
        shellConfig.runMode match {
          case ShellRunMode.ListProfiles => {
            KernelLogger.info("profile list:")
            for (p <- Toolchain.profiles()) {
              KernelLogger.info(s"${p}")
            }
          }
          case ShellRunMode.Serve => {
            // run as server
          }
          // case ShellRunMode.Simulation => {
          //   if (!Simulator.simulators.keys.toSeq.contains(shellConfig.runSimulation)) {
          //     println(s"not supported simulator ${shellConfig.runSimulation}")
          //   } else {
          //     val simulator = Simulator.simulators(shellConfig.runSimulation)(
          //       shellConfig.simulationConfig.copy(
          //         sourceDir = config.getSourcePath(shellConfig.workingDir),
          //         workingDir = new File(shellConfig.workingDir.getAbsolutePath, shellConfig.simulationConfig.workingDir.getPath)
          //       ))
          //     val returnValue = simulator.simulate()
          //   }
          // }
          case _ => {
            println("not implemented.")
          }
        }
      }
      case _ => {}
    }
  }
}
