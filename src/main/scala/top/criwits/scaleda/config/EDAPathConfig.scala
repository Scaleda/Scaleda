package top.criwits.scaleda
package config

import com.intellij.openapi.options.SearchableConfigurable

import javax.swing.JComponent

class EDAPathConfig extends SearchableConfigurable {
  private val configUI = new EDAPathConfigUI
  override def getId: String = "pathcfg"

  override def getDisplayName: String = "pathcfg"

  override def createComponent(): JComponent = configUI.getComponent;

  override def isModified: Boolean = true

  override def apply(): Unit = {
    println("hey")
  }
}
