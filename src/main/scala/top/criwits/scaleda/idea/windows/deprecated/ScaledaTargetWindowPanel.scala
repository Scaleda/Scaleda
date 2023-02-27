package top.criwits.scaleda
package idea.windows.deprecated

import idea.ScaledaBundle
import idea.utils.{MainLogger, OutputLogger}
import kernel.project.config.{ProjectConfig, TargetConfig}
import kernel.shell.{ScaledaRun, ScaledaRunHandler}

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.SimpleToolWindowPanel
import com.intellij.ui.ColoredTreeCellRenderer
import com.intellij.ui.treeStructure.Tree

import java.awt.event.{MouseAdapter, MouseEvent}
import java.io.File
import javax.swing.JTree
import javax.swing.event.{TreeSelectionEvent, TreeSelectionListener}
import javax.swing.tree.{DefaultMutableTreeNode, DefaultTreeModel}

@Deprecated
class ScaledaRunIdeaHandler(project: Project) extends ScaledaRunHandler {
  override def onStdout(data: String): Unit = OutputLogger(project).info(data)

  override def onStderr(data: String): Unit = OutputLogger(project).error(data)

  override def onReturn(returnValue: Int, finishedAll: Boolean): Unit = OutputLogger(project).info(s"command done, returns ${returnValue}, finishedAll: ${finishedAll}")
}

@Deprecated
class ScaledaTargetWindowPanel(project: Project) extends SimpleToolWindowPanel(true, true) {
  // Tree
  private val tree = new Tree(new MyTreeModel)
  tree.setRootVisible(true) // FIXME
  tree.setDragEnabled(true)
  tree.setExpandableItemsEnabled(true)
  tree.setCellRenderer(new MyCellRenderer)


  private val root = tree.getModel.getRoot.asInstanceOf[RootNode]
  private val sims = new CategoryNode(("simulation", ScaledaBundle.message("windows.tasks.simulation")))
  private val synth = new CategoryNode(("synthesis", ScaledaBundle.message("windows.tasks.synthesis")))
  private val impl = new CategoryNode(("implementation", ScaledaBundle.message("windows.tasks.implementation")))
  root.add(sims)
  root.add(synth)
  root.add(impl)

  tree.addTreeSelectionListener(new TreeSelectionListener {
    override def valueChanged(treeSelectionEvent: TreeSelectionEvent) = {
      val selPath = treeSelectionEvent.getPath
      if (selPath != null) {
        val node = selPath.getLastPathComponent match {
          case n: RootNode =>
          case n: CategoryNode =>
          case n: TargetNode => {
            val t = new Thread(() => {
              MainLogger.warn(s"ScaledaRun: task name ${n.target.name}") // <== Double Click here
              ProjectConfig.getConfig().map(config => {
                config.taskByName(if (n.target.name == "Vivado") "Vivado Synth" else "", "").map(f => { // Deprecated
                  val (target, task) = f
                  ScaledaRun.runTask(new ScaledaRunIdeaHandler(project), new File(ProjectConfig.projectBase.get), target, task)
                })
              })
            })
            t.setDaemon(true)
            t.start()
          }
          // Runner.runInConsole(project, new GeneralCommandLine("C:\\Windows\\System32\\cmd.exe"), true, true)
        }
      }
    }
  })

  // FIXME
  tree.addMouseListener(new MouseAdapter {
    override def mousePressed(e: MouseEvent): Unit = {
      val selPath = tree.getPathForLocation(e.getX, e.getY)
      // if (selPath != null) {
      //   val node = selPath.getLastPathComponent match {
      //     case n: RootNode =>
      //     case n: CategoryNode =>
      //     case n: TargetNode =>
      //       MainLogger.warn(s"ScaledaRun: task name ${n.target.name}") // <== Double Click here
      //       ProjectConfig.getConfig().map(config => {
      //         config.targetByName(if (n.target.name == "Vivado") "Vivado Synth" else "").map(f => {
      //           val (task, target) = f
      //           ScaledaRun.runTask(ScaledaRunIdeaHandler, new File(ProjectConfig.projectBase.get), task, target)
      //         })
      //       })
      //       // Runner.runInConsole(project, new GeneralCommandLine("C:\\Windows\\System32\\cmd.exe"), true, true)
      //   }
      // }
    }
  })


  setContent(tree)

  loadTasks()

  def loadTasks(): Unit = {
    sims.removeAllChildren()
    synth.removeAllChildren()
    impl.removeAllChildren()
    ProjectConfig.getConfig().foreach(config => {
      config.targetsWithSim.foreach(task => sims.add(new TargetNode(task)))
      config.targetsWithSynth.foreach(task => synth.add(new TargetNode(task)))
      config.targetsWithImpl.foreach(task => impl.add(new TargetNode(task)))
    })
  }


  private class RootNode extends DefaultMutableTreeNode {
    // TODO
  }

  private class CategoryNode(val category: (String, String)) extends DefaultMutableTreeNode(category) {
    // TODO
  }

  private class TargetNode(val target: TargetConfig) extends DefaultMutableTreeNode(target) {
    setAllowsChildren(false)
  }

  private class MyCellRenderer extends ColoredTreeCellRenderer {
    override def customizeCellRenderer(tree: JTree, value: Any, selected: Boolean, expanded: Boolean, leaf: Boolean, row: Int, hasFocus: Boolean): Unit =
      value match {
        case node: CategoryNode =>
          append(node.category._2)
        case node: TargetNode =>
          append(node.target.name)
        case _ =>
      }
  }

  private class MyTreeModel extends DefaultTreeModel(new RootNode) {

  }


}
