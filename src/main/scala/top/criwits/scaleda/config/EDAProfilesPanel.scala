package top.criwits.scaleda
package config

import com.intellij.execution.impl.RunConfigurable.MyTreeModel
import com.intellij.ide.bookmark.ui.tree.RootNode
import com.intellij.openapi.ui.Splitter
import com.intellij.ui.ToolbarDecorator
import com.intellij.ui.treeStructure.Tree

import java.awt.{BorderLayout, Component}
import javax.swing.tree.DefaultTreeModel
import javax.swing.{JComponent, JPanel, JTree}

class EDAProfilesPanel extends JPanel(new BorderLayout){
  private var profiles: Seq[EDAProfile] = Seq.empty

  private def initPanel(): Unit = {
    val splitter = new Splitter(false, 0.3f)
    add(splitter, BorderLayout.CENTER)

    val treePanel = ToolbarDecorator.createDecorator()
  }
}
