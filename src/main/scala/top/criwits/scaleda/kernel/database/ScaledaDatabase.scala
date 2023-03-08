package top.criwits.scaleda
package kernel.database

import kernel.auth.JwtManager
import kernel.database.dao.{Token, User}
import kernel.utils.{KernelLogger, Paths}

import org.apache.commons.io.IOUtils

import java.io.File
import java.sql._
import java.time.{Duration, Instant, ZoneId, ZonedDateTime}
import java.util
import scala.collection.mutable.ArrayBuffer

class ScaledaDatabase {
  private val jdbcUrl                      = s"jdbc:sqlite:${new File(Paths.getDatabaseDir, "scaleda.sqlite").getAbsolutePath}"
  private var inner_connection: Connection = _

  private def doConnect(): Unit = {
    if (inner_connection != null && !inner_connection.isClosed) inner_connection.close()
    try {
      inner_connection = DriverManager.getConnection(jdbcUrl)
    } catch {
      case e: SQLException =>
        KernelLogger.error("cannot load database:", e)
        try {
          if (inner_connection != null) inner_connection.close()
        } catch {
          case e: SQLException =>
            KernelLogger.error("cannot close inner_connection when exit", e)
        }
    }
    inner_connection.setAutoCommit(true)
  }

  doConnect()

  def getConnection: Connection = {
    // auto reconnect
    if (inner_connection == null || (inner_connection != null && inner_connection.isClosed))
      doConnect()
    inner_connection
  }

  private def loadSqlFromResource(path: String): String =
    IOUtils.toString(getClass.getClassLoader.getResourceAsStream(path), "UTF-8")

  private def updateDatabase(): Unit = {
    val sqlUpdate       = loadSqlFromResource("database/scaleda.sql")
    val statementUpdate = getConnection.createStatement
    statementUpdate.setQueryTimeout(30)
    statementUpdate.executeUpdate(sqlUpdate)
    statementUpdate.close()
  }

  private def dropDatabase(): Unit = {
    val sqlDrop       = loadSqlFromResource("database/drop.sql")
    val statementDrop = getConnection.createStatement
    statementDrop.setQueryTimeout(30)
    statementDrop.executeUpdate(sqlDrop)
    statementDrop.close()
  }

  def forceCleanDatabase(): Unit = {
    dropDatabase()
    updateDatabase()
  }

  updateDatabase()

  // t_user

  def findUser(username: String, password: Option[String] = None): Option[User] =
    selectUser(username, password).headOption

  def selectUser(username: String, password: Option[String] = None): Seq[User] = {
    val preparing =
      if (password.isDefined)
        getConnection.prepareStatement("SELECT username,password,nickname FROM t_user WHERE username=? AND password=?")
      else
        getConnection.prepareStatement("SELECT username,password,nickname FROM t_user WHERE username=?")

    preparing.setString(1, username)
    password.foreach(password => preparing.setString(2, password))
    val resultSet = preparing.executeQuery()
    val result    = ArrayBuffer[User]()
    while (resultSet.next()) {
      val user = new User
      user.setUsername(resultSet.getString("username"))
      user.setPassword(resultSet.getString("password"))
      user.setNickname(resultSet.getString("nickname"))
      result.addOne(user)
    }
    resultSet.close()
    preparing.close()
    result.toSeq
  }

  def insertUser(user: User): Unit = {
    val userNow = findUser(user.getUsername)
    if (userNow.isDefined) throw new UserException("user exists")
    val preparing = getConnection.prepareStatement("INSERT INTO t_user (username, password, nickname) VALUES (?, ?, ?)")
    Seq(user.getUsername, user.getPassword, user.getNickname).zipWithIndex.foreach(t =>
      preparing.setString(t._2 + 1, t._1)
    )
    preparing.executeUpdate()
    preparing.close()
  }

  def deleteUser(username: String): Unit = {
    val preparing = getConnection.prepareStatement("DELETE FROM t_user WHERE username=?")
    preparing.setString(1, username)
    preparing.executeUpdate()
    preparing.close()
  }

  def checkAndUpdatePassword(username: String, oldPassword: String, newPassword: String): Unit = {
    val userNow = findUser(username, password = Some(oldPassword))
    // TODO: i18n
    if (userNow.isEmpty)
      throw new UserException("username or password error")
    val preparing = getConnection.prepareStatement("UPDATE t_user SET password=? WHERE username=?")
    preparing.setString(1, newPassword)
    preparing.setString(2, username)
    preparing.executeUpdate()
    preparing.close()
  }

  def userAuthority(username: String, password: String): Boolean = {
    val preparing = getConnection.prepareStatement("SELECT * FROM t_user WHERE username=? AND password=?")
    preparing.setString(1, username)
    preparing.setString(2, password)
    val resultSet = preparing.executeQuery()
    val ok        = resultSet.next()
    resultSet.close()
    preparing.close()
    ok
  }

  // t_token

  def createToken(
      username: String,
      provider: Map[String, String] => Option[String] = claims => JwtManager.createToken(claims = claims)
  ): Token = {
    // val token     = provider(Map("username" -> username)).get
    // val exp       = JwtManager.decode(token).get.getExpiresAt
    val token     = "test-token"
    val exp       = new Date(new util.Date().getTime + Duration.ofSeconds(2).toMillis)
    val preparing = getConnection.prepareStatement("INSERT INTO t_token (token, username, exp) VALUES (?, ?, ?)")
    preparing.setString(1, token)
    preparing.setString(2, username)
    // convert writing timezone to utc
    // preparing.setDate(
    //   3,
    //   new Date(
    //     ZonedDateTime
    //       .ofInstant(
    //         new util.Date(exp.getTime).toInstant,
    //         ZoneId.of("UTC")
    //         // ZoneId.systemDefault()
    //       )
    //       .toInstant
    //       .toEpochMilli
    //   )
    // )
    preparing.setTimestamp(3, Timestamp.from(new util.Date(exp.getTime).toInstant))
    preparing.executeUpdate()
    preparing.close()
    new Token(token, username, exp)
  }

  def findToken(token: String): Option[Token] = {
    val preparing = getConnection.prepareStatement(
      "SELECT token, username, exp FROM t_token WHERE token=?"
    )
    preparing.setString(1, token)
    val resultSet = preparing.executeQuery()
    val result    = ArrayBuffer[Token]()
    while (resultSet.next()) {
      val t = new Token()
      t.setToken(resultSet.getString("token"))
      t.setUsername(resultSet.getString("username"))
      // convert time from utc to timezone
      // t.setExp(
      //   new util.Date(
      //     ZonedDateTime
      //       .ofInstant(new util.Date(resultSet.getDate("exp").getTime).toInstant, ZoneId.systemDefault())
      //       .toInstant
      //       .toEpochMilli
      //   )
      // )
      t.setExp(resultSet.getTimestamp("exp"))
      result.addOne(t)
    }
    resultSet.close()
    preparing.close()
    result.headOption
  }

  def tokenToUser(token: String): Option[User] =
    findToken(token).flatMap(t => findUser(t.getUsername))

  def manuallyCleanToken(): Unit = {
    // val st = getConnection.createStatement()
    // st.executeUpdate("DELETE FROM t_token WHERE exp < CURRENT_TIMESTAMP")
    // st.close()
    {
      val preparing = getConnection.prepareStatement("SELECT token, username, exp FROM t_token WHERE exp < ?")
      preparing.setTimestamp(1, Timestamp.from(Instant.now()))
      val resultSet = preparing.executeQuery()
      val result    = ArrayBuffer[Token]()
      while (resultSet.next()) {
        result.addOne(
          new Token(resultSet.getString("token"), resultSet.getString("username"), resultSet.getTimestamp("exp"))
        )
      }
      preparing.close()
      for (token <- result) {
        KernelLogger.warn("will delete:", token, "now", Timestamp.from(Instant.now()))
      }
    }
    {
      val preparing = getConnection.prepareStatement("DELETE FROM t_token WHERE exp < ?")
      preparing.setTimestamp(1, Timestamp.from(Instant.now()))
      preparing.executeUpdate()
      preparing.close()
    }
  }

}

object ScaledaDatabaseTest extends App {
  val db = new ScaledaDatabase
  db.forceCleanDatabase()
}

object ScaledaDatabaseUserTest extends App {
  val db = new ScaledaDatabase
  db.forceCleanDatabase()
  val user = new User("test", "test", "test")
  db.insertUser(user)
  try {
    db.insertUser(user)
  } catch {
    case e: UserException => KernelLogger.info(e)
  }
  db.checkAndUpdatePassword(user.getUsername, user.getPassword, "new")
  val userNow = db.findUser(user.getUsername, Some("new")).get
  KernelLogger.info("now:", userNow)
}

object ScaledaDatabaseTokenTest extends App {
  val db = new ScaledaDatabase
  db.forceCleanDatabase()
  val token = db.createToken("test")
  val load  = db.findToken(token.getToken).get
  KernelLogger.info("now", new util.Date().getTime, "token", token.getExp.getTime, "load", load.getExp.getTime)
  KernelLogger.info(
    "now",
    new util.Date().toInstant,
    "token",
    new util.Date(token.getExp.getTime).toInstant,
    "load",
    new util.Date(load.getExp.getTime).toInstant
  )
  val delta = token.getExp.getTime - new util.Date().getTime
  KernelLogger.info("delta", delta)
  val newDate = new util.Date(new util.Date().getTime + Duration.ofMinutes(1).toMillis)
  val delta2  = newDate.getTime - new util.Date().getTime
  KernelLogger.info("delta2", delta2, "newDate", newDate)
  // Thread.sleep(3000)
  db.manuallyCleanToken()
  // val resultSet = db.getConnection.createStatement().executeQuery("SELECT CURRENT_TIMESTAMP")
  val resultSet = db.getConnection.createStatement().executeQuery("SELECT datetime('now')")
  val date      = resultSet.getDate(1)
  // new util.Date(date.getTime).
  KernelLogger.info("now utc:", Instant.now())
  KernelLogger.info("db now:", new util.Date(date.getTime + Duration.ofHours(8).toMillis).toInstant)
  val instant = ZonedDateTime.ofInstant(new util.Date(date.getTime).toInstant, ZoneId.systemDefault())
  KernelLogger.info("instant:", instant)
  KernelLogger.info("instant date:", util.Date.from(instant.toInstant))
  KernelLogger.info("sys now:", new util.Date(System.currentTimeMillis()).toInstant)
  val found = db.findToken(token.getToken)
  KernelLogger.info("token:", new util.Date(token.getExp.getTime).toInstant, "found:", found.isDefined)
}
