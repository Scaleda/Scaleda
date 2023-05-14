package top.scaleda
package idea.utils

import kernel.project.ProjectManifest
import kernel.utils.KernelFileUtils

import com.intellij.openapi.fileChooser.FileChooserDescriptor
import com.intellij.openapi.ui.TextBrowseFolderListener
import com.intellij.openapi.vfs.VirtualFile

class ProjectBrowserListener(fileChooserDescriptor: FileChooserDescriptor)(implicit manifest: ProjectManifest)
    extends TextBrowseFolderListener(fileChooserDescriptor) {
  override def chosenFileToResultingText(chosenFile: VirtualFile) = {
    KernelFileUtils.toProjectRelativePath(chosenFile.getPresentableUrl).getOrElse(chosenFile.getPresentableUrl())
  }
}
