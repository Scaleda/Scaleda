package top.scaleda.idea.windows.bottomPanel;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import org.jetbrains.annotations.NotNull;
import top.scaleda.idea.ScaledaBundle;
import top.scaleda.idea.project.io.YmlRootManager;
import top.scaleda.idea.utils.ScaledaIdeaLogger;
import top.scaleda.idea.windows.bottomPanel.console.ConsoleTabManager;
import top.scaleda.idea.windows.bottomPanel.message.MessageListPanel;
import top.scaleda.idea.windows.bottomPanel.netviewer.NetViewerPanel;

public class ScaledaMessagesWindowFactory implements ToolWindowFactory {
  @Override
  public boolean shouldBeAvailable(@NotNull Project project) {
    return YmlRootManager.getInstance(project).getRoots().nonEmpty();
  }

  @Override
  public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
    ConsoleService service = project.getService(ConsoleService.class);
    ConsoleTabManager tabManager = new ConsoleTabManager(project, toolWindow.getContentManager());
    service.setConsoleTabManager(tabManager);
    MessageListPanel messageTab = new MessageListPanel(project);
    NetViewerPanel netViewerPanel = new NetViewerPanel(project);
    tabManager.addPanel(netViewerPanel, ScaledaBundle.message("windows.tool.log.waveform.title"), false, false);
    tabManager.addPanel(messageTab, ScaledaBundle.message("windows.tool.log.message.title"), false, false);
    tabManager.addConsoleTab(ScaledaIdeaLogger.getId(), ScaledaBundle.message("windows.tool.log.console.title"), false, false);
  }
}
