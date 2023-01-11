package top.criwits.scaleda.idea.runner.task.edit;

import javax.swing.*;

public class EditTargetDialog implements EditDialogProvider {
    private JTextField name;
    private JPanel mainPanel;
    private JComboBox toolchain;
    private JTextField device;
    private JTextField pack;
    private JTextField speed;

    @Override
    public JPanel getMainPanel() {
        return mainPanel;
    }
}
