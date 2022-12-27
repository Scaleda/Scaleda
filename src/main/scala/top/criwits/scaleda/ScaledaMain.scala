package top.criwits.scaleda

import utils.MainLogger

import com.intellij.openapi.project.{DumbAware, Project}
import com.intellij.openapi.startup.StartupActivity

class ScaledaMain extends DumbAware with StartupActivity {
  override def runActivity(project: Project) = {
    MainLogger.Log.warn("Hi there, it's scaleda! Launching...")
    MainLogger.Log.warn("Scaleda launched.")
  }
}
