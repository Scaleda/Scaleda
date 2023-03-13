package top.criwits.scaleda
package idea.settings.auth

import idea.ScaledaBundle
import kernel.database.dao.User
import kernel.net.RemoteServer
import kernel.net.user.{ScaledaAuthorizationProvider, ScaledaRegisterLogin, UserTokenBean}

import com.intellij.icons.AllIcons

class LoginDialog(save: Boolean = true, data: Option[UserTokenBean] = None, ignoredHosts: Seq[String] = Seq())
    extends LoginDialogInner {
  private var userToken: Option[UserTokenBean] = data
  userToken.foreach(t => {
    usernameField.setText(t.username)
    hostField.setText(t.host)
  })

  private def updateButtonEnable(): Unit = {
    buttonOK.setEnabled(canClickOk)
  }
  updateButtonEnable()
  if (data.isEmpty) {
    RemoteServer.AVAILABLE_REMOTE_HOSTS.headOption.foreach(host => {
      hostField.setText(host)
    })
  }

  def getUserToken = userToken
  def setUserToken(t: Option[UserTokenBean]): Unit =
    userToken = t

  override protected def onOK(): Unit = {
    if (save) userToken.foreach(t => ScaledaAuthorizationProvider.saveTokenPair(t))
    dispose()
  }

  override protected def onCancel(): Unit = {
    dispose()
  }

  override protected def onLogin(): Unit = {
    val host     = hostField.getText
    val username = usernameField.getText
    val password = String.valueOf(passwordField.getPassword)
    if (ignoredHosts.contains(host)) {
      stateLabel.setIcon(AllIcons.General.Warning)
      stateLabel.setText(ScaledaBundle.message("settings.dialog.login.host.repeated", host))
      userToken = None
      return
    }
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
    }
    updateButtonEnable()
  }

  override protected def canClickOk = userToken.isDefined && userToken.get.refreshToken.nonEmpty

  override protected def onRegister() = {
    val host     = hostField.getText
    val username = usernameField.getText
    val password = String.valueOf(passwordField.getPassword)
    val buttons  = Seq(buttonOK, buttonLogin, buttonRegister, buttonCancel)
    buttons.foreach(_.setEnabled(false))
    try {
      val handler = new ScaledaRegisterLogin(host = host)
      val reply   = handler.register(new User(username, password, ""))
      if (reply.ok) {
        stateLabel.setIcon(AllIcons.General.InspectionsOK)
        stateLabel.setText(ScaledaBundle.message("settings.dialog.register.reply.ok"))
        onLogin()
      } else {
        stateLabel.setIcon(AllIcons.General.BalloonError)
        stateLabel.setText(reply.reason)
        userToken = None
      }
    } catch {
      case e: Throwable =>
        stateLabel.setIcon(AllIcons.General.BalloonError)
        stateLabel.setText(ScaledaBundle.message("settings.dialog.login.reply.err.net", e.getMessage))
        e.printStackTrace()
        userToken = None
    } finally {
      buttons.foreach(_.setEnabled(true))
      updateButtonEnable()
    }
  }
}
