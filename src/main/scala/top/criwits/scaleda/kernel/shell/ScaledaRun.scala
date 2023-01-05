package top.criwits.scaleda
package kernel.shell

import kernel.project.task.{TargetConfig, TargetType, TaskConfig}

import top.criwits.scaleda.kernel.project.config.ProjectConfig
import top.criwits.scaleda.kernel.shell.command.{CommandResponse, CommandRunner}
import top.criwits.scaleda.kernel.toolchain.Toolchain
import top.criwits.scaleda.kernel.toolchain.executor.{SimulationExecutor, SynthesisExecutor}
import top.criwits.scaleda.kernel.toolchain.impl.{Vivado, VivadoTemplateRenderer}
import top.criwits.scaleda.kernel.utils.KernelLogger

import java.io.File

object ScaledaRun {
  def runTask(workingDir: File, task: TaskConfig, target: TargetConfig) = {
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
          case CommandResponse.Stdout => KernelLogger.info(data.asInstanceOf[String])
          case CommandResponse.Stderr => KernelLogger.error(data.asInstanceOf[String])
          case CommandResponse.Return => KernelLogger.info(s"command done, returns ${data.asInstanceOf[Int]}")
        }
      })
    })
  }
}
