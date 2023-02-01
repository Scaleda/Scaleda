package top.criwits.scaleda
package verilog.completion


import com.intellij.codeInsight.completion.{CompletionParameters, CompletionProvider, CompletionResultSet}
import com.intellij.codeInsight.lookup.{LookupElement, LookupElementBuilder}
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.ProcessingContext
import top.criwits.scaleda.verilog.VerilogFileType
import top.criwits.scaleda.verilog.psi.factory.nodes.module.{ModuleDeclarationPsiNode, ModuleIdentifierPsiNode}
import top.criwits.scaleda.verilog.references.ModuleInstantiationPsiNode
import top.criwits.scaleda.verilog.utils.FileUtils

class ModuleItemOuterReferenceCompletionProvider extends CompletionProvider[CompletionParameters] {
  override def addCompletions(parameters: CompletionParameters, context: ProcessingContext, result: CompletionResultSet): Unit = {
    val element = parameters.getOriginalPosition
    val currentModuleDeclaration = PsiTreeUtil.getParentOfType(element, classOf[ModuleDeclarationPsiNode])
    val currentModuleInstantiationNames: Set[String] = PsiTreeUtil
      .findChildrenOfType(
        currentModuleDeclaration,
        classOf[ModuleInstantiationPsiNode])
      .toArray
      .map(x => x.asInstanceOf[PsiElement])
      .map(it => PsiTreeUtil.findChildOfType(it, classOf[ModuleIdentifierPsiNode]))
      .map(it => it.getName)
      .toSet
    if (element.getProject == null) return
    FileUtils.getAllVerilogFiles(element.getProject)
      .flatMap(it => it.getAvailableNamedElementsScala)
      .filter(it => it.isInstanceOf[ModuleDeclarationPsiNode])
      .filter(it => !currentModuleInstantiationNames.contains(it.getName))
      .filter(it => it != currentModuleDeclaration)
      .map(it => buildLookupElementForModuleDeclaration(it.asInstanceOf[ModuleDeclarationPsiNode]))
      .foreach(it => result.addElement(it))
  }

  def buildLookupElementForModuleDeclaration(moduleDeclarationPsiNode: ModuleDeclarationPsiNode): LookupElement = {
    LookupElementBuilder
      .create(moduleDeclarationPsiNode)
      .withIcon(VerilogFileType.DefaultIcon)
      .withTypeText("module")
      .withTailText(s" (in \"${moduleDeclarationPsiNode.getContainingFile.getName}\")")

  }
}
