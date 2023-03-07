package top.criwits.scaleda
package kernel.database

import kernel.utils.{KernelLogger, Paths}

import org.apache.commons.io.IOUtils

import java.io.File
import java.sql.{Connection, DriverManager, SQLException}

class ScaledaDatabase {
  val jdbcUrl = s"jdbc:sqlite:${new File(Paths.getDatabaseDir, "scaleda.sqlite").getAbsolutePath}"
  def getConnection: Connection = {
    var inner_connection: Connection = null
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
}

object ScaledaDatabaseTest extends App {
  val db = new ScaledaDatabase
  db.forceCleanDatabase()
}
