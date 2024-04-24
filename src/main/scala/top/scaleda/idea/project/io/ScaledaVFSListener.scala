package top.scaleda
package idea.project.io

import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ProjectRootManager
import com.intellij.openapi.vfs.newvfs.BulkFileListener
import com.intellij.openapi.vfs.newvfs.events.VFileEvent

import java.util
import scala.jdk.CollectionConverters._

class ScaledaVFSListener(private val project: Project) extends BulkFileListener {
  override def after(events: util.List[_ <: VFileEvent]): Unit = {
    events.asScala.map(event => {
      val file = event.getFile
      file != null && file.exists() && ProjectRootManager.getInstance(project).getFileIndex.isInContent(file)
    }).foreach(event => {
      /// scaleda.yml change...
      // TODO
    })
  }
}
