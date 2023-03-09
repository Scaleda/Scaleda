package top.criwits.scaleda.verilog.editor.codeStyle;

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
import top.criwits.scaleda.verilog.VerilogFileType;
import top.criwits.scaleda.verilog.VerilogLanguage;
import top.criwits.scaleda.verilog.VerilogLanguage$;

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
    super(VerilogLanguage$.MODULE$, currentSettings, settings);
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
    var indentOptions = settings.getLanguageIndentOptions(VerilogLanguage$.MODULE$);
    indentOptions.INDENT_SIZE = myIndentationSpaces.getValue();
    settings.setRightMargin(VerilogLanguage$.MODULE$, myColumnLimit.getValue());

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
    var indentOptions = settings.getLanguageIndentOptions(VerilogLanguage$.MODULE$);
    myIndentationSpaces.setValue(indentOptions.INDENT_SIZE);
    myColumnLimit.setValue(settings.getRightMargin(VerilogLanguage$.MODULE$));

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



  {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
    $$$setupUI$$$();
  }

  /**
   * Method generated by IntelliJ IDEA GUI Designer
   * >>> IMPORTANT!! <<<
   * DO NOT edit this method OR call it in your code!
   *
   * @noinspection ALL
   */
  private void $$$setupUI$$$() {
    myPanel = new JPanel();
    myPanel.setLayout(new GridLayoutManager(16, 4, new Insets(0, 0, 0, 0), -1, -1));
    final JLabel label1 = new JLabel();
    this.$$$loadLabelText$$$(label1, this.$$$getMessageFromBundle$$$("bundles/ScaledaBundle", "codestyle.indent.spaces"));
    myPanel.add(label1, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(129, 15), null, 0, false));
    myIndentationSpaces = new IntegerField();
    myIndentationSpaces.setText("");
    myPanel.add(myIndentationSpaces, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
    final TitledSeparator titledSeparator1 = new TitledSeparator();
    titledSeparator1.setText(this.$$$getMessageFromBundle$$$("bundles/ScaledaBundle", "codestyle.indent.and.width"));
    myPanel.add(titledSeparator1, new GridConstraints(0, 0, 1, 4, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
    final JLabel label2 = new JLabel();
    this.$$$loadLabelText$$$(label2, this.$$$getMessageFromBundle$$$("bundles/ScaledaBundle", "codestyle.indent.line.break.penalty"));
    myPanel.add(label2, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(129, 15), null, 0, false));
    myLineBreakPenalty = new IntegerField();
    myPanel.add(myLineBreakPenalty, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
    final TitledSeparator titledSeparator2 = new TitledSeparator();
    titledSeparator2.setText(this.$$$getMessageFromBundle$$$("bundles/ScaledaBundle", "codestyle.format.options"));
    myPanel.add(titledSeparator2, new GridConstraints(4, 0, 1, 4, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
    final JLabel label3 = new JLabel();
    this.$$$loadLabelText$$$(label3, this.$$$getMessageFromBundle$$$("bundles/ScaledaBundle", "codestyle.format.assignment.statement"));
    myPanel.add(label3, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(129, 15), null, 0, false));
    myAssignAlign = new JComboBox();
    final DefaultComboBoxModel defaultComboBoxModel1 = new DefaultComboBoxModel();
    defaultComboBoxModel1.addElement("Align");
    defaultComboBoxModel1.addElement("Flush left");
    defaultComboBoxModel1.addElement("Preserve");
    defaultComboBoxModel1.addElement("Infer");
    myAssignAlign.setModel(defaultComboBoxModel1);
    myPanel.add(myAssignAlign, new GridConstraints(5, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    final JLabel label4 = new JLabel();
    this.$$$loadLabelText$$$(label4, this.$$$getMessageFromBundle$$$("bundles/ScaledaBundle", "codestyle.format.case.item.alignment"));
    myPanel.add(label4, new GridConstraints(5, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    final JLabel label5 = new JLabel();
    this.$$$loadLabelText$$$(label5, this.$$$getMessageFromBundle$$$("bundles/ScaledaBundle", "codestyle.format.class.member.alignment"));
    myPanel.add(label5, new GridConstraints(6, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(129, 15), null, 0, false));
    final JLabel label6 = new JLabel();
    this.$$$loadLabelText$$$(label6, this.$$$getMessageFromBundle$$$("bundles/ScaledaBundle", "codestyle.format.distribution"));
    myPanel.add(label6, new GridConstraints(6, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    final JLabel label7 = new JLabel();
    this.$$$loadLabelText$$$(label7, this.$$$getMessageFromBundle$$$("bundles/ScaledaBundle", "codestyle.enum.assignment.alignment"));
    myPanel.add(label7, new GridConstraints(7, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(129, 15), null, 0, false));
    final JLabel label8 = new JLabel();
    this.$$$loadLabelText$$$(label8, this.$$$getMessageFromBundle$$$("bundles/ScaledaBundle", "codestyle.format.module.net.var.alignment"));
    myPanel.add(label8, new GridConstraints(7, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    final JLabel label9 = new JLabel();
    this.$$$loadLabelText$$$(label9, this.$$$getMessageFromBundle$$$("bundles/ScaledaBundle", "codestyle.format.named.param.alignment"));
    myPanel.add(label9, new GridConstraints(9, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(129, 15), null, 0, false));
    final JLabel label10 = new JLabel();
    this.$$$loadLabelText$$$(label10, this.$$$getMessageFromBundle$$$("bundles/ScaledaBundle", "codestyle.format.named.param.indent"));
    myPanel.add(label10, new GridConstraints(9, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    myNamedParamIndent = new JComboBox();
    final DefaultComboBoxModel defaultComboBoxModel2 = new DefaultComboBoxModel();
    defaultComboBoxModel2.addElement("Wrap");
    defaultComboBoxModel2.addElement("Indent");
    myNamedParamIndent.setModel(defaultComboBoxModel2);
    myPanel.add(myNamedParamIndent, new GridConstraints(9, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    final JLabel label11 = new JLabel();
    this.$$$loadLabelText$$$(label11, this.$$$getMessageFromBundle$$$("bundles/ScaledaBundle", "codestyle.indent.wrap.spaces"));
    myPanel.add(label11, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(129, 15), null, 0, false));
    myWrapSpaces = new IntegerField();
    myPanel.add(myWrapSpaces, new GridConstraints(1, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
    final JLabel label12 = new JLabel();
    this.$$$loadLabelText$$$(label12, this.$$$getMessageFromBundle$$$("bundles/ScaledaBundle", "codestyle.indent.over.column.penalty"));
    myPanel.add(label12, new GridConstraints(3, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(129, 15), null, 0, false));
    myOverColumnLimitPenalty = new IntegerField();
    myPanel.add(myOverColumnLimitPenalty, new GridConstraints(3, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
    myCaseAlign = new JComboBox();
    final DefaultComboBoxModel defaultComboBoxModel3 = new DefaultComboBoxModel();
    defaultComboBoxModel3.addElement("Align");
    defaultComboBoxModel3.addElement("Flush left");
    defaultComboBoxModel3.addElement("Preserve");
    defaultComboBoxModel3.addElement("Infer");
    myCaseAlign.setModel(defaultComboBoxModel3);
    myPanel.add(myCaseAlign, new GridConstraints(5, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    myDistributionAlign = new JComboBox();
    final DefaultComboBoxModel defaultComboBoxModel4 = new DefaultComboBoxModel();
    defaultComboBoxModel4.addElement("Align");
    defaultComboBoxModel4.addElement("Flush left");
    defaultComboBoxModel4.addElement("Preserve");
    defaultComboBoxModel4.addElement("Infer");
    myDistributionAlign.setModel(defaultComboBoxModel4);
    myPanel.add(myDistributionAlign, new GridConstraints(6, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    myModuleAlign = new JComboBox();
    final DefaultComboBoxModel defaultComboBoxModel5 = new DefaultComboBoxModel();
    defaultComboBoxModel5.addElement("Align");
    defaultComboBoxModel5.addElement("Flush left");
    defaultComboBoxModel5.addElement("Preserve");
    defaultComboBoxModel5.addElement("Infer");
    myModuleAlign.setModel(defaultComboBoxModel5);
    myPanel.add(myModuleAlign, new GridConstraints(7, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    myClassAlign = new JComboBox();
    final DefaultComboBoxModel defaultComboBoxModel6 = new DefaultComboBoxModel();
    defaultComboBoxModel6.addElement("Align");
    defaultComboBoxModel6.addElement("Flush left");
    defaultComboBoxModel6.addElement("Preserve");
    defaultComboBoxModel6.addElement("Infer");
    myClassAlign.setModel(defaultComboBoxModel6);
    myPanel.add(myClassAlign, new GridConstraints(6, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    myEnumAlign = new JComboBox();
    final DefaultComboBoxModel defaultComboBoxModel7 = new DefaultComboBoxModel();
    defaultComboBoxModel7.addElement("Align");
    defaultComboBoxModel7.addElement("Flush left");
    defaultComboBoxModel7.addElement("Preserve");
    defaultComboBoxModel7.addElement("Infer");
    myEnumAlign.setModel(defaultComboBoxModel7);
    myPanel.add(myEnumAlign, new GridConstraints(7, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    myNamedParamAlign = new JComboBox();
    final DefaultComboBoxModel defaultComboBoxModel8 = new DefaultComboBoxModel();
    defaultComboBoxModel8.addElement("Align");
    defaultComboBoxModel8.addElement("Flush left");
    defaultComboBoxModel8.addElement("Preserve");
    defaultComboBoxModel8.addElement("Infer");
    myNamedParamAlign.setModel(defaultComboBoxModel8);
    myPanel.add(myNamedParamAlign, new GridConstraints(9, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    final JLabel label13 = new JLabel();
    this.$$$loadLabelText$$$(label13, this.$$$getMessageFromBundle$$$("bundles/ScaledaBundle", "codestyle.format.named.port.alignment"));
    myPanel.add(label13, new GridConstraints(10, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(129, 15), null, 0, false));
    final JLabel label14 = new JLabel();
    this.$$$loadLabelText$$$(label14, this.$$$getMessageFromBundle$$$("bundles/ScaledaBundle", "codestyle.format.named.port.indent"));
    myPanel.add(label14, new GridConstraints(10, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    myNamedPortIndent = new JComboBox();
    final DefaultComboBoxModel defaultComboBoxModel9 = new DefaultComboBoxModel();
    defaultComboBoxModel9.addElement("Wrap");
    defaultComboBoxModel9.addElement("Indent");
    myNamedPortIndent.setModel(defaultComboBoxModel9);
    myPanel.add(myNamedPortIndent, new GridConstraints(10, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    myNamedPortAlign = new JComboBox();
    final DefaultComboBoxModel defaultComboBoxModel10 = new DefaultComboBoxModel();
    defaultComboBoxModel10.addElement("Align");
    defaultComboBoxModel10.addElement("Flush left");
    defaultComboBoxModel10.addElement("Preserve");
    defaultComboBoxModel10.addElement("Infer");
    myNamedPortAlign.setModel(defaultComboBoxModel10);
    myPanel.add(myNamedPortAlign, new GridConstraints(10, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    final JLabel label15 = new JLabel();
    this.$$$loadLabelText$$$(label15, this.$$$getMessageFromBundle$$$("bundles/ScaledaBundle", "codestyle.format.port.declaration.indent"));
    myPanel.add(label15, new GridConstraints(11, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    myPortDecIndent = new JComboBox();
    final DefaultComboBoxModel defaultComboBoxModel11 = new DefaultComboBoxModel();
    defaultComboBoxModel11.addElement("Wrap");
    defaultComboBoxModel11.addElement("Indent");
    myPortDecIndent.setModel(defaultComboBoxModel11);
    myPanel.add(myPortDecIndent, new GridConstraints(11, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    myPortDecAlign = new JComboBox();
    final DefaultComboBoxModel defaultComboBoxModel12 = new DefaultComboBoxModel();
    defaultComboBoxModel12.addElement("Align");
    defaultComboBoxModel12.addElement("Flush left");
    defaultComboBoxModel12.addElement("Preserve");
    defaultComboBoxModel12.addElement("Infer");
    myPortDecAlign.setModel(defaultComboBoxModel12);
    myPanel.add(myPortDecAlign, new GridConstraints(11, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    final JLabel label16 = new JLabel();
    this.$$$loadLabelText$$$(label16, this.$$$getMessageFromBundle$$$("bundles/ScaledaBundle", "codestyle.format.port.declaration.alignment"));
    myPanel.add(label16, new GridConstraints(11, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(129, 15), null, 0, false));
    myCompact = new JCheckBox();
    this.$$$loadButtonText$$$(myCompact, this.$$$getMessageFromBundle$$$("bundles/ScaledaBundle", "codestyle.format.compact"));
    myPanel.add(myCompact, new GridConstraints(12, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    myExpandCoverpoints = new JCheckBox();
    this.$$$loadButtonText$$$(myExpandCoverpoints, this.$$$getMessageFromBundle$$$("bundles/ScaledaBundle", "codestyle.format.coverpoints"));
    myPanel.add(myExpandCoverpoints, new GridConstraints(12, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    myAlignPacked = new JCheckBox();
    this.$$$loadButtonText$$$(myAlignPacked, this.$$$getMessageFromBundle$$$("bundles/ScaledaBundle", "codestyle.format.packed"));
    myPanel.add(myAlignPacked, new GridConstraints(13, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    myAlignUnpacked = new JCheckBox();
    this.$$$loadButtonText$$$(myAlignUnpacked, this.$$$getMessageFromBundle$$$("bundles/ScaledaBundle", "codestyle.format.unpacked"));
    myPanel.add(myAlignUnpacked, new GridConstraints(13, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    final JLabel label17 = new JLabel();
    this.$$$loadLabelText$$$(label17, this.$$$getMessageFromBundle$$$("bundles/ScaledaBundle", "codestyle.format.struct"));
    myPanel.add(label17, new GridConstraints(14, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    myFormatStruct = new JComboBox();
    final DefaultComboBoxModel defaultComboBoxModel13 = new DefaultComboBoxModel();
    defaultComboBoxModel13.addElement("Align");
    defaultComboBoxModel13.addElement("Flush left");
    defaultComboBoxModel13.addElement("Preserve");
    defaultComboBoxModel13.addElement("Infer");
    myFormatStruct.setModel(defaultComboBoxModel13);
    myPanel.add(myFormatStruct, new GridConstraints(14, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    final JLabel label18 = new JLabel();
    this.$$$loadLabelText$$$(label18, this.$$$getMessageFromBundle$$$("bundles/ScaledaBundle", "codestyle.indent.column.limit"));
    myPanel.add(label18, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(129, 15), null, 0, false));
    myColumnLimit = new IntegerField();
    myPanel.add(myColumnLimit, new GridConstraints(2, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
    myWrapLongLines = new JCheckBox();
    this.$$$loadButtonText$$$(myWrapLongLines, this.$$$getMessageFromBundle$$$("bundles/ScaledaBundle", "codestyle.format.wrap.long.lines"));
    myPanel.add(myWrapLongLines, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    final Spacer spacer1 = new Spacer();
    myPanel.add(spacer1, new GridConstraints(15, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
    final Spacer spacer2 = new Spacer();
    myPanel.add(spacer2, new GridConstraints(15, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
    final Spacer spacer3 = new Spacer();
    myPanel.add(spacer3, new GridConstraints(15, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
    final Spacer spacer4 = new Spacer();
    myPanel.add(spacer4, new GridConstraints(15, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
    final JLabel label19 = new JLabel();
    this.$$$loadLabelText$$$(label19, this.$$$getMessageFromBundle$$$("bundles/ScaledaBundle", "codestyle.format.formal.alignment"));
    myPanel.add(label19, new GridConstraints(8, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(129, 15), null, 0, false));
    final JLabel label20 = new JLabel();
    this.$$$loadLabelText$$$(label20, this.$$$getMessageFromBundle$$$("bundles/ScaledaBundle", "codestyle.format.formal.indent"));
    myPanel.add(label20, new GridConstraints(8, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    myFormalIndent = new JComboBox();
    final DefaultComboBoxModel defaultComboBoxModel14 = new DefaultComboBoxModel();
    defaultComboBoxModel14.addElement("Wrap");
    defaultComboBoxModel14.addElement("Indent");
    myFormalIndent.setModel(defaultComboBoxModel14);
    myPanel.add(myFormalIndent, new GridConstraints(8, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    myFormalAlign = new JComboBox();
    final DefaultComboBoxModel defaultComboBoxModel15 = new DefaultComboBoxModel();
    defaultComboBoxModel15.addElement("Align");
    defaultComboBoxModel15.addElement("Flush left");
    defaultComboBoxModel15.addElement("Preserve");
    defaultComboBoxModel15.addElement("Infer");
    myFormalAlign.setModel(defaultComboBoxModel15);
    myPanel.add(myFormalAlign, new GridConstraints(8, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
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

  /**
   * @noinspection ALL
   */
  private void $$$loadLabelText$$$(JLabel component, String text) {
    StringBuffer result = new StringBuffer();
    boolean haveMnemonic = false;
    char mnemonic = '\0';
    int mnemonicIndex = -1;
    for (int i = 0; i < text.length(); i++) {
      if (text.charAt(i) == '&') {
        i++;
        if (i == text.length()) break;
        if (!haveMnemonic && text.charAt(i) != '&') {
          haveMnemonic = true;
          mnemonic = text.charAt(i);
          mnemonicIndex = result.length();
        }
      }
      result.append(text.charAt(i));
    }
    component.setText(result.toString());
    if (haveMnemonic) {
      component.setDisplayedMnemonic(mnemonic);
      component.setDisplayedMnemonicIndex(mnemonicIndex);
    }
  }

  /**
   * @noinspection ALL
   */
  private void $$$loadButtonText$$$(AbstractButton component, String text) {
    StringBuffer result = new StringBuffer();
    boolean haveMnemonic = false;
    char mnemonic = '\0';
    int mnemonicIndex = -1;
    for (int i = 0; i < text.length(); i++) {
      if (text.charAt(i) == '&') {
        i++;
        if (i == text.length()) break;
        if (!haveMnemonic && text.charAt(i) != '&') {
          haveMnemonic = true;
          mnemonic = text.charAt(i);
          mnemonicIndex = result.length();
        }
      }
      result.append(text.charAt(i));
    }
    component.setText(result.toString());
    if (haveMnemonic) {
      component.setMnemonic(mnemonic);
      component.setDisplayedMnemonicIndex(mnemonicIndex);
    }
  }

  /**
   * @noinspection ALL
   */
  public JComponent $$$getRootComponent$$$() {
    return myPanel;
  }
}
