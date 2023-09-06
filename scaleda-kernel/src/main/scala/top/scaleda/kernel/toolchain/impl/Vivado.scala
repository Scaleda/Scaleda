package top.scaleda
package kernel.toolchain.impl
import kernel.net.remote.RemoteInfo
import kernel.project.ScaledaProject
import kernel.project.config.TaskType._
import kernel.project.config._
import kernel.project.detect.BasicProjectDetector
import kernel.project.importer.VivadoTargetParser
import kernel.shell.command.{CommandDeps, CommandRunner}
import kernel.template.{ResourceTemplateRender, Template}
import kernel.toolchain._
import kernel.toolchain.executor._
import kernel.utils._
import org.apache.commons.codec.digest.DigestUtils
import top.scaleda.kernel.net.user.ScaledaAuthorizationProvider
import top.scaleda.kernel.shell.ScaledaRunHandlerToArray
import top.scaleda.kernel.toolchain.runner.ScaledaRuntime

import java.io.{File, FilenameFilter}
import scala.async.Async.{async, await}
import scala.collection.mutable.ArrayBuffer
import scala.concurrent.ExecutionContext.Implicits.global

/** Implementation for EDA toolchain software, Vivado
  *
  * @param executor An [[Executor]] used to hold information like configurations.
  */
class Vivado(executor: Executor) extends Toolchain(executor) with ToolchainProfileDetector {
  import Vivado._
  override def getInternalID: String = internalID

  override def getName: String = userFriendlyName

  private def getTclFromTask(task: TaskConfig, defaultTcl: String): String = {
    val tclUse = task.tcl.getOrElse(defaultTcl)
    if (task.tcl.isEmpty)
      KernelLogger.warn("did not specify tcl for vivado target, fallback to default", tclUse)
    tclUse
  }

  private def commandDepsForSingleTcl(tclUse: String) = Seq(
    CommandDeps(
      args = Seq(
        getVivadoExec(executor.profile.path),
        "-mode",
        "batch",
        "-source",
        tclUse
      ),
      path = executor.workingDir.getAbsolutePath
    )
  )

  override def simulate(task: TaskConfig)(implicit project: ScaledaProject) = {
    val tclUse = getTclFromTask(task, "run_sim.tcl")
    commandDepsForSingleTcl(tclUse)
  }

  override def synthesise(task: TaskConfig)(implicit project: ScaledaProject) = {
    val tclUse = getTclFromTask(task, "run_synth.tcl")
    commandDepsForSingleTcl(tclUse)
  }

  override def implement(task: TaskConfig)(implicit project: ScaledaProject) = {
    val tclUse = getTclFromTask(task, "run_impl.tcl")
    commandDepsForSingleTcl(tclUse)
  }

  override def programming(task: TaskConfig)(implicit project: ScaledaProject) = {
    val tclUse = getTclFromTask(task, "run_program.tcl")
    // TODO: Vivado programming
    commandDepsForSingleTcl(tclUse)
  }

  override def detectProfiles = Vivado.detectProfiles
}

object Vivado
    extends ToolchainProfileDetector
    with ToolchainPresetProvider
    with BasicProjectDetector
    with VivadoTargetParser {
  val userFriendlyName: String = "Xilinx Vivado"
  val internalID: String       = "vivado"

  val supportedTask: Set[TaskType.Value] = Set(
    TaskType.Simulation,
    TaskType.Synthesis,
    TaskType.Implement,
    TaskType.Programming
  )

  def getVivadoExec(path: String): String = new File(
    new File(path),
    "/bin/vivado" + (if (OS.isWindows) ".bat" else "")
  ).getAbsolutePath

  class Verifier(override val toolchainProfile: ToolchainProfile) extends ToolchainProfile.Verifier(toolchainProfile) {
    override def verifyCommandLine: Option[Seq[CommandDeps]] = {
      val vivadoFile = new File(getVivadoExec(toolchainProfile.path))
      if (!vivadoFile.exists()) {
        return None
      }
      Some(Seq(CommandDeps(args = Seq(vivadoFile.getAbsolutePath, "-version"))))
    }

    override def parseVersionInfo(
        returnValues: Seq[Int],
        outputs: Seq[String]
    ): (Boolean, Option[String]) =
      Verifier.parseVersionInfo(returnValues, outputs)
  }

  object Verifier {
    def parseVersionInfo(
        returnValues: Seq[Int],
        outputs: Seq[String]
    ): (Boolean, Option[String]) = {
      if (!returnValues.forall(_ == 0)) {
        (false, None)
      } else {
        (true, Some(outputs.head.split("\\n").head)) // should work
      }
    }
  }

  case class TemplateContext(
      top: String, // Top module, if sim == true, then it is testbench; otherwise, it is topmodule
      workDir: String,
      part: String,
      jobs: Int = OS.cpuCount,
      sourceList: Seq[String] = Seq(), // NOTE: simulation top file is EXCLUDED here!
      sim: Boolean,
      testbenchSource: String, // empty if no testbench is given
      ipList: Seq[String] = Seq(),
      xdcList: Seq[String] = Seq(),
      timingReport: Boolean = false,
      vcdFile: String,
      insertTclSection: String = ""
  )

  private def generateContext(
      rt: ScaledaRuntime,
      targetTemplateFiles: Seq[File] = Seq(),
      ipsData: Map[String, ProjectConfig] = Map()
  ): TemplateContext = {
    implicit val manifest                       = rt.project
    def doSeparatorReplace(src: String): String = src.replace('\\', '/')
    val (taskConfig, targetConfig, executor)    = (rt.task, rt.target, rt.executor)

    val sim   = taskConfig.taskType == Simulation
    val synth = taskConfig.taskType == Synthesis
    val impl  = taskConfig.taskType == Implement
    var topOptional =
      if (sim) Some(executor.asInstanceOf[SimulationExecutor].topModule)
      else if (synth) Some(executor.asInstanceOf[SynthesisExecutor].topModule)
      else if (impl) Some(executor.asInstanceOf[ImplementExecutor].topModule)
      else None

    // FIXME: this may got None...why?
    if (topOptional.isEmpty)
      topOptional = taskConfig.findTopModule
    if (topOptional.isEmpty) {
      KernelLogger.warn(
        "cannot find top module for",
        taskConfig,
        targetConfig,
        executor
      )
    }
    val ips: Map[String, ProjectConfig] =
      if (ipsData.nonEmpty) ipsData else taskConfig.getAllIps
    val targetAction = Set("all") ++ (if (sim) Set("simulation")
                                      else if (synth) Set("synthesis")
                                      else if (impl) Set("implementation")
                                      else Set())
    val ipInstances: Seq[IPInstance] =
      rt.task.getIpInstances.filter(i => ips.exists(_._2.exports.get.id == i.typeId))
    val ipTclSections: String = ipInstances
      .map(instance => {
        val ip      = ips.find(_._2.exports.get.id == instance.typeId).get
        val actions = ip._2.exports.get.actions
        val tclSections: String = actions
          .filter(a => targetAction.contains(a._1))
          .map(_._2.tcl)
          .flatMap(tcl =>
            tcl.map(t =>
              Template.render(t, ip._2.exports.get.getContextMap(instance.getRenderOptions))(TemplateContextReplace)
            )
          )
          .mkString("\n")
        tclSections
      })
      .mkString("\n")
    // val insertTcl = ips.flatMap(c => c._2.exports).map(c => c.getContextMap())
    // TODO / FIXME: Exception // TODO: topModule is in executor???
    val top = topOptional.get
    val sources =
      if (sim)
        taskConfig.getTestSet ++ ips.flatMap(c => c._2.getTestSet(project = ScaledaProject.getTemporalProject(c._1)))
      else
        taskConfig.getSourceSet ++ ips.flatMap(c =>
          c._2.getSourceSet(project = ScaledaProject.getTemporalProject(c._1))
        )
    val topFile =
      KernelFileUtils.getModuleFileFromSet(sources, module = top).get // TODO / FIXME
    val testbenchSource = doSeparatorReplace(topFile.getAbsolutePath)
    val vcdFile =
      if (sim) doSeparatorReplace(executor.asInstanceOf[SimulationExecutor].vcdFile.getAbsolutePath) else ""
    val xdcList = if (impl) executor.asInstanceOf[ImplementExecutor].constraints.map(_.getAbsolutePath) else Seq()
    val ipList = KernelFileUtils
      .getAllSourceFiles(
        taskConfig.getIpFiles ++ taskConfig.getAllIps
          .flatMap(c => c._2.getIpFiles(project = ScaledaProject.getTemporalProject(c._1))),
        suffixing = Set("xcix", "xci")
      )
      .filter(_.exists())
      .map(_.getAbsolutePath)
    Vivado.TemplateContext(
      top = top,
      workDir = doSeparatorReplace(executor.workingDir.getAbsolutePath),
      part = targetConfig.options.get("part"), // FIXME
      sourceList = KernelFileUtils
        .getAllSourceFiles(sources)
        .filter(f => (!sim) || f.getAbsolutePath != topFile.getAbsolutePath)
        .map(p => doSeparatorReplace(p.getAbsolutePath)) ++ targetTemplateFiles.map(_.getAbsolutePath),
      sim = sim,
      // if sim == false, then this will not be used
      testbenchSource = testbenchSource,
      vcdFile = vcdFile,
      xdcList = xdcList,
      ipList = ipList,
      insertTclSection = ipTclSections
    )
  }

  class TemplateRenderer(rt: ScaledaRuntime)(implicit replace: ImplicitPathReplace = NoPathReplace)
      extends ResourceTemplateRender(
        "tcl/vivado",
        rt.executor.workingDir.getAbsolutePath,
        Map(
          "args.tcl.j2"           -> "args.tcl",
          "create_project.tcl.j2" -> "create_project.tcl",
          "run_sim.tcl.j2"        -> "run_sim.tcl",
          "run_synth.tcl.j2"      -> "run_synth.tcl",
          "run_impl.tcl.j2"       -> "run_impl.tcl",
          "run_program.tcl.j2"    -> "run_program.tcl"
        )
      )(replace) {

    override def context: Map[String, Any] =
      // Serialization.getCCParams(generateContext(rt))
      ???
  }

  override def detectProfiles = async {
    Paths.findExecutableOnPath("vivado") match {
      case Some(vivadoPath) =>
        val vivadoFile = new File(vivadoPath)
        val commands = Seq(
          CommandDeps(args = Seq(vivadoFile.getAbsolutePath, "-version"))
        )
        val outputStrings = ArrayBuffer[String]()
        val returnValues = await(
          CommandRunner.executeAsync(
            commands,
            new ScaledaRunHandlerToArray(None, outputStrings)
          )
        )
        val result =
          Vivado.Verifier.parseVersionInfo(returnValues, outputStrings.toSeq)
        if (result._1) {
          val profile =
            new ToolchainProfile(
              result._2.getOrElse(userFriendlyName),
              internalID
            )
          profile.path = vivadoFile.getAbsolutePath
            .replace("/bin/vivado", "")
            .replace("\\bin\\vivado.bat", "")
          Seq(profile)
        } else Seq()
      case None => Seq()
    }
  }
  ToolchainProfileDetector.registerDetector(this)

  override def handlePreset(rtOld: ScaledaRuntime, remoteInfo: Option[RemoteInfo]): Option[ScaledaRuntime] = {
    var rt                = rtOld
    implicit val manifest = rt.project
    val userTokenBean     = ScaledaAuthorizationProvider.loadByHost(rt.profile.host)
    // A local username is required...
    // TODO: Move preset process to server side?
    if (rt.profile.isRemoteProfile) {
      if (userTokenBean.isEmpty || userTokenBean.exists(_.username.isEmpty)) {
        KernelLogger.warn("Cannot apply Vivado preset! Check your user token info, re-login or register")
        return None
      }
    }
    implicit val replace =
      if (rt.profile.isRemoteProfile) {
        require(remoteInfo.nonEmpty, "remote info required before handle preset")
        // this path may not exist on local
        val remoteTargetPath = remoteInfo.get.tempPrefix +
          (if (remoteInfo.get.os.isRemoteOsTypeWindows) "\\" else "/") +
          userTokenBean.get.username + "-" + DigestUtils.sha256Hex(rt.id)
        // val pathFrom = rt.workingDir.getAbsolutePath.replace('\\', '/')
        new ImplicitPathReplace(
          rt.projectBase.getAbsolutePath,
          remoteTargetPath,
          inner = Some(TemplateContextReplace)
        )
      } else {
        // { waterfall } => {waterfall}
        TemplateContextReplace
      }

    // FIXME: spaces in vivado tcl support
    // paths in vivado do not supports spaces, spaces should be replaced as \x20
    // val dataReplace = new RegexReplace(" ", Seq("\\x20"))

    rt.task.taskType match {
      case Implement =>
        // load constraints from dir
        val executor = rt.executor.asInstanceOf[ImplementExecutor]
        executor.constraintsDir
          .flatMap(dir => {
            if (dir.exists() && dir.isDirectory) {
              val constraints = KernelFileUtils.scanDirectory(directory = dir, suffixing = Set("xdc"))
              Some(executor.copy(constraints = executor.constraints ++ constraints, constraintsDir = None))
            } else None
          })
          .foreach(executorNew => rt = rt.copy(executor = executorNew))
      case _ =>
    }
    val targetAction = Set("all") ++ (rt.executor match {
      case _: SimulationExecutor  => Set("simulation")
      case _: SynthesisExecutor   => Set("synthesis")
      case _: ImplementExecutor   => Set("implement")
      case _: ProgrammingExecutor => Set("programming")
      case _                      => Set()
    })
    val targetTemplateFiles = KernelFileUtils.handleIpInstances(rt.task, rt.executor.workingDir, targetAction)
    if (rt.profile.isRemoteProfile) {
      // remove old vivado project if exists and only for remote
      val top = rt.task.findTopModule.getOrElse("NONE")
      val pathsToDelete = Seq(
        // s"$top/$top.sim",
        // s"$top/$top.cache",
        // s"$top/$top.hw",
        // s"$top/$top.ip_user_files",
        // s"$top/$top.runs"
        s"$top"
      )
      pathsToDelete.foreach(f => {
        val file = new File(rt.executor.workingDir, f)
        KernelLogger.info("vivado preset: removing", file, "exists:", file.exists())
        KernelFileUtils.deleteDirectory(file.toPath)
      })
    }
    // add sources
    val templateContext = generateContext(rt, ipsData = Map(), targetTemplateFiles = targetTemplateFiles)
    rt = rt.copy(context = rt.context ++ Map("sourceFiles" -> templateContext.sourceList.map(new File(_))))
    rt = rt.copy(task = rt.task.copy(tcl = Some(rt.task.taskType match {
      case TaskType.Simulation  => "run_sim.tcl"
      case TaskType.Synthesis   => "run_synth.tcl"
      case TaskType.Implement   => "run_impl.tcl"
      case TaskType.Programming => "run_program.tcl"
    })))
    val useContext       = Serialization.getCCParams(templateContext)
    val templateRenderer = new Vivado.TemplateRenderer(rt)(replace)
    templateRenderer.render(useContext = useContext)
    Some(rt)
  }

  override def detect(path: File) = {
    // only detect <project-name>.xpr file now
    if (!path.exists() || !path.isDirectory) false
    else {
      val projectFile = path
        .listFiles(new FilenameFilter {
          override def accept(file: File, s: String): Boolean = s.endsWith(".xpr")
        })
        .headOption
      if (projectFile.isEmpty) false
      else true
    }
  }
}
