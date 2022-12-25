package top.criwits.scaleda.config;

import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import top.criwits.scaleda.toolchain.ToolchainProfile;

import javax.swing.*;

public class EDASettingsPanel {
    private TextFieldWithBrowseButton EDAInstallPath;
    private JTextField ProfileName;
    private JComboBox comboBox1;
    private JPanel mainPanel;
    private JPanel optionPanel;

    public JPanel getComponent() {
        return mainPanel;
    }

    public ToolchainProfile getProfile() {
        // TODO
        return null;
    }
}
