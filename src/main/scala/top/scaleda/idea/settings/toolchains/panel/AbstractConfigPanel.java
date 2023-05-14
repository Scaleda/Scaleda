package top.scaleda.idea.settings.toolchains.panel;

import com.intellij.ui.SimpleColoredComponent;

import javax.swing.*;
import javax.swing.event.DocumentListener;

public abstract class AbstractConfigPanel {
    public abstract JPanel getComponent();
    public abstract SimpleColoredComponent getStatusLabel();
    public abstract void addPathFieldListener(DocumentListener listener);
}
