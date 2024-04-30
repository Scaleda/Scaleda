package top.scaleda.idea.windows.rightPanel;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.ContentManager;
import org.jetbrains.annotations.NotNull;
import top.scaleda.idea.ScaledaBundle;

public class ScaledaTasksWindowFactory implements ToolWindowFactory {
  @Override
  public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
    @NotNull ContentManager contentManager = toolWindow.getContentManager();
    ScaledaTasksPanel panel = new ScaledaTasksPanel(project);
    contentManager.addContent(
            contentManager.getFactory().createContent(
                    panel, ScaledaBundle.message("tasks.tool.window.title"), false));
  }
}
