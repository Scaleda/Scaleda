package top.scaleda
package idea

import kernel.utils.KernelLogger

import top.scaleda.idea.utils.ScaledaIdeaLogger
import top.scaleda.kernel.template.Template

class ScaledaAppServiceImpl extends ScaledaAppService {
  private val loggerInited = false
  override def initLoggers(): Unit = {
    if (!loggerInited) {
      KernelLogger.addLogger(ScaledaIdeaLogger)
      ScaledaIdeaLogger.info("Kernel logger initialized")
    }

  }

  private val jinjaInited = false
  override def initJinja(): Unit = {
    if (!jinjaInited) {
      Template.initJinja()
      ScaledaIdeaLogger.info("Jinja2 initialized")
    }
  }

  override def dispose(): Unit = {
    Template.destroyJinja()
    KernelLogger.clearAllLoggers()
  }
}
