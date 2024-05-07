package top.scaleda.idea.windows.bottomPanel;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import org.jetbrains.annotations.NotNull;
import top.scaleda.idea.ScaledaBundle;
import top.scaleda.idea.utils.ScaledaIdeaLogger;
import top.scaleda.idea.windows.bottomPanel.console.ConsoleTabManager;
import top.scaleda.idea.windows.bottomPanel.message.MessageListPanel;
import top.scaleda.idea.windows.bottomPanel.rvcd.RvcdPanel;

public class ScaledaToolWindowFactory implements ToolWindowFactory {
  // @Override
  // public boolean shouldBeAvailable(@NotNull Project project) {
  //   return YmlRootManager.getInstance(project).getRoots().nonEmpty();
  // }

  @Override
  public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
    ConsoleService service = project.getService(ConsoleService.class);
    ConsoleTabManager tabManager = new ConsoleTabManager(project, toolWindow.getContentManager());
    Disposer.register(service, tabManager);
    service.setConsoleTabManager(tabManager);
    MessageListPanel messageTab = new MessageListPanel(project);
    RvcdPanel rvcdPanel = new RvcdPanel(project);
    Disposer.register(tabManager, messageTab);
    Disposer.register(tabManager, rvcdPanel);
    tabManager.addPanel(rvcdPanel, ScaledaBundle.message("windows.tool.log.waveform.title"), false, false);
    tabManager.addPanel(messageTab, ScaledaBundle.message("windows.tool.log.message.title"), false, false);
    tabManager.addConsoleTab(ScaledaIdeaLogger.getId(), ScaledaBundle.message("windows.tool.log.console.title"), false, false);
  }
}
