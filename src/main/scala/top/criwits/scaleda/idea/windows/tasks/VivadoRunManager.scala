package top.criwits.scaleda
package idea.windows.tasks

import idea.runner.SimpleCommandRunner
import idea.utils.{MainLogger, runInEdt}
import idea.windows.tasks.ScaledaRunWindowFactory.{vivadoModel, vivadoProject}
import kernel.project.detect.{VivadoProjectConfig, VivadoRun}
import kernel.toolchain.Toolchain
import kernel.toolchain.impl.Vivado
import kernel.utils.Paths
import kernel.utils.serialise.XMLHelper

import com.intellij.execution.impl.RunManagerImpl
import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem._
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.SimpleToolWindowPanel
import com.intellij.openapi.vfs.VirtualFileManager
import com.intellij.ui.components.JBList
import com.intellij.ui.content.ContentManager
import com.intellij.ui.content.impl.ContentImpl
import com.intellij.ui.{ColoredListCellRenderer, ListSpeedSearch, ScrollPaneFactory}

import java.awt.GridLayout
import java.awt.event.{KeyEvent, MouseAdapter, MouseEvent}
import java.io.File
import javax.swing.{DefaultListModel, JList, JPanel, SwingUtilities}

object VivadoRunManager {
  def handleVivadoProject(project: Project, contentManager: ContentManager): Unit = {
    // check if this project has a vivado project
    val projectBase = new File(project.getBasePath)
    if (!(projectBase.exists() && projectBase.isDirectory)) return

    // list file, find *.xpr
    val xprFiles = projectBase.listFiles().filter(_.getName.endsWith(".xpr"))
    // use head file
    xprFiles.headOption.foreach(xprFile => {
      // try to parse it as vivado project struct
      try {
        ScaledaRunWindowFactory.vivadoProject = Some(XMLHelper(xprFile, classOf[VivadoProjectConfig]))
      } catch {
        case e: Throwable => MainLogger.warn("cannot parse vivado project", e)
      }
      ScaledaRunWindowFactory.vivadoProject.foreach(c => {
        ScaledaRunWindowFactory.vivadoModel = Some(new DefaultListModel[VivadoRun]())

        val panel = new SimpleToolWindowPanel(true)
        val list  = new JBList[VivadoRun](vivadoModel.get)

        class VivadoRunListCellRenderer extends ColoredListCellRenderer[VivadoRun] {
          override def customizeCellRenderer(
              list: JList[_ <: VivadoRun],
              value: VivadoRun,
              index: Int,
              selected: Boolean,
              hasFocus: Boolean
          ) = {
            if (value != null) {
              setIcon(AllIcons.RunConfigurations.TestState.Run)
              append(s"[${value.Id}] ${value.Description}")
            }
          }
        }

        list.setCellRenderer(new VivadoRunListCellRenderer)
        new ListSpeedSearch[VivadoRun](list)
        panel.add(ScrollPaneFactory.createScrollPane(list))

        // Run
        val runManager = RunManagerImpl.getInstanceImpl(project)

        class VivadoRunTaskAction(list: JList[VivadoRun], project: Project, runManager: RunManagerImpl)
            extends AnAction("run vivado task", "run", AllIcons.RunConfigurations.TestState.Run) {
          override def actionPerformed(e: AnActionEvent): Unit = {
            val item = list.getSelectedValue
            if (item == null) return
            val id = item.Id

            Toolchain
              .profiles()
              .find(_.toolchainType == Vivado.internalID)
              .map(_.path)
              .map(vivadoPath => {
                // FIXME: java.lang.Throwable: Assert: must be called on EDT
                SimpleCommandRunner.runCommand(
                  project,
                  VirtualFileManager.getInstance().findFileByNioPath(new File(project.getBasePath).toPath),
                  s"${Vivado.getVivadoExec(vivadoPath)} -mode batch -source ${Paths.getGlobalConfigDir}/scripts/vivado_call.tcl -tclargs ${xprFile.getAbsolutePath} ${id}"
                )
              })
              .getOrElse({
                MainLogger.warn("cannot find vivado path")
              })
          }
        }
        val runTaskAction =
          new VivadoRunTaskAction(list, project, runManager)
        runTaskAction.registerCustomShortcutSet(
          new CustomShortcutSet(KeyEvent.VK_ENTER),
          panel
        )

        // Double click
        list.addMouseListener(new MouseAdapter {
          override def mousePressed(e: MouseEvent) = {
            if (e != null && e.getClickCount == 2 && e.getButton == MouseEvent.BUTTON1) {
              ActionManager
                .getInstance()
                .tryToExecute(runTaskAction, e, list, "", true)
            }

          }
        })
        // Right click
        list.addMouseListener(new MouseAdapter {
          override def mouseClicked(e: MouseEvent): Unit = {
            if (SwingUtilities.isRightMouseButton(e)) {}
          }
        })

        val group = new DefaultActionGroup()
        group.add(runTaskAction)
        group.addSeparator()

        class ReloadVivadoAction extends AnAction("Refresh Vivado", "refresh", AllIcons.Actions.Refresh) {
          override def actionPerformed(e: AnActionEvent) = {
            try {
              vivadoProject = Some(XMLHelper(xprFile, classOf[VivadoProjectConfig]))
              vivadoProject.foreach(v => {
                vivadoModel.get.clear()
                v.runs.map(r => {
                  vivadoModel.get.addElement(r)
                })
              })
            } catch {
              case e: Throwable => MainLogger.warn("cannot reload vivado project", e)
            }
          }
        }

        val refreshTasksAction = new ReloadVivadoAction
        group.add(refreshTasksAction)

        refreshTasksAction.actionPerformed(null)

        val toolbar = ActionManager
          .getInstance()
          .createActionToolbar("VivadoRunToolbar", group, true)
        toolbar.setTargetComponent(list)
        val toolBarPanel     = new JPanel(new GridLayout())
        val toolbarComponent = toolbar.getComponent
        require(toolbarComponent != null)
        toolBarPanel.add(toolbarComponent)
        panel.setToolbar(toolBarPanel)
        contentManager.addContent(new ContentImpl(panel, "Vivado", true))
      })
    })
  }
}
