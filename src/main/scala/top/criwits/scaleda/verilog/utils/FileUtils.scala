package top.criwits.scaleda
package verilog.utils

import idea.utils.inReadAction
import verilog.{VerilogFileType, VerilogPSIFileRoot}

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.{LocalFileSystem, VirtualFile, VirtualFileManager}
import com.intellij.psi.PsiManager
import com.intellij.psi.search.{FileTypeIndex, GlobalSearchScope}
import com.intellij.psi.util.PsiTreeUtil
import top.criwits.scaleda.kernel.project.config.ProjectConfig
import top.criwits.scaleda.verilog.psi.nodes.module.{ModuleDeclarationPsiNode, ModuleHeadPsiNode}

import java.io.File
import scala.jdk.javaapi.CollectionConverters
import scala.jdk.CollectionConverters._

object FileUtils {


  def getAllVerilogFiles(project: Project, test: Boolean = false): List[VerilogPSIFileRoot] = { // FIXME: if source not in src/ will also match
    ProjectConfig.projectBase.map(projectBase => {
      val sourceDir: File = new File(new File(projectBase).getAbsolutePath, ProjectConfig.config.source)
      val testDir: File = new File(new File(projectBase).getAbsolutePath, ProjectConfig.config.test)

      val vf = LocalFileSystem.getInstance().findFileByIoFile(if (test) testDir else sourceDir)
      val psiDirectory = PsiManager.getInstance(project).findDirectory(vf)
      var result: List[VerilogPSIFileRoot] = null
      inReadAction {
        result = PsiTreeUtil.findChildrenOfAnyType(psiDirectory, classOf[VerilogPSIFileRoot]).asScala.toList
      }
      result
    }).getOrElse(List.empty)
  }

  def getAllVerilogFilesList(project: Project) =
    CollectionConverters.asJava(getAllVerilogFiles(project))
}
