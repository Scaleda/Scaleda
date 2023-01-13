package top.criwits.scaleda
package idea.windows.tool.logging

import com.intellij.openapi.project.Project

import javax.swing.{JLabel, JPanel}

class NoActionTabsView(project: Project) {
  private val content = new JPanel()
  content.add(new JLabel("Empty"))
  def getContent: JPanel = content
}
