package top.criwits.scaleda
package idea.windows.tasks

import idea.ScaledaBundle
import idea.runner.task.{ScaledaReloadTasksAction, ScaledaRunToolWindowTaskAction, ScaledaTaskPopupMenuAction}
import idea.utils.{MainLogger, ProjectNow, invokeLater}
import idea.windows.tasks.ScaledaRunWindowFactory.{model, vivadoModel, vivadoProject}
import idea.windows.tasks.ip.ScaledaIPManageAction
import kernel.project.config.ProjectConfig
import kernel.project.detect.{VivadoProjectConfig, VivadoRun}
import kernel.utils.serialise.XMLHelper

import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.executors.DefaultRunExecutor
import com.intellij.execution.impl.RunManagerImpl
import com.intellij.execution.runners.ExecutionEnvironmentBuilder
import com.intellij.execution.{ExecutionException, Executor}
import com.intellij.icons.AllIcons
import com.intellij.ide.actions.runAnything.RunAnythingUtil.LOG
import com.intellij.ide.actions.runAnything.execution.RunAnythingRunProfile
import com.intellij.ide.{CommonActionsManager, DefaultTreeExpander, IdeBundle}
import com.intellij.openapi.actionSystem._
import com.intellij.openapi.actionSystem.impl.SimpleDataContext
import com.intellij.openapi.project.{DumbService, Project}
import com.intellij.openapi.ui.{Messages, SimpleToolWindowPanel}
import com.intellij.openapi.vfs.{VirtualFile, VirtualFileManager}
import com.intellij.openapi.wm.{ToolWindow, ToolWindowFactory}
import com.intellij.ui.components.JBList
import com.intellij.ui.content.impl.ContentImpl
import com.intellij.ui.treeStructure.Tree
import com.intellij.ui.{ColoredListCellRenderer, ListSpeedSearch, ScrollPaneFactory, TreeSpeedSearch}
import com.intellij.util.execution.ParametersListUtil
import com.intellij.util.ui.tree.TreeUtil

import java.awt.GridLayout
import java.awt.event.{KeyEvent, MouseAdapter, MouseEvent}
import java.io.File
import javax.swing.tree.DefaultTreeModel
import javax.swing.{DefaultListModel, JList, JPanel, SwingUtilities}

class ScaledaRunWindowFactory extends ToolWindowFactory {
  private var created = false

  override def init(toolWindow: ToolWindow) = {
    super.init(toolWindow)
    MainLogger.info("ScaledaRunWindowFactory#init")
    invokeLater {
      ProjectNow().foreach(p => createToolWindowContent(p, toolWindow))
    }
  }

  override def createToolWindowContent(
      project: Project,
      toolWindow: ToolWindow
  ): Unit = {
    if (created) return
    else created = true
    MainLogger.info("ScaledaRunWindowFactory#createToolWindowContent")
    toolWindow.setTitle(ScaledaBundle.message("tasks.tool.window.title"))
    val contentManager = toolWindow.getContentManager

    val options = new ScaledaRunToolWindowOption(project)
    DumbService
      .getInstance(project)
      .runWhenSmart(() => {
        model = Some(new DefaultTreeModel(null))

        val panel = new SimpleToolWindowPanel(true)
        val tree  = new Tree(model.get)

        tree.setCellRenderer(new ScaledaRunTreeCellRenderer)
        TreeUtil.installActions(tree)
        new TreeSpeedSearch(tree)
        panel.add(ScrollPaneFactory.createScrollPane(tree))
        // var autoScrollHandler = new AutoScrollTo

        // Run
        val runManager = RunManagerImpl.getInstanceImpl(project)
        val runTaskAction =
          new ScaledaRunToolWindowTaskAction(tree, project, runManager)
        runTaskAction.registerCustomShortcutSet(
          new CustomShortcutSet(KeyEvent.VK_ENTER),
          panel
        )

        // Double click
        tree.addMouseListener(new MouseAdapter {
          override def mousePressed(e: MouseEvent) = {
            if (e != null && e.getClickCount == 2 && e.getButton == MouseEvent.BUTTON1) {
              ActionManager
                .getInstance()
                .tryToExecute(runTaskAction, e, tree, "", true)
            }

          }
        })
        // Right click
        tree.addMouseListener(new MouseAdapter {
          override def mouseClicked(e: MouseEvent): Unit = {
            if (SwingUtilities.isRightMouseButton(e)) {
              ActionManager
                .getInstance()
                .tryToExecute(new ScaledaTaskPopupMenuAction(tree, e, runTaskAction, project), e, tree, null, true)
            }
          }
        })

        val group = new DefaultActionGroup()
        group.add(runTaskAction)
        group.addSeparator()

        val treeExpander = new DefaultTreeExpander(tree)
        val expandAll = CommonActionsManager
          .getInstance()
          .createExpandAllAction(treeExpander, tree)
        group.add(expandAll)
        ScaledaRunWindowFactory.expandAll = Some(expandAll)
        // auto expand all after creation
        ActionManager.getInstance().tryToExecute(expandAll, null, null, null, false)
        group.add(
          CommonActionsManager
            .getInstance()
            .createCollapseAllAction(treeExpander, tree)
        )

        group.addSeparator()
        group.add(new ScaledaEditTasksAction)
        group.add(new ScaledaIPManageAction)

        //        group.addSeparator()
        //
        //        val createTargetAction = new ScaledaCreateNewTargetAction(model.get, project)
        //        group.add(createTargetAction)
        //        val createTaskAction = new ScaledaCreateNewTaskAction(tree, project)
        //        group.add(createTaskAction)

        group.addSeparator()

        val refreshTasksAction = new ScaledaReloadTasksAction
        group.add(refreshTasksAction)

        val toolbar = ActionManager
          .getInstance()
          .createActionToolbar("ScaledaRunToolbar", group, true)
        toolbar.setTargetComponent(tree)
        val toolBarPanel     = new JPanel(new GridLayout())
        val toolbarComponent = toolbar.getComponent
        require(toolbarComponent != null)
        toolBarPanel.add(toolbarComponent)
        panel.setToolbar(toolBarPanel)
        contentManager.addContent(new ContentImpl(panel, "Scaleda", true))

        def handleVivadoProject(): Unit = {
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
                  // if (selectedNode == null) return
                  // val task = selectedNode.getUserObject.asInstanceOf[Task]
                  // val config = runManager.createConfiguration(task.name, classOf[ScaledaRunConfigurationType])
                  // val runConfiguration = config.getConfiguration.asInstanceOf[ScaledaRunConfiguration]
                  // runConfiguration.setTask(task)
                  // runManager.addConfiguration(config)
                  // runManager.setSelectedConfiguration(config)
                  // val executor = ExecutorRegistry.getInstance().getExecutorById(DefaultRunExecutor.EXECUTOR_ID)
                  // ProgramRunnerUtil.executeConfiguration(project, config, executor)

                  // ScaledaRun.runTaskBackground(new ScaledaRunProcessHandler(project, )

                  // run command lines in IDEA
                  // val commandLine = new GeneralCommandLine()
                  // commandLine.setExePath("vivado")
                  // commandLine.addParameter("-mode")
                  // commandLine.addParameter("batch")
                  // commandLine.addParameter("-source")
                  // val handler = new KillableColoredProcessHandler(commandLine)
                  // new RunAnythingCommandExecutionProvider().execute()
                  def runCommand(
                      workDirectory: VirtualFile,
                      commandString: String,
                      executor: Executor,
                      dataContext: DataContext
                  ) = {
                    val initialCommandLine =
                      new GeneralCommandLine(ParametersListUtil.parse(commandString, false, true))
                        .withParentEnvironmentType(GeneralCommandLine.ParentEnvironmentType.CONSOLE)
                        .withWorkDirectory(workDirectory.getPath)

                    val commandLine = initialCommandLine
                    try {
                      val runAnythingRunProfile = new RunAnythingRunProfile(
                        commandLine,
                        commandString
                      )
                      ExecutionEnvironmentBuilder
                        .create(project, executor, runAnythingRunProfile)
                        .dataContext(dataContext)
                        .buildAndExecute()
                    } catch {
                      case e: ExecutionException =>
                        LOG.warn(e)
                        Messages
                          .showInfoMessage(project, e.getMessage, IdeBundle.message("run.anything.console.error.title"))
                    }
                  }

                  runCommand(
                    VirtualFileManager.getInstance().findFileByNioPath(new File(project.getBasePath).toPath),
                    "/opt/Xilinx/Vivado/2019.2/bin/vivado -mode tcl",
                    DefaultRunExecutor.getRunExecutorInstance,
                    SimpleDataContext.builder().build()
                  )
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
                  if (SwingUtilities.isRightMouseButton(e)) {
                    // ActionManager
                    //   .getInstance()
                    //   .tryToExecute(
                    //     new ScaledaTaskPopupMenuAction(tree, e, runTaskAction, project),
                    //     e,
                    //     tree,
                    //     null,
                    //     true
                    //   )
                  }
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

        handleVivadoProject()
      })
  }
}

object ScaledaRunWindowFactory {
  val WINDOW_ID                       = ScaledaBundle.message("tasks.configuration.name")
  var model: Option[DefaultTreeModel] = None
  var expandAll: Option[AnAction]     = None

  var vivadoProject: Option[VivadoProjectConfig]       = None
  var vivadoModel: Option[DefaultListModel[VivadoRun]] = None

  def getRootNode: ScaledaRunRootNode = {
    ProjectConfig
      .getConfig()
      .map(c => {
        new ScaledaRunRootNode(c)
      })
      .orNull
  }
}
