package top.scaleda
package bluespec

import kernel.language.LangBluespec

import com.intellij.lang.Language

class BluespecLanguage extends Language(LangBluespec.getName)
object BluespecLanguage {
  val INSTANCE = new BluespecLanguage
}
