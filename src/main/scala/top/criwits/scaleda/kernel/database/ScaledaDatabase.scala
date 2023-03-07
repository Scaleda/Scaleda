package top.criwits.scaleda
package kernel.database

import kernel.database.dao.User
import kernel.utils.{KernelLogger, Paths}

import org.apache.commons.io.IOUtils

import java.io.File
import java.sql.{Connection, DriverManager, SQLException}
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

  private def getConnection: Connection = {
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
    statementUpdate.execute(sqlUpdate)
    statementUpdate.close()
  }

  private def dropDatabase(): Unit = {
    val sqlDrop       = loadSqlFromResource("database/drop.sql")
    val statementDrop = getConnection.createStatement
    statementDrop.setQueryTimeout(30)
    statementDrop.execute(sqlDrop)
    statementDrop.close()
  }

  def forceCleanDatabase(): Unit = {
    dropDatabase()
    updateDatabase()
  }

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
    result.toSeq
  }

  def insertUser(user: User): Unit = {
    val userNow = findUser(user.getUsername)
    if (userNow.isDefined) throw new UserException("user exists")
    val preparing = getConnection.prepareStatement("INSERT INTO t_user (username, password, nickname) VALUES (?, ?, ?)")
    Seq(user.getUsername, user.getPassword, user.getNickname).zipWithIndex.foreach(t =>
      preparing.setString(t._2 + 1, t._1)
    )
    preparing.execute()
  }

  def deleteUser(username: String): Unit = {
    val preparing = getConnection.prepareStatement("DELETE FROM t_user WHERE username=?")
    preparing.setString(1, username)
    preparing.execute()
  }

  def checkAndUpdatePassword(username: String, oldPassword: String, newPassword: String): Unit = {
    val userNow = findUser(username, password = Some(oldPassword))
    // TODO: i18n
    if (userNow.isEmpty)
      throw new UserException("username or password error")
    val preparing = getConnection.prepareStatement("UPDATE t_user SET password=? WHERE username=?")
    preparing.setString(1, newPassword)
    preparing.setString(2, username)
    preparing.execute()
  }

  def userAuthority(username: String, password: String): Boolean = {
    val preparing = getConnection.prepareStatement("SELECT * FROM t_user WHERE username=? AND password=?")
    preparing.setString(1, username)
    preparing.setString(2, password)
    val resultSet = preparing.executeQuery()
    resultSet.next()
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
