package top.criwits.scaleda.idea.runner.task.edit;

import top.criwits.scaleda.kernel.project.config.ProjectConfig$;
import top.criwits.scaleda.kernel.project.task.TargetConfig;
import top.criwits.scaleda.kernel.project.task.TaskConfig;

import javax.swing.*;
import java.util.Arrays;

public class EditTaskDialog implements EditDialogProvider<TaskConfig> {
    private JPanel mainPanel;
    private JTextField name;
    private JComboBox<String> typ;
    private JTextField tcl;
    private JCheckBox preset;
    private JComboBox<String> target;

    @Override
    public JPanel getMainPanel() {
        return mainPanel;
    }

    @Override
    public TaskConfig getData() {
        return new TaskConfig(name.getText(),
                (String) typ.getSelectedItem(),
                tcl.getText(),
                preset.isSelected());
    }

    public EditTaskDialog(TaskConfig initial) {
        name.setText(initial.name());
        tcl.setText(initial.tcl());
        preset.setSelected(initial.preset());
        String[] types = {"simulation", "synthesis", "implementation"};
        typ.setModel(new DefaultComboBoxModel<>(types));
        String[] targets = Arrays.stream(ProjectConfig$.MODULE$.config().targets()).map(TargetConfig::name).toArray(String[]::new);
        target.setModel(new DefaultComboBoxModel<>(targets));
    }

    public String getTargetName() {
        return (String) target.getSelectedItem();
    }
}
