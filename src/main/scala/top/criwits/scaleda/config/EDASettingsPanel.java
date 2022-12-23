package top.criwits.scaleda.config;

import com.intellij.openapi.ui.TextFieldWithBrowseButton;

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
}
