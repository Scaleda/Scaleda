package top.criwits.scaleda
package kernel

import kernel.database.dao.User
import kernel.database.{ScaledaDatabase, UserException}
import kernel.utils.KernelLogger

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should

import java.time.{Duration, Instant, ZoneId, ZonedDateTime}
import java.util.Date

class DatabaseTest extends AnyFlatSpec with should.Matchers {
  behavior of "Database"
  it should "test force-clean database" in {
    val db = new ScaledaDatabase
    db.forceCleanDatabase()
  }
  it should "test database user" in {
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
  it should "test database token" in {
    val db = new ScaledaDatabase
    db.forceCleanDatabase()
    val token = db.createToken("test").get
    val load  = db.findToken(token.getToken).get
    KernelLogger.info("now", new Date().getTime, "token", token.getExp.getTime, "load", load.getExp.getTime)
    KernelLogger.info(
      "now",
      new Date().toInstant,
      "token",
      new Date(token.getExp.getTime).toInstant,
      "load",
      new Date(load.getExp.getTime).toInstant
    )
    val delta = token.getExp.getTime - new Date().getTime
    KernelLogger.info("delta", delta)
    val newDate = new Date(new Date().getTime + Duration.ofMinutes(1).toMillis)
    val delta2  = newDate.getTime - new Date().getTime
    KernelLogger.info("delta2", delta2, "newDate", newDate)
    // Thread.sleep(3000)
    db.manuallyCleanToken()
    // val resultSet = db.getConnection.createStatement().executeQuery("SELECT CURRENT_TIMESTAMP")
    val resultSet = db.getConnection.createStatement().executeQuery("SELECT datetime('now')")
    val date      = resultSet.getDate(1)
    // new Date(date.getTime).
    KernelLogger.info("now utc:", Instant.now())
    KernelLogger.info("db now:", new Date(date.getTime + Duration.ofHours(8).toMillis).toInstant)
    val instant = ZonedDateTime.ofInstant(new Date(date.getTime).toInstant, ZoneId.systemDefault())
    KernelLogger.info("instant:", instant)
    KernelLogger.info("instant date:", Date.from(instant.toInstant))
    KernelLogger.info("sys now:", new Date(System.currentTimeMillis()).toInstant)
    val found = db.findToken(token.getToken)
    KernelLogger.info("token:", new Date(token.getExp.getTime).toInstant, "found:", found.isDefined)
  }
}
