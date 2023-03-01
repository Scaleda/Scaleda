package top.criwits.scaleda
package idea

import idea.utils.MainLogger
import kernel.template.Template
import kernel.utils.KernelLogger

import com.intellij.openapi.Disposable

final class ScaledaMainService extends Disposable {
  // copy kernel logs to main logger
  KernelLogger.append(MainLogger)
  // init jinjia
  Template.initJinja()

  override def dispose(): Unit = {
    Template.destroyJinja()
    KernelLogger.clearOthers()
  }

}
