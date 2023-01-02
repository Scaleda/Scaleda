package top.criwits.scaleda.idea.settings.toolchains;

import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import top.criwits.scaleda.kernel.toolchain.ToolchainProfile;

import javax.swing.*;

public class EDASoftwarePanel {
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


    }
    public ToolchainProfile getProfile() {
        // TODO
        return null;
    }
}
