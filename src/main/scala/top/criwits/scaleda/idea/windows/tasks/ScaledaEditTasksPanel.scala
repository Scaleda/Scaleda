package top.criwits.scaleda
package idea.windows.tasks

import com.intellij.openapi.ui.Splitter

import java.awt.BorderLayout
import javax.swing.JPanel

class ScaledaEditTasksPanel extends JPanel(new BorderLayout) {
  // Load from scaleda.yml

  // left side, tree


  // Splitter
  val splitter = new Splitter(false, 0.3f)
  add(splitter, BorderLayout.CENTER)
}
