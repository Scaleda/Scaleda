package top.scaleda
package idea.windows.tool.logging

import javax.swing.{JLabel, JPanel}

class EmptyTabContentView {
  private val content = new JPanel()
  content.add(new JLabel("Empty"))
  def getContent: JPanel = content
}
