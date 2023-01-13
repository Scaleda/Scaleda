package top.criwits.scaleda
package idea

import kernel.template.Template
import kernel.utils.KernelLogger

import com.intellij.openapi.Disposable

final class ScaledaMainService extends Disposable {
  override def dispose() = {
    Template.destroyJinja()
    KernelLogger.clearOthers()
  }
}
