package top.criwits.scaleda.idea.runner.task.edit;

import javax.swing.*;

public class EditTaskDialog implements EditDialogProvider {
    private JPanel mainPanel;
    private JTextField name;
    private JComboBox typ;
    private JTextField tcl;
    private JCheckBox preset;

    @Override
    public JPanel getMainPanel() {
        return mainPanel;
    }
}
