package top.criwits.scaleda
package kernel.shell

import kernel.net.RemoteServer
import kernel.project.config.ProjectConfig
import kernel.toolchain.Toolchain
import kernel.utils.{JsonHelper, KernelLogger, Paths}

import scopt.OParser

import java.io.File

object ShellRunMode extends Enumeration {
  val None, Run, ListProfiles, ListTasks, Serve = Value
}

case class ShellArgs
(target: String = "",
 workingDir: File = new File("."),
 runMode: ShellRunMode.Value = ShellRunMode.None,
 serverHost: String = "",
 serverPort: Int = RemoteServer.port)

object ScaledaShellMain {
  private def loadConfig(projectRootPath: String): Unit = {
    val rootDir = new File(projectRootPath)
    if (rootDir.exists() && rootDir.isDirectory) {
      ProjectConfig.projectBase = Some(rootDir)
    }
    val projectConfigFile = new File(projectRootPath, ProjectConfig.defaultConfigFile)
    if (projectConfigFile.exists() && !projectConfigFile.isDirectory) {
      ProjectConfig.configFile = Some(projectConfigFile.getAbsolutePath)
      val config = ProjectConfig.getConfig()
      KernelLogger.info(s"project config: ${config}")
    }
  }

  def main(args: Array[String]): Unit = {
    KernelLogger.info(s"Scaleda shell! args: ${args.mkString(" ")}")
    // seek for -C
    var pathArgFound = false
    args.foreach(arg => {
      if (arg == "-C" || arg == "--workdir") {
        pathArgFound = true
      } else {
        if (pathArgFound) {
          loadConfig(arg)
          pathArgFound = false
        }
      }
    })
    if (ProjectConfig.configFile.isEmpty) {
      // try loading config in pwd
      loadConfig(Paths.pwd.getAbsolutePath)
    }
    if (ProjectConfig.configFile.isEmpty) KernelLogger.info("no project config detected!")
    val shellParser = {
      val builder = OParser.builder[ShellArgs]
      import builder._
      OParser.sequence(
        programName("scaleda"),
        head("scaleda", "0.1"),
        opt[File]('C', "workdir")
          .action((x, c) => c.copy(workingDir = x))
          .text("Working directory"),
        opt[String]("host")
          .action((x, c) => c.copy(serverHost = x))
          .text("Set server host for RPC"),
        opt[Int]("port")
          .action((x, c) => c.copy(serverPort = x))
          .text("Set server port for RPC"),
        cmd("serve")
          .text("Run as server")
          .action((_, c) => c.copy(runMode = ShellRunMode.Serve)),
        cmd("profiles")
          .text("Show loaded profiles")
          .action((_, c) => c.copy(runMode = ShellRunMode.ListProfiles)),
        cmd("tasks")
          .text("Show loaded tasks")
          .action((_, c) => c.copy(runMode = ShellRunMode.ListTasks)),
        cmd("run")
          .text("Run target")
          .action((_, c) => c.copy(runMode = ShellRunMode.Run))
          .children(
            opt[String]('t', "target")
              .action((x, c) => c.copy(target = x))
              .text(s"Available targets: ${
                ProjectConfig.getConfig()
                  .map(config => config.targetNames.mkString(", "))
                  .getOrElse("None")
              }")
              .validate(name => if (ProjectConfig.getConfig().exists(c => c.targetNames.contains(name)))
                success else failure(s"no target ${name} found!")),
          ),
        help("help").text("Prints this usage text"),
      )
    }
    OParser.parse(shellParser, args, ShellArgs()).foreach(shellConfig => {
      val workingDir = shellConfig.workingDir
      val config = ProjectConfig.getConfig()
      KernelLogger.info(s"shell config: ${shellConfig}")
      shellConfig.runMode match {
        case ShellRunMode.ListProfiles => {
          KernelLogger.info("profile list:")
          for (p <- Toolchain.profiles()) {
            KernelLogger.info(s"${JsonHelper(p)}")
          }
        }
        case ShellRunMode.ListTasks => {
          KernelLogger.info("task list:")
          ProjectConfig.getConfig().map(config =>
            for (p <- config.tasks) {
              KernelLogger.info(s"${JsonHelper(p)}")
            }).getOrElse(KernelLogger.info("no task loaded"))
        }
        case ShellRunMode.Serve => {
          // run as server
          RemoteServer.start()
        }
        case ShellRunMode.Run => {
          config.map(c => {
            ProjectConfig.getConfig().foreach(_.targetByName(shellConfig.target).map(f => {
              val (task, target) = f
              ScaledaRun.runTask(workingDir, task, target)
            }).getOrElse(KernelLogger.error("no specific target!")))
          }).getOrElse(KernelLogger.error("no config loaded!"))
        }
        case _ => {
          KernelLogger.error("not implemented.")
        }
      }
    })
  }
}
