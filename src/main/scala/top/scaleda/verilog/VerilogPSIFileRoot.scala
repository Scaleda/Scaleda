package top.scaleda
package verilog

import verilog.psi.nodes.ScopeNode
import verilog.psi.nodes.`macro`.MacroDeclarationPsiNode
import verilog.psi.nodes.module.ModuleDeclarationPsiNode

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.FileViewProvider
import com.intellij.psi.util.PsiTreeUtil

import javax.swing.Icon

class VerilogPSIFileRoot(viewProvider: FileViewProvider)
    extends PsiFileBase(viewProvider, VerilogLanguage.INSTANCE) /* with ScopeNode */ {

  override def getFileType: FileType = VerilogFileType.instance.asInstanceOf[FileType]

  override def toString: String = "Verilog file"

  override def getIcon(flags: Int): Icon = VerilogFileType.DefaultIcon

  override def getContext: ScopeNode = null

  def getModuleDeclarations: Array[ModuleDeclarationPsiNode] =
    PsiTreeUtil
      .findChildrenOfType(this, classOf[ModuleDeclarationPsiNode])
      .toArray
      .map(x => x.asInstanceOf[ModuleDeclarationPsiNode])

//  def getInstantiatedModules: Array[ModuleDeclarationPsiNode] = {
//    val instantiations = PsiTreeUtil.findChildrenOfType(this, classOf[ModuleInstantiationPsiNode]).asScala
//    instantiations
//      .map(_.getReference.multiResolve(false))
//      .foldLeft(Seq[ResolveResult]())(_ ++ _)
//      .filter(_.isValidResult)
//      .map(_.getElement)
//      .filter(_.isInstanceOf[ModuleDeclarationPsiNode])
//      .map(_.asInstanceOf[ModuleDeclarationPsiNode])
//      .foldLeft(Array.empty[ModuleDeclarationPsiNode]) { (seen, current) =>
//        if (seen.map(_.getName).contains(current.getName)) seen else seen :+ current
//      }
//  }

  def getMacroDeclarations: Array[MacroDeclarationPsiNode] = {
    PsiTreeUtil
      .findChildrenOfType(this, classOf[MacroDeclarationPsiNode])
      .toArray
      .map(x => x.asInstanceOf[MacroDeclarationPsiNode])
  }

//  override def getAvailableNamedElements =
//    PsiTreeUtil.findChildrenOfType(this, classOf[ModuleDeclarationPsiNode])
}
