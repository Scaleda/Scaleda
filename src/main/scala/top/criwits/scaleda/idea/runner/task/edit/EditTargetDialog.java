package top.criwits.scaleda.idea.runner.task.edit;

import top.criwits.scaleda.kernel.project.task.TargetConfig;
import top.criwits.scaleda.kernel.project.task.TaskConfig;
import top.criwits.scaleda.kernel.toolchain.Toolchain;

import javax.swing.*;

public class EditTargetDialog implements EditDialogProvider<TargetConfig> {
    private JTextField name;
    private JPanel mainPanel;
    private JComboBox<String> toolchain;
    private JTextField device;
    private JTextField pack;
    private JTextField speed;

    @Override
    public JPanel getMainPanel() {
        return mainPanel;
    }

    @Override
    public TargetConfig getData() {
        TaskConfig[] empty = new TaskConfig[0];
        return new TargetConfig(name.getText(),
                (String) toolchain.getSelectedItem(),
                device.getText(),
                pack.getText(),
                Integer.parseInt(speed.getText()),
                empty);
    }

    public EditTargetDialog(TargetConfig initial) {
        name.setText(initial.name());
        String[] profiles = Toolchain.toolchainNames();
        toolchain.setModel(new DefaultComboBoxModel<>(profiles));
        device.setText(initial.device());
        pack.setText(initial.getPackage());
        speed.setText(initial.getPackage());
    }

}
