package top.scaleda.idea.windows.rightPanel.target.toolchain;

import com.intellij.icons.AllIcons;
import com.intellij.ui.DocumentAdapter;
import com.intellij.ui.SimpleColoredComponent;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import org.jetbrains.annotations.NotNull;
import top.scaleda.idea.ScaledaBundle;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.ResourceBundle;

public class VivadoConfigPanel extends ExtraConfigPanel {
    private JPanel mainPanel;
    private JTextField partField;
    private SimpleColoredComponent statusLabel;

    public boolean getStatus() {
        return !getPart().isEmpty();
    }

    @Override
    public JPanel getComponent() {
        return mainPanel;
    }

    @Override
    public void addEditListener(DocumentListener listener) {
        partField.getDocument().addDocumentListener(listener);
    }

    @Override
    public void loadConfig(Map<String, String> options) {
        partField.setText(options.get("part") != null ? options.get("part") : "");
    }

    @Override
    public Map<String, String> getConfig() {
        return Map.of(
                "part", partField.getText()
        );
    }


    public String getPart() {
        return partField.getText().toLowerCase();
    }

    public String getFullDevice() {
        return getPart();
    }

    private void checkStatus() {
        statusLabel.clear();
        if (getStatus()) {
            statusLabel.setIcon(AllIcons.General.BalloonInformation);
            statusLabel.append(ScaledaBundle.message("windows.edit.target.full_device_name",
                    getFullDevice()));
        } else {
            statusLabel.setIcon(AllIcons.General.BalloonError);
            statusLabel.append(ScaledaBundle.message("windows.edit.target.invalid_device"));
        }
    }

    public VivadoConfigPanel() {
        var updateListener = new DocumentAdapter() {
            @Override
            protected void textChanged(@NotNull DocumentEvent e) {
                checkStatus();
            }
        };
        addEditListener(updateListener);
        checkStatus();
    }


  private static Method $$$cachedGetBundleMethod$$$ = null;

    private String $$$getMessageFromBundle$$$(String path, String key) {
        ResourceBundle bundle;
        try {
            Class<?> thisClass = this.getClass();
            if ($$$cachedGetBundleMethod$$$ == null) {
                Class<?> dynamicBundleClass = thisClass.getClassLoader().loadClass("com.intellij.DynamicBundle");
                $$$cachedGetBundleMethod$$$ = dynamicBundleClass.getMethod("getBundle", String.class, Class.class);
            }
            bundle = (ResourceBundle) $$$cachedGetBundleMethod$$$.invoke(null, path, thisClass);
        } catch (Exception e) {
            bundle = ResourceBundle.getBundle(path);
        }
        return bundle.getString(key);
    }

}
