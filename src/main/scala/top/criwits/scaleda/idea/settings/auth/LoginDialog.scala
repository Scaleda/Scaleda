package top.criwits.scaleda
package idea.settings.auth

import idea.ScaledaBundle
import kernel.net.RemoteServer
import kernel.net.user.{ScaledaAuthorizationProvider, ScaledaRegisterLogin, UserTokenBean}

import com.intellij.icons.AllIcons

class LoginDialog extends LoginDialogInner {
  private var userToken: Option[UserTokenBean] = None
  private def updateButtonEnable(): Unit = {
    buttonOK.setEnabled(canClickOk)
  }
  updateButtonEnable()
  RemoteServer.AVAILABLE_REMOTE_HOSTS.headOption.foreach(host => {
    hostField.setText(host)
  })

  override protected def onOK(): Unit = {
    userToken.foreach(t => ScaledaAuthorizationProvider.saveTokenPair(t))
  }

  override protected def onCancel(): Unit = {
    dispose()
  }

  override protected def onLogin(): Unit = {
    val host     = hostField.getText
    val username = usernameField.getText
    val password = String.valueOf(passwordField.getPassword)
    try {
      val handler = new ScaledaRegisterLogin(host = host)
      val reply   = handler.login(username, password, save = false)
      if (reply.ok) {
        userToken = Some(
          UserTokenBean(host = host, username = username, token = reply.token, refreshToken = reply.refreshToken)
        )
        stateLabel.setIcon(AllIcons.General.InspectionsOK)
        stateLabel.setText(ScaledaBundle.message("settings.dialog.login.reply.ok"))
      } else {
        userToken = None
        stateLabel.setIcon(AllIcons.General.BalloonError)
        stateLabel.setText(reply.reason)
      }
    } catch {
      case e: Throwable =>
        userToken = None
        stateLabel.setIcon(AllIcons.General.BalloonError)
        stateLabel.setText(ScaledaBundle.message("settings.dialog.login.reply.err.net", e.getMessage))
        e.printStackTrace()
    }
    updateButtonEnable()
  }

  override protected def canClickOk = userToken.isDefined
}
