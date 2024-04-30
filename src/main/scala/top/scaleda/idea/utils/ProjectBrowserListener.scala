package top.scaleda
package idea.utils

import kernel.project.ScaledaProject
import kernel.utils.KernelFileUtils

import com.intellij.openapi.fileChooser.FileChooserDescriptor
import com.intellij.openapi.ui.{BrowseFolderDescriptor, TextBrowseFolderListener}
import com.intellij.openapi.vfs.VirtualFile

class ProjectBrowserListener(fileChooserDescriptor: FileChooserDescriptor)(implicit project: ScaledaProject)
    extends TextBrowseFolderListener(
      {
        val v = new BrowseFolderDescriptor(fileChooserDescriptor)
        v.setConvertFileToText(ProjectBrowserListener.convertChosenFileToResultingText)
        v
      }
    ) {}
object ProjectBrowserListener {
  private def convertChosenFileToResultingText(chosenFile: VirtualFile)(implicit project: ScaledaProject): String = {
    KernelFileUtils.toProjectRelativePath(chosenFile.getPresentableUrl).getOrElse(chosenFile.getPresentableUrl)
  }
}
