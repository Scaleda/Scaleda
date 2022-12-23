package top.criwits.scaleda
package config

import com.intellij.openapi.options.SearchableConfigurable

import javax.swing.JComponent

class EDAProfileConfigurable
  extends SearchableConfigurable {
  private val configUI = new EDAProfilePanel
  override def getId: String = "edaprofile"

  override def getDisplayName: String = "edaprofile"

  override def createComponent(): JComponent = configUI

  override def isModified: Boolean = true

  override def apply(): Unit = {
    println("hey")
  }
}
