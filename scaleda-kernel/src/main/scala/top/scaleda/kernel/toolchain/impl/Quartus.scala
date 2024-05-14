package top.scaleda
package kernel.toolchain.impl

import top.scaleda.kernel.net.remote.RemoteInfo
import top.scaleda.kernel.project.ScaledaProject
import top.scaleda.kernel.project.config.{TaskConfig, TaskType}
import top.scaleda.kernel.shell.command.CommandDeps
import top.scaleda.kernel.template.ResourceTemplateRender
import top.scaleda.kernel.toolchain.executor.Executor
import top.scaleda.kernel.toolchain.runner.ScaledaRuntime
import top.scaleda.kernel.toolchain.{Toolchain, ToolchainPresetProvider}
import top.scaleda.kernel.utils._

import java.io.File
import scala.io.Source

/** Implementation of [[Toolchain]] for Intel Quartus Prime Standard.
  *
  * @param executor An [[Executor]] used to hold information like configurations.
  *
  * Common flow for Quartus Prime Standard:
  * quartus_map --read_settings_files=on --write_settings_files=off proj -c proj
  * quartus_fit --read_settings_files=off --write_settings_files=off proj -c proj
  * quartus_asm --read_settings_files=off --write_settings_files=off proj -c proj
  * quartus_sta proj -c proj
  * quartus_eda --read_settings_files=on --write_settings_files=off proj -c proj
  * quartus_pgm -m jtag -c 1 -o "p;output_files/proj.sof"
  */
class Quartus(executor: Executor) extends Toolchain(executor) {
  override def getInternalID: String = Quartus.internalID
  override def getName: String       = Quartus.userFriendlyName

  private def projectName(task: TaskConfig) = task.findTopModule.getOrElse("proj")

  private val createProject = CommandDeps(
    args = Seq(
      // TODO: when remote, need to use remote path
      Quartus.getQuartusExec("quartus_sh", executor.profile.path, OS.isWindows),
      "-t",
      "create_project.tcl"
    ),
    path = executor.workingDir.getAbsolutePath
  )

  override def synthesise(task: TaskConfig)(implicit project: ScaledaProject): Seq[CommandDeps] =
    createProject +:
      Seq(
        CommandDeps(
          args = Seq(
            Quartus.getQuartusExec("quartus_map", executor.profile.path, OS.isWindows),
            "--read_settings_files=on",
            "--write_settings_files=off",
            projectName(task),
            "-c",
            projectName(task)
          ),
          path = executor.workingDir.getAbsolutePath
        )
      )

  override def implement(task: TaskConfig)(implicit project: ScaledaProject): Seq[CommandDeps] =
    synthesise(task) ++
      Seq(
        CommandDeps(
          args = Seq(
            Quartus.getQuartusExec("quartus_fit", executor.profile.path, OS.isWindows),
            "--read_settings_files=off",
            "--write_settings_files=off",
            projectName(task),
            "-c",
            projectName(task)
          ),
          path = executor.workingDir.getAbsolutePath
        ),
        CommandDeps(
          args = Seq(
            Quartus.getQuartusExec("quartus_asm", executor.profile.path, OS.isWindows),
            "--read_settings_files=off",
            "--write_settings_files=off",
            projectName(task),
            "-c",
            projectName(task)
          ),
          path = executor.workingDir.getAbsolutePath
        ),
        CommandDeps(
          args = Seq(
            Quartus.getQuartusExec("quartus_sta", executor.profile.path, OS.isWindows),
            projectName(task),
            "-c",
            projectName(task)
          ),
          path = executor.workingDir.getAbsolutePath
        ),
        CommandDeps(
          args = Seq(
            Quartus.getQuartusExec("quartus_eda", executor.profile.path, OS.isWindows),
            "--read_settings_files=on",
            "--write_settings_files=off",
            projectName(task),
            "-c",
            projectName(task)
          ),
          path = executor.workingDir.getAbsolutePath
        )
      )

  override def programming(task: TaskConfig)(implicit project: ScaledaProject): Seq[CommandDeps] =
    implement(task) ++
      Seq(
        CommandDeps(
          args = Seq(
            Quartus.getQuartusExec("quartus_pgm", executor.profile.path, OS.isWindows),
            "-m",
            "jtag",
            "-c",
            "1",
            "-o",
            s"p;output_files/${projectName(task)}.sof"
          ),
          path = executor.workingDir.getAbsolutePath
        )
      )
}

object Quartus extends ToolchainPresetProvider {
  val userFriendlyName: String = "Intel Quartus"
  val internalID: String       = "quartus"

  val supportedTask: Set[TaskType.Value] = Set(
    // TaskType.Simulation,
    TaskType.Synthesis,
    TaskType.Implement,
    TaskType.Programming
  )

  def getQuartusExec(exec: String, path: String, isWindows: Boolean): String = new File(
    new File(path),
    "/quartus/bin/" + exec + (if (isWindows) ".exe" else "")
  ).getAbsolutePath

  private case class TemplateContext(
      top: String,
      workDir: String,
      family: String,
      part: String,
      sourceVerilogList: Seq[String] = Seq(),
      ipList: Seq[String] = Seq(),
      sdcList: Seq[String] = Seq(),
      // timingReport: Boolean = true,
      insertTclSection: String = ""
  )

  class TemplateRenderer(rt: ScaledaRuntime)(implicit replace: ImplicitPathReplace = NoPathReplace)
      extends ResourceTemplateRender(
        "tcl/quartus",
        rt.executor.workingDir.getAbsolutePath,
        Map("create_project.tcl.j2" -> "create_project.tcl")
      )(replace) {
    override def context: Map[String, Any] =
      // FIXME: need test...
      Map()
  }

  /** Generate context for template. TODO: Scaleda IP Support
    * @param rt
    * @return
    */
  private def generateContext(rt: ScaledaRuntime): TemplateContext = {
    implicit val manifest                    = rt.project
    val (taskConfig, targetConfig, executor) = (rt.task, rt.target, rt.executor)
    val sourceSet                            = taskConfig.getSourceSet
    val sources                              = KernelFileUtils.getAllSourceFiles(sourceSet)

    // val constraints =
    //   taskConfig.taskType match {
    //     case TaskType.Implement   => executor.asInstanceOf[ImplementExecutor].constraints
    //     case TaskType.Programming => executor.asInstanceOf[ProgrammingExecutor].constraints
    //     case _                    => Seq()
    //   }
    val sdcList = rt.executor.constraints.filter(_.getName.toLowerCase.endsWith(".sdc"))
    val qsfList = rt.executor.constraints.filter(_.getName.toLowerCase.endsWith(".qsf"))
    // read all qsf to insertTclSection
    var insertTclSection = ""
    qsfList foreach { f =>
      val s = Source.fromFile(f)
      insertTclSection += s.mkString
      s.close()
    }

    TemplateContext(
      top = taskConfig.findTopModule.get,
      workDir = executor.workingDir.getAbsolutePath,
      family = targetConfig.options.flatMap(_.get("family")).getOrElse("Arria 10"),
      part = targetConfig.options.flatMap(_.get("part")).getOrElse("10AX115N4F40E3SGE3"),
      sourceVerilogList = sources.map(_.getAbsolutePath),
      ipList = taskConfig.getIpFiles.toSeq,
      // sdcList = taskConfig.getConstraints.map(Seq(_)).getOrElse(Seq())
      sdcList = sdcList.map(_.getAbsolutePath),
      insertTclSection = insertTclSection
    )
  }

  /** Execute in `runTask`, from runtime to new runtime
    *
    * @param rt         runtime
    * @param remoteInfo remote information
    * @return
    */
  override def handlePreset(rtOld: ScaledaRuntime, remoteInfo: Option[RemoteInfo]): Option[ScaledaRuntime] = {
    var rt = rtOld
    // implicit val manifest: ScaledaProject     = rt.project
    implicit val replace: ImplicitPathReplace = TemplateContextReplace

    // rt.task.taskType match {
    //   case TaskType.Implement =>
    //     val executor = rt.executor.asInstanceOf[ImplementExecutor]
    //     executor.constraintsDir
    //       .flatMap(dir => {
    //         if (dir.exists() && dir.isDirectory) {
    //           val constraints = KernelFileUtils.scanDirectory(directory = dir, suffixing = Set("sdc"))
    //           Some(executor.copy(constraints = executor.constraints ++ constraints, constraintsDir = None))
    //         } else None
    //       })
    //       .foreach(executorNew => rt = rt.copy(executor = executorNew))
    //   case TaskType.Programming =>
    //     val executor = rt.executor.asInstanceOf[ProgrammingExecutor]
    //     executor.constraintsDir
    //       .flatMap(dir => {
    //         if (dir.exists() && dir.isDirectory) {
    //           val constraints = KernelFileUtils.scanDirectory(directory = dir, suffixing = Set("sdc"))
    //           Some(executor.copy(constraints = executor.constraints ++ constraints, constraintsDir = None))
    //         } else None
    //       })
    //       .foreach(executorNew => rt = rt.copy(executor = executorNew))
    //   case _ =>
    // }
    rt = rt.copy(executor = rt.executor.collectConstraintFiles(Set("sdc")))

    KernelLogger.debug("rt", rt)

    val templateContext = generateContext(rt)
    rt =
      rt.copy(context = rt.context ++ Map("sourceVerilogFiles" -> templateContext.sourceVerilogList.map(new File(_))))

    val useContext       = Serialization.getCCParams(templateContext)
    val templateRenderer = new TemplateRenderer(rt)(replace)
    templateRenderer.render(useContext = useContext)
    Some(rt)
  }
}
