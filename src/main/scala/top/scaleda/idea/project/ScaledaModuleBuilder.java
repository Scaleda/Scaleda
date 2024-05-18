package top.scaleda.idea.project;

import com.intellij.ide.util.projectWizard.ModuleBuilder;
import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.util.io.FileUtilRt;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

public class ScaledaModuleBuilder extends ModuleBuilder {
  @Override
  public ModuleType<?> getModuleType() {
    return ScaledaModuleType.getInstance();
  }

  @Override
  public void setupRootModel(@NotNull ModifiableRootModel modifiableRootModel) throws ConfigurationException {
    @Nullable String contentPath = getContentEntryPath();
    if (contentPath == null) {
      throw new ConfigurationException("Content path is not specified");
    }
    File contentDir = new File(contentPath);
    if (FileUtilRt.createDirectory(contentDir)) {
      // addSourceFolder(contentDir, false);
    }
  }
}
