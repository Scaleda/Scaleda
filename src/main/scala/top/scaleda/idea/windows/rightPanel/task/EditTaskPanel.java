package top.scaleda.idea.windows.rightPanel.task;

import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;

import javax.swing.*;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.lang.reflect.Method;
import java.util.ResourceBundle;

public class EditTaskPanel {
  public JPanel mainPanel;
  public JTextField nameField;
  public JComboBox<String> typeField;
  public JCheckBox presetField;
  public JTextField topModuleField;
  public JButton chooseTopModuleButton;
  public TextFieldWithBrowseButton constraintsField;
  public TextFieldWithBrowseButton tclField;

  public void addDocumentListener(DocumentListener listener) {
    nameField.getDocument().addDocumentListener(listener);
    topModuleField.getDocument().addDocumentListener(listener);
    constraintsField.getTextField().getDocument().addDocumentListener(listener);
//        tclField.getTextField().getDocument().addDocumentListener(listener);

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
