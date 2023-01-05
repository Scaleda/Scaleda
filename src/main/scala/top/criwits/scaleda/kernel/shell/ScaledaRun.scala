package top.criwits.scaleda
package kernel.shell

import kernel.project.config.ProjectConfig
import kernel.project.task.{TargetConfig, TargetType, TaskConfig}
import kernel.shell.command.{CommandResponse, CommandRunner}
import kernel.toolchain.Toolchain
import kernel.toolchain.executor.{SimulationExecutor, SynthesisExecutor}
import kernel.toolchain.impl.{Vivado, VivadoTemplateRenderer}
import kernel.utils.KernelLogger

import java.io.File

object ScaledaRun {
  def runTask(handler: ScaledaRunHandler, workingDir: File, task: TaskConfig, target: TargetConfig) = {
    KernelLogger.info(s"runTask workingDir=${workingDir.getAbsoluteFile}")
    val config = ProjectConfig.getConfig().get
    val info = Toolchain.toolchains(task.toolchain)
    // find profile
    Toolchain.profiles().filter(p => p.toolchainType == task.toolchain).foreach(profile => {
      // generate executor
      val executor = target.getType match {
        case TargetType.Simulation =>
          SimulationExecutor(workingDir = new File(workingDir, ".sim"), topFile = new File(config.topFile), profile = profile)
        case TargetType.Synthesis =>
          SynthesisExecutor(workingDir = new File(workingDir, ".synth"), topFile = new File(config.topFile), profile = profile)
        case _ => ???
      }
      if (target.preset) {
        task.toolchain match {
          case Vivado.internalID => {
            val r = new VivadoTemplateRenderer(executor = executor, taskConfig = task)
            r.render()
          }
          case _ => KernelLogger.error(s"not supported preset: ${task.toolchain}")
        }
      }
      val toolchain = info._2(executor)
      val commands = toolchain.commands(target.getType)
      CommandRunner.execute(commands, (commandRespType, data) => {
        commandRespType match {
          case CommandResponse.Stdout => handler.onStdout(data.asInstanceOf[String])
          case CommandResponse.Stderr => handler.onStderr(data.asInstanceOf[String])
          case CommandResponse.Return => handler.onReturn(data.asInstanceOf[Int])
        }
      })
    })
  }
}


trait ScaledaRunHandler {
  def onStdout(data: String): Unit

  def onStderr(data: String): Unit

  def onReturn(returnValue: Int): Unit
}

object ScaledaRunKernelHandler extends ScaledaRunHandler {
  override def onStdout(data: String): Unit = KernelLogger.info(data)

  override def onStderr(data: String): Unit = KernelLogger.error(data)

  override def onReturn(returnValue: Int): Unit = KernelLogger.info(s"command done, returns ${returnValue}")
}