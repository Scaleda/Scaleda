package top.criwits.scaleda
package idea

import idea.utils.{Icons, MainLogger}
import idea.windows.tasks.ScaledaRunWindowFactory
import kernel.project.config.ProjectConfig
import kernel.template.Template
import kernel.utils.{KernelLogger, LogLevel}

import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ProjectFileIndex
import com.intellij.openapi.startup.StartupActivity
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.wm.{RegisterToolWindowTaskBuilder, ToolWindowAnchor, ToolWindowManager}
import top.criwits.scaleda.idea.windows.tool.logging.ScaledaLoggingService

import scala.util.Random

class ScaledaMain extends StartupActivity {
  override def runActivity(project: Project): Unit = {
    KernelLogger.append(MainLogger)
    // init jinjia
    Template.initJinja()

    // val c = project.getService(classOf[ScaledaMainService])
    // require(c != null)

    // check is having project config
    var searchedFile: Option[VirtualFile] = None
    ProjectFileIndex
      .getInstance(project)
      .iterateContent(fileOrDir => {
        if (
          !fileOrDir.isDirectory && fileOrDir.getName == ProjectConfig.defaultConfigFile
        ) {
          searchedFile = Some(fileOrDir)
        }
        true
      })
    if (searchedFile.isEmpty) {
      MainLogger.warn("there's no project config")
    } else {
      val f = searchedFile.get
      ProjectConfig.configFile = Some(f.getPath)
      ProjectConfig.projectBase = Some(f.getParent.getPath)
      MainLogger.info(s"found config: $f")
    }

    // setup tool window
    val toolWindowManager = ToolWindowManager.getInstance(project)
    toolWindowManager.invokeLater(() => {
      val builder =
        new RegisterToolWindowTaskBuilder(ScaledaRunWindowFactory.WINDOW_ID)
      builder.icon = Icons.mainSmall
      builder.contentFactory = new ScaledaRunWindowFactory
      builder.anchor = ToolWindowAnchor.RIGHT
      // builder.stripeTitle = ScaledaBundle.message("tasks.tool.window.title")
      val toolWindow = toolWindowManager.registerToolWindow(builder.build())
    })

    MainLogger.info("Scaleda launched.")
    val t = new Thread(() => {
      val service = project.getService(classOf[ScaledaLoggingService])
      while (true) {
        val r = LogLevel(Random.nextInt(LogLevel.Fatal.id) - LogLevel.Debug.id)
        val time = System.currentTimeMillis()
        val text = r match {
          case Lo
        }
        service.print("scaleda-message-vivado", "WARNING: [Test tag] test message", LogLevel.Warn)
        Thread.sleep(500)
      }
    })
    t.setDaemon(true)
    t.start()
  }
}
