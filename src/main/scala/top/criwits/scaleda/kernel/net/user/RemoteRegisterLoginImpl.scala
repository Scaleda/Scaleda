package top.criwits.scaleda
package kernel.net.user

import kernel.auth.JwtManager
import kernel.database.dao.User
import kernel.database.{ScaledaDatabase, UserException}
import kernel.net.remote._

import scala.async.Async.async
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class RemoteRegisterLoginImpl extends RemoteRegisterLoginGrpc.RemoteRegisterLogin {
  override def register(request: RemoteRegisterRequest): Future[RemoteRegisterReply] = async {
    try {
      val db = new ScaledaDatabase
      db.insertUser(new User(request.username, request.password, request.nickname))
      RemoteRegisterReply.of(ok = true, "ok")
    } catch {
      case e: UserException => RemoteRegisterReply.of(ok = false, e.getMessage)
    }
  }

  override def login(request: RemoteLoginRequest): Future[RemoteLoginReply] = async {
    try {
      val db   = new ScaledaDatabase
      val user = db.findUser(request.username, password = Some(request.password))
      if (user.nonEmpty) {
        val token = db.createToken(request.username, claims => JwtManager.createRefreshToken(claims = claims))
        RemoteLoginReply.of(ok = true, token.getToken, "ok")
      } else {
        // TODO: i18n
        RemoteLoginReply.of(ok = false, "", "username or password error")
      }
    } catch {
      case e: UserException => RemoteLoginReply.of(ok = false, "", e.getMessage)
    }
  }

  override def refresh(request: RemoteRefreshRequest): Future[RemoteRefreshReply] = async {
    val db    = new ScaledaDatabase
    val token = db.findToken(request.refreshToken)
    if (token.isEmpty) {
      // TODO: i18n
      RemoteRefreshReply.of(ok = false, "", "Token invalid")
    } else {
      val tokenNew = db.createToken(token.get.getUsername, claims => JwtManager.createToken(claims = claims))
      RemoteRefreshReply.of(ok = true, tokenNew.getToken, "ok")
    }
  }
}
