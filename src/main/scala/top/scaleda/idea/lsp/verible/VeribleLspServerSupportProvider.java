package top.scaleda.idea.lsp.verible;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.platform.lsp.api.LspServerSupportProvider;
import org.jetbrains.annotations.NotNull;

public class VeribleLspServerSupportProvider implements LspServerSupportProvider {
  @Override
  public void fileOpened(@NotNull Project project, @NotNull VirtualFile virtualFile, @NotNull LspServerSupportProvider.LspServerStarter lspServerStarter) {
  }
}
