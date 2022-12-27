package top.criwits.scaleda.config;

import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import top.criwits.scaleda.toolchain.ToolchainProfile;
import top.criwits.scaleda.toolchain.ToolchainType;
import top.criwits.scaleda.toolchain.ToolchainType$;

import javax.swing.*;

public class EDASettingsPanel {
    private TextFieldWithBrowseButton EDAInstallPath;
    private JTextField profileName;
    private JComboBox toolType;
    private JPanel mainPanel;
    private JPanel optionPanel;

    public JPanel getComponent() {
        return mainPanel;
    }

    public void loadToolchainTypes() {
    }

    public void disableAll() {
        profileName.setText("");
        profileName.setEnabled(false);

        toolType.setEnabled(false);
        EDAInstallPath.setText("");
        EDAInstallPath.setEditable(false);
    }

    public void loadProfile(ToolchainProfile profile) {
        profileName.setText(profile.name());
        profileName.setEnabled(true);

    }
    public ToolchainProfile getProfile() {
        // TODO
        return null;
    }
}
