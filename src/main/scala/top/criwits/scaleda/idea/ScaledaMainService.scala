package top.criwits.scaleda
package idea

import idea.utils.{MainLogger, Notification, OutputLogger}
import kernel.template.Template

import com.intellij.openapi.Disposable
import com.intellij.openapi.util.Disposer

// @Service(Array(Service.Level.PROJECT))
// @Service(Array(Service.Level.APP))
final class ScaledaMainService extends Disposable {
  // register disposables
  private val disposer = Disposer.newDisposable("ScaledaRootDisposable")
  Disposer.register(disposer, MainLogger)
  Disposer.register(disposer, OutputLogger)
  Disposer.register(disposer, Notification)
  Disposer.register(disposer, () => Template.destroyJinja())

  override def dispose() = disposer.dispose()
}
