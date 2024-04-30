package top.scaleda
package verilog.editor.codeStyle

import com.intellij.application.options.{CodeStyleAbstractConfigurable, CodeStyleAbstractPanel}
import com.intellij.lang.Language
import com.intellij.psi.codeStyle.{CodeStyleConfigurable, CodeStyleSettings, CustomCodeStyleSettings, LanguageCodeStyleSettingsProvider}
import top.scaleda.verilog.VerilogLanguage

class VerilogCodeStyleSettingsProvider extends LanguageCodeStyleSettingsProvider {
  override def createConfigurable(baseSettings: CodeStyleSettings, modelSettings: CodeStyleSettings): CodeStyleConfigurable = {
    new CodeStyleAbstractConfigurable(baseSettings, modelSettings, getLanguage.getID) {
      override def createPanel(settings: CodeStyleSettings): CodeStyleAbstractPanel = new VerilogCodeStylePanel(settings, modelSettings)
    }
  }

  override def getCodeSample(settingsType: LanguageCodeStyleSettingsProvider.SettingsType): String = ""

  override def getLanguage: Language = VerilogLanguage.INSTANCE

  override def createCustomSettings(settings: CodeStyleSettings): CustomCodeStyleSettings = {
    new VerilogCodeStyleSettings(settings)
  }
}
