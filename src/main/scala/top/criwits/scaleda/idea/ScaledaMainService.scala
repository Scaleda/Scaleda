package top.criwits.scaleda
package idea

import idea.utils.{MainLogger, Notification, OutputLogger}
import kernel.template.Template
import kernel.utils.KernelLogger

import com.intellij.openapi.Disposable

// @Service(Array(Service.Level.PROJECT))
// @Service(Array(Service.Level.APP))
final class ScaledaMainService extends Disposable {
  // register disposables
  // private val disposer = Disposer.newDisposable(this, "ScaledaRootDisposable")
  // Disposer.register(disposer, MainLogger)
  // Disposer.register(disposer, OutputLogger)
  // Disposer.register(disposer, Notification)
  // Disposer.register(disposer, () => Template.destroyJinja())

  // Disposer.register(this, MainLogger)
  // Disposer.register(this, OutputLogger)
  // Disposer.register(this, Notification)
  // Disposer.register(this, () => Template.destroyJinja())

  // override def dispose() = disposer.dispose()
  override def dispose() = {
    // disposer.dispose()
    MainLogger.dispose()
    OutputLogger.dispose()
    Notification.dispose()
    Template.destroyJinja()
    KernelLogger.clearOthers()
  }
}
