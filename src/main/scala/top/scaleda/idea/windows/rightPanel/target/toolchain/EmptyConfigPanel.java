package top.scaleda.idea.windows.rightPanel.target.toolchain;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;

import javax.swing.*;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.lang.reflect.Method;
import java.util.*;

public class EmptyConfigPanel extends ExtraConfigPanel {
    private JPanel mainPanel;

    @Override
    public boolean getStatus() {
        return true;
    }

    @Override
    public JPanel getComponent() {
        return mainPanel;
    }

    @Override
    public void addEditListener(DocumentListener listener) {
        // do nothing
    }


    @Override
    public void loadConfig(Map<String, String> options) {
        // load nothing, meow~

    }

    @Override
    public Map<String, String> getConfig() {
        return new HashMap<>();
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
