package top.criwits.scaleda.idea.settings.auth;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public abstract class LoginDialogInner extends JDialog {
    protected JPanel contentPane;
    protected JButton buttonOK;
    protected JButton buttonCancel;
    protected JButton buttonLogin;
    protected JTextField usernameField;
    protected JPasswordField passwordField;
    private JPanel panelState;
    protected JTextField hostField;
    protected JLabel stateLabel;

    public LoginDialogInner() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(e -> onOK());
        buttonCancel.addActionListener(e -> onCancel());
        buttonLogin.addActionListener(e -> onLogin());

        // 点击 X 时调用 onCancel()
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // 遇到 ESCAPE 时调用 onCancel()
        contentPane.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    }

    protected abstract void onOK();

    protected abstract void onCancel();

    protected abstract void onLogin();

    protected abstract boolean canClickOk();
}
