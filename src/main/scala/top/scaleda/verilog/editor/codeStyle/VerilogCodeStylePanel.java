package top.scaleda.verilog.editor.codeStyle;

import com.intellij.application.options.CodeStyleAbstractPanel;
import com.intellij.openapi.editor.colors.EditorColorsScheme;
import com.intellij.openapi.editor.highlighter.EditorHighlighter;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.psi.codeStyle.CodeStyleSettings;
import com.intellij.ui.TitledSeparator;
import com.intellij.ui.components.fields.IntegerField;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import com.intellij.util.ui.JBUI;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import scala.Enumeration;
import top.scaleda.verilog.VerilogFileType;
import top.scaleda.verilog.VerilogLanguage;
import top.scaleda.verilog.VerilogLanguage$;
import top.scaleda.verilog.VerilogFileType;
import top.scaleda.verilog.VerilogLanguage$;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.Method;
import java.util.ResourceBundle;

public class VerilogCodeStylePanel extends CodeStyleAbstractPanel {
  private JComboBox myAssignAlign;
  private JCheckBox myCompact;
  private JCheckBox myAlignPacked;
  private IntegerField myIndentationSpaces;
  private IntegerField myWrapSpaces;
  private IntegerField myLineBreakPenalty;
  private IntegerField myColumnLimit;
  private JCheckBox myWrapLongLines;
  private IntegerField myOverColumnLimitPenalty;
  private JComboBox myCaseAlign;
  private JComboBox myClassAlign;
  private JComboBox myDistributionAlign;
  private JComboBox myEnumAlign;
  private JComboBox myModuleAlign;
  private JComboBox myNamedParamAlign;
  private JComboBox myNamedParamIndent;
  private JComboBox myNamedPortAlign;
  private JComboBox myNamedPortIndent;
  private JComboBox myPortDecAlign;
  private JComboBox myPortDecIndent;
  private JCheckBox myExpandCoverpoints;
  private JCheckBox myAlignUnpacked;
  private JComboBox myFormatStruct;
  private JPanel myPanel;
  private JComboBox myFormalAlign;
  private JComboBox myFormalIndent;

  VerilogCodeStylePanel(CodeStyleSettings currentSettings, CodeStyleSettings settings) {
    super(VerilogLanguage$.MODULE$.INSTANCE(), currentSettings, settings);
    myPanel.setBorder(JBUI.Borders.empty(10));
    addPanelToWatch(myPanel);

  }

  @Override
  protected int getRightMargin() {
    return 0;
  }

  @Override
  protected @Nullable EditorHighlighter createHighlighter(EditorColorsScheme scheme) {
    return null;
  }

  @Override
  protected @NotNull FileType getFileType() {
    return VerilogFileType.instance();
  }

  @Override
  protected @NonNls
  @Nullable String getPreviewText() {
    return "";
  }

  @Override
  public void apply(CodeStyleSettings settings) throws ConfigurationException {
    var indentOptions = settings.getLanguageIndentOptions(VerilogLanguage$.MODULE$.INSTANCE());
    indentOptions.INDENT_SIZE = myIndentationSpaces.getValue();
    settings.setRightMargin(VerilogLanguage$.MODULE$.INSTANCE(), myColumnLimit.getValue());

    var verilogSettings = settings.getCustomSettings(VerilogCodeStyleSettings.class);
    verilogSettings.LINE_BREAK_PENALTY_$eq(myLineBreakPenalty.getValue());
    verilogSettings.OVER_COLUMN_LIMIT_PENALTY_$eq(myOverColumnLimitPenalty.getValue());
    verilogSettings.WRAP_SPACES_$eq(myWrapSpaces.getValue());
    verilogSettings.TRY_WRAP_LONG_LINES_$eq(myWrapLongLines.isSelected());

    verilogSettings.ASSIGNMENT_STATEMENT_ALIGNMENT_$eq(getIndentOption(myAssignAlign));
    verilogSettings.CASE_ITEMS_ALIGNMENT_$eq(getIndentOption(myCaseAlign));
    verilogSettings.CLASS_MEMBER_VARIABLE_ALIGNMENT_$eq(getIndentOption(myClassAlign));
    verilogSettings.COMPACT_INDEXING_AND_SELECTION_$eq(myCompact.isSelected());
    verilogSettings.DISTRIBUTION_ITEMS_ALIGNMENT_$eq(getIndentOption(myDistributionAlign));
    verilogSettings.ENUM_ASSIGNMENT_STATEMENT_ALIGNMENT_$eq(getIndentOption(myEnumAlign));
    verilogSettings.EXPAND_COVERPOINTS_$eq(myExpandCoverpoints.isSelected());
    verilogSettings.FORMAL_PARAMETERS_ALIGNMENT_$eq(getIndentOption(myFormalAlign));
    verilogSettings.FORMAL_PARAMETERS_INDENTATION_$eq(getIndentWrap(myFormalIndent));
    verilogSettings.MODULE_NET_VARIABLE_ALIGNMENT_$eq(getIndentOption(myModuleAlign));
    verilogSettings.NAMED_PARAMETER_ALIGNMENT_$eq(getIndentOption(myNamedParamAlign));
    verilogSettings.NAMED_PARAMETER_INDENTATION_$eq(getIndentWrap(myNamedParamIndent));
    verilogSettings.NAMED_PORT_ALIGNMENT_$eq(getIndentOption(myNamedPortAlign));
    verilogSettings.NAMED_PORT_INDENTATION_$eq(getIndentWrap(myNamedPortIndent));
    verilogSettings.PORT_DECLARATIONS_ALIGNMENT_$eq(getIndentOption(myPortDecAlign));
    verilogSettings.PORT_DECLARATIONS_INDENTATION_$eq(getIndentWrap(myPortDecIndent));
    verilogSettings.PORT_DRAPD_$eq(myAlignPacked.isSelected());
    verilogSettings.PORT_DRAUD_$eq(myAlignUnpacked.isSelected());
    verilogSettings.STRUCT_UNION_MEMBERS_ALIGNMENT_$eq(getIndentOption(myFormatStruct));
  }

  private Enumeration.Value getIndentOption(JComboBox box) {
    switch (box.getSelectedIndex()) {
      case 0:
        return VerilogCodeStyleSettings.ALIGN();
      case 1:
        return VerilogCodeStyleSettings.FLUSH_LEFT();
      case 2:
        return VerilogCodeStyleSettings.PRESERVE();
      default:
        return VerilogCodeStyleSettings.INFER();
    }
  }

  private boolean getIndentWrap(JComboBox box) {
    return box.getSelectedIndex() == 1;
  }

  @Override
  protected void resetImpl(CodeStyleSettings settings) {
    var indentOptions = settings.getLanguageIndentOptions(VerilogLanguage$.MODULE$.INSTANCE());
    myIndentationSpaces.setValue(indentOptions.INDENT_SIZE);
    myColumnLimit.setValue(settings.getRightMargin(VerilogLanguage$.MODULE$.INSTANCE()));

    var verilogSettings = settings.getCustomSettings(VerilogCodeStyleSettings.class);
    myLineBreakPenalty.setValue(verilogSettings.LINE_BREAK_PENALTY());
    myOverColumnLimitPenalty.setValue(verilogSettings.OVER_COLUMN_LIMIT_PENALTY());
    myWrapSpaces.setValue(verilogSettings.WRAP_SPACES());

    myAssignAlign.setSelectedIndex(getIndentOption(verilogSettings.ASSIGNMENT_STATEMENT_ALIGNMENT()));
    myCaseAlign.setSelectedIndex(getIndentOption(verilogSettings.CASE_ITEMS_ALIGNMENT()));
    myClassAlign.setSelectedIndex(getIndentOption(verilogSettings.CLASS_MEMBER_VARIABLE_ALIGNMENT()));
    myCompact.setSelected(verilogSettings.COMPACT_INDEXING_AND_SELECTION());
    myDistributionAlign.setSelectedIndex(getIndentOption(verilogSettings.DISTRIBUTION_ITEMS_ALIGNMENT()));
    myEnumAlign.setSelectedIndex(getIndentOption(verilogSettings.ENUM_ASSIGNMENT_STATEMENT_ALIGNMENT()));
    myExpandCoverpoints.setSelected(verilogSettings.EXPAND_COVERPOINTS());
    myFormalAlign.setSelectedIndex(getIndentOption(verilogSettings.FORMAL_PARAMETERS_ALIGNMENT()));
    myFormalIndent.setSelectedIndex(getIndentWrap(verilogSettings.FORMAL_PARAMETERS_INDENTATION()));
    myModuleAlign.setSelectedIndex(getIndentOption(verilogSettings.MODULE_NET_VARIABLE_ALIGNMENT()));
    myNamedParamAlign.setSelectedIndex(getIndentOption(verilogSettings.NAMED_PARAMETER_ALIGNMENT()));
    myNamedParamIndent.setSelectedIndex(getIndentWrap(verilogSettings.NAMED_PARAMETER_INDENTATION()));
    myNamedPortAlign.setSelectedIndex(getIndentOption(verilogSettings.NAMED_PORT_ALIGNMENT()));
    myNamedPortIndent.setSelectedIndex(getIndentWrap(verilogSettings.NAMED_PORT_INDENTATION()));
    myPortDecAlign.setSelectedIndex(getIndentOption(verilogSettings.PORT_DECLARATIONS_ALIGNMENT()));
    myPortDecIndent.setSelectedIndex(getIndentWrap(verilogSettings.PORT_DECLARATIONS_INDENTATION()));
    myAlignPacked.setSelected(verilogSettings.PORT_DRAPD());
    myAlignUnpacked.setSelected(verilogSettings.PORT_DRAUD());
    myFormatStruct.setSelectedIndex(getIndentOption(verilogSettings.STRUCT_UNION_MEMBERS_ALIGNMENT()));
    myWrapLongLines.setSelected(verilogSettings.TRY_WRAP_LONG_LINES());
  }

  private int getIndentOption(Enumeration.Value value) {
    if (value.equals(VerilogCodeStyleSettings.ALIGN())) return 0;
    if (value.equals(VerilogCodeStyleSettings.FLUSH_LEFT())) return 1;
    if (value.equals(VerilogCodeStyleSettings.PRESERVE())) return 2;
    if (value.equals(VerilogCodeStyleSettings.INFER())) return 3;

    return 0;
  }

  private int getIndentWrap(boolean indent) {
    if (indent) return 1; else return 0;
  }

  @Override
  public boolean isModified(CodeStyleSettings settings) {
    return true; // TODO
  }

  @Override
  public @Nullable JComponent getPanel() {
    return myPanel;
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
