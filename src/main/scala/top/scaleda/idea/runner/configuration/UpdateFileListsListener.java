package top.scaleda.idea.runner.configuration;

import com.intellij.execution.RunManagerListener;
import com.intellij.execution.RunnerAndConfigurationSettings;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.Nullable;
import top.scaleda.idea.application.config.ScaledaIdeaConfig$;
import top.scaleda.idea.application.config.ScaledaLspConfig;
import top.scaleda.idea.lsp.LspServers$;

public class UpdateFileListsListener implements RunManagerListener {
  private final Project project;

  public UpdateFileListsListener(Project project) {
    this.project = project;
  }

  @Override
  public void runConfigurationSelected(@Nullable RunnerAndConfigurationSettings settings) {
    // new RunManagerListener {
    //   // Only this method is called... have no idea.
    //   override def runConfigurationSelected(settings: RunnerAndConfigurationSettings): Unit = {
    //           ScaledaIdeaLogger.debug("runConfigurationSelected", settings, "current", getCurrentRunConfiguration(project))
    //           // re-generate file lists
    //           val lspConfig = ScaledaIdeaConfig.getConfig.lsp
    //           LspServers.servers
    //                   .find(server => server.name == lspConfig.tool)
    //         .foreach { server =>
    //     server.generateFileLists(project)
    //   }
    //     }
    // }

    ScaledaLspConfig lspConfig = ScaledaIdeaConfig$.MODULE$.getConfig().lsp();
    LspServers$.MODULE$.servers().find(server -> server.name().equals(lspConfig.tool())).foreach(server -> {
      server.generateFileLists(project);
      return null;
    });
  }
}
