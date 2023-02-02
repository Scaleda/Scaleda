package top.criwits.scaleda
package verilog.utils

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiManager
import com.intellij.psi.search.{FileTypeIndex, GlobalSearchScope}
import top.criwits.scaleda.verilog.{VerilogFileType, VerilogPSIFileRoot}

import scala.jdk.javaapi.CollectionConverters

object FileUtils {
  def getAllVerilogFiles(project: Project): List[VerilogPSIFileRoot] = { // FIXME: if source not in src/ will also match
    FileTypeIndex.getFiles(VerilogFileType.instance, GlobalSearchScope.allScope(project))
      .toArray
      .map(it => PsiManager.getInstance(project).findFile(it.asInstanceOf[VirtualFile]))
      .filter(it => it != null)
      .filter(it => it.isInstanceOf[VerilogPSIFileRoot])
      .map(it => it.asInstanceOf[VerilogPSIFileRoot])
      .toList
  }

  def getAllVerilogFilesList(project: Project) =
    CollectionConverters.asJava(getAllVerilogFiles(project))
}
