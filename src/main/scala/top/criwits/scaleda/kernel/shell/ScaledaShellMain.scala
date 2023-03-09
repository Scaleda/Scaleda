package top.criwits.scaleda
package kernel.shell

import kernel.bin.ExtractAssets
import kernel.configuration.ScaledaKernelConfiguration
import kernel.database.dao.User
import kernel.net.RemoteClient
import kernel.net.remote.Empty
import kernel.net.user.ScaledaRegisterLogin
import kernel.project.config.ProjectConfig
import kernel.server.ScaledaServerMain
import kernel.template.Template
import kernel.toolchain.Toolchain
import kernel.utils.serialise.JSONHelper
import kernel.utils.{EnvironmentUtils, KernelLogger, Paths, ScaledaClean}

import scopt.OParser

import java.io.File

object ShellRunMode extends Enumeration {
  val None, Install, Run, ListProfiles, ListTasks, ListConfigurations, Serve, Clean, Login, Register, RefreshToken =
    Value
}

case class ShellArgs(
    taskName: String = "",
    targetName: String = "",
    workingDir: File = new File("."),
    runMode: ShellRunMode.Value = ShellRunMode.None,
    serverHost: String = "",
    profileName: String = "",
    user: User = new User("", "", ""),
    configureName: String = ""
)

object ScaledaShellMain {
  private def loadConfig(projectRootPath: String): Unit = {
    val rootDir = new File(projectRootPath)
    if (rootDir.exists() && rootDir.isDirectory) {
      ProjectConfig.projectBase = Some(rootDir.getAbsolutePath)
    }
    val projectConfigFile =
      new File(projectRootPath, ProjectConfig.defaultConfigFile)
    if (projectConfigFile.exists() && !projectConfigFile.isDirectory) {
      ProjectConfig.configFile = Some(projectConfigFile.getAbsolutePath)
      val config = ProjectConfig.getConfig()
      KernelLogger.info(s"project config: $config")
    }
  }

  private def preParseArgs(
      args: Array[String],
      option: Seq[String]
  ): Option[String] = {
    var found = false
    args.foreach(arg => {
      if (option.contains(arg))
        found = true
      else if (found)
        return Some(arg)
    })
    None
  }

  def main(args: Array[String]): Unit = {
    KernelLogger.info(s"This is Scaleda shell! Args: ${args.mkString(" ")}")
    Template.initJinja()
    // invoke Env backup
    val _ = EnvironmentUtils.Backup

    // install binaries
    if (!ExtractAssets.isInstalled) {
      ExtractAssets.run()
      KernelLogger.info("All assets ready")
    }

    // preparse workdir
    preParseArgs(args, Seq("-C", "--workdir")).foreach(a => loadConfig(_))
    // preparse server host
    // val host = preParseArgs(args, Seq("-h", "--host"))
    if (ProjectConfig.configFile.isEmpty) {
      // try loading config in pwd
      loadConfig(Paths.pwd.getAbsolutePath)
    }
    if (ProjectConfig.configFile.isEmpty)
      KernelLogger.info("No project config detected!")

    val shellParser = {
      val builder = OParser.builder[ShellArgs]
      import builder._
      OParser.sequence(
        programName("scaleda"),
        head("scaleda", "0.1"),
        opt[File]('C', "workdir")
          .action((x, c) => c.copy(workingDir = x))
          .text("Working directory"),
        opt[String]('h', "host")
          .action((x, c) => c.copy(serverHost = x))
          .text("Set server host for RPC"),
        opt[String]('u', "username")
          .action((x, c) => {
            val user = c.user
            user.setUsername(x)
            c.copy(user = user)
          })
          .text("Specify username"),
        opt[String]('p', "password")
          .action((x, c) => {
            val user = c.user
            user.setPassword(x)
            c.copy(user = user)
          })
          .text("Specify password"),
        cmd("install")
          .text("Install necessary binaries force")
          .action((_, c) => c.copy(runMode = ShellRunMode.Install)),
        cmd("serve")
          .text("Run as server")
          .action((_, c) => c.copy(runMode = ShellRunMode.Serve)),
        cmd("clean")
          .text("Clean all data on device")
          .action((_, c) => c.copy(runMode = ShellRunMode.Clean)),
        cmd("login")
          .text("Login into server")
          .action((_, c) => c.copy(runMode = ShellRunMode.Login)),
        cmd("register")
          .text("Create account in server")
          .action((_, c) => c.copy(runMode = ShellRunMode.Register))
          .children(
            opt[String]('n', "nickname")
              .action((x, c) => {
                val user = c.user
                user.setNickname(x)
                c.copy(user = user)
              })
              .text("Specify nickname")
          ),
        cmd("refresh")
          .text("Refresh token")
          .action((_, c) => c.copy(runMode = ShellRunMode.RefreshToken)),
        cmd("profiles")
          .text("Show loaded profiles")
          .action((_, c) => c.copy(runMode = ShellRunMode.ListProfiles)),
        cmd("tasks")
          .text("Show loaded tasks")
          .action((_, c) => c.copy(runMode = ShellRunMode.ListTasks)),
        cmd("configurations")
          .text("Show all configurations to run")
          .action((_, c) => c.copy(runMode = ShellRunMode.ListConfigurations)),
        cmd("run")
          .text("Run task")
          .action((_, c) => c.copy(runMode = ShellRunMode.Run))
          .children(
            opt[String]('c', "config")
              .action((x, c) => c.copy(configureName = x))
              .text("Specify configure"),
            opt[String]('t', "task")
              .action((x, c) => c.copy(taskName = x))
              .text("Specify the task"),
            opt[String]('r', "target")
              .action((x, c) => c.copy(targetName = x))
              .text("Specify the target, otherwise will auto fill"),
            opt[String]('p', "profile")
              .action((x, c) => c.copy(profileName = x))
              .text("Specify profile name, otherwise will auto fill")
          ),
        help("help").text("Prints this usage text")
      )
    }
    OParser
      .parse(shellParser, args, ShellArgs())
      .foreach(shellConfig => {
        val stub =
          if (shellConfig.serverHost.nonEmpty)
            Some(RemoteClient(shellConfig.serverHost))
          else None
        val workingDir = shellConfig.workingDir
        val config     = ProjectConfig.getConfig()
        KernelLogger.info(s"shell config: $shellConfig")
        shellConfig.runMode match {
          case ShellRunMode.ListProfiles =>
            KernelLogger.info("local profile list:")
            for (p <- Toolchain.profiles()) {
              KernelLogger.info(s"${JSONHelper(p)}")
            }
            {
              stub.foreach(stub => {
                val (client, _) = stub
                val profiles    = client.getProfiles(Empty())
                if (profiles.profiles.nonEmpty) {
                  KernelLogger.info("remote profile list:")
                  for (p <- profiles.profiles)
                    KernelLogger.info(s"${JSONHelper(p)}")
                }
              })
            }
          case ShellRunMode.ListTasks =>
            KernelLogger.info("task list:")
            ProjectConfig
              .getConfig()
              .map(config =>
                for (p <- config.targets.flatMap(_.tasks)) {
                  KernelLogger.info(s"${JSONHelper(p)}")
                }
              )
              .getOrElse(KernelLogger.info("no task loaded"))
          case ShellRunMode.ListConfigurations =>
            KernelLogger.info("configurations:")
            KernelLogger.info(ScaledaKernelConfiguration.configurations)
          case ShellRunMode.Run =>
            ProjectConfig
              .getConfig()
              .foreach(c => {
                var profileHost = shellConfig.serverHost
                var taskName    = shellConfig.taskName
                var targetName  = shellConfig.targetName
                // auto in ScaledaRun
                var profileName    = shellConfig.profileName
                val configureName  = shellConfig.configureName
                val configurations = ScaledaKernelConfiguration.configurations
                if (configurations.contains(configureName)) {
                  val configuration = configurations(configureName)
                  taskName = configuration.configuration.scaleda.taskName
                  targetName = configuration.configuration.scaleda.targetName
                  profileHost = configuration.configuration.scaleda.profileHost
                  profileName = configuration.configuration.scaleda.profileName
                } else {
                  if (taskName.isEmpty) {
                    if (c.taskNames.length > 1) KernelLogger.warn("Multiple tasks available")
                    // select first task
                    c.headTargetTask
                      .map(f => {
                        val (target, task) = f
                        targetName = target.name
                        taskName = task.name
                      })
                      .getOrElse(KernelLogger.error("No task available"))
                  }
                  if (targetName.isEmpty) {
                    // auto fill in target name
                    c.taskByName(taskName)
                      .map(f => {
                        val (target, _) = f
                        targetName = target.name
                      })
                      .getOrElse(KernelLogger.error("Task name error"))
                  }
                }
                // last check
                if (c.taskByTaskTargetName(taskName, targetName).isEmpty) {
                  KernelLogger.error(s"Cannot find target-task: $targetName-$taskName")
                } else {
                  KernelLogger.info(s"Run target-task-profile-host: $targetName-$taskName-$profileName-$profileHost")
                  ScaledaRun
                    .generateRuntimeFromName(targetName, taskName, profileName, profileHost)
                    .map(rt => {
                      ScaledaRun
                        .runTask(
                          if (rt.profile.isRemoteProfile) ScaledaRunKernelRemoteHandler else ScaledaRunKernelHandler,
                          rt
                        )
                    })
                    .getOrElse(KernelLogger.error("Cannot generate runtime!"))
                }
              })
          case ShellRunMode.None =>
            KernelLogger.warn("no action specified, do nothing")
          case ShellRunMode.Install =>
            ExtractAssets.run()
          case ShellRunMode.Serve =>
            ScaledaServerMain.run(shellConfig)
          case ShellRunMode.Login =>
            require(shellConfig.serverHost.nonEmpty, "server host must provide")
            val reply = new ScaledaRegisterLogin(shellConfig.serverHost)
              .login(shellConfig.user.getUsername, shellConfig.user.getPassword)
            if (!reply.ok) {
              KernelLogger.error("Login failed:", reply.reason)
            }
          case ShellRunMode.Register =>
            require(shellConfig.serverHost.nonEmpty, "server host must provide")
            val reply = new ScaledaRegisterLogin(shellConfig.serverHost)
              .register(shellConfig.user)
            if (!reply.ok) {
              KernelLogger.error("Register failed:", reply.reason)
            }
          case ShellRunMode.Clean =>
            ScaledaClean.run()
          case ShellRunMode.RefreshToken =>
            if (new ScaledaRegisterLogin(shellConfig.serverHost).refreshAndStore())
              KernelLogger.info("Refresh token done")
            else KernelLogger.error("Refresh token failed")
          case _ =>
            KernelLogger.error("not implemented.")
        }

        // shutdown client
        stub.foreach(_._2())
      })
  }
}
