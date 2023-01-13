package top.criwits.scaleda
package idea.windows.tool.logging

trait LogSourceListener {
  def newContentAdded(sourceId: String): Unit
  def sourceStarted(sourceId: String): Boolean
  def sourceStopped(sourceId: String): Boolean
}
