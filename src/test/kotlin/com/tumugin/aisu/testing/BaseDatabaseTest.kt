package com.tumugin.aisu.testing

import com.tumugin.aisu.di.AisuDIModule
import com.tumugin.aisu.domain.app.database.JDBCConnectionRepository
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.koin.core.context.GlobalContext
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest

abstract class BaseDatabaseTest : KoinTest {
  companion object {
    // NOTE: 一度作ったコネクションは使い回す
    private val jdbcConnectionRepository by lazy { GlobalContext.get().get<JDBCConnectionRepository>() }
  }

  private fun setupApplication() {
    if (GlobalContext.getOrNull() !== null) {
      return
    }
    AisuDIModule.startTesting()
  }

  @BeforeEach
  fun beforeBaseTest() {
    setupApplication()
    jdbcConnectionRepository.prepareORM()
    jdbcConnectionRepository.migrate()
    truncateDatabase()
  }

  @AfterEach
  fun afterEach() {
    stopKoin()
  }

  private fun truncateDatabase() {
    transaction {
      // FIXME: db.dialect.allTablesNames() is broken for now
      val conn = TransactionManager.current().connection
      val statement = conn.prepareStatement(
        "SELECT table_name FROM information_schema.tables WHERE table_schema = 'public'",
        arrayOf()
      )
      var allTablesResultSet = statement.executeQuery()
      val allTableNames = mutableListOf<String>()
      while (allTablesResultSet.next()) {
        allTableNames.add(allTablesResultSet.getString("table_name"))
      }

      // flywayのテーブルを除いて全てのテーブルをTRUNCATEする
      allTableNames.filterNot {
        it.contains("flyway_schema_history") || it.contains("FLYWAY_SCHEMA_HISTORY")
      }.forEach {
        TransactionManager.current().exec("TRUNCATE TABLE $it RESTART IDENTITY CASCADE")
      }
    }
  }
}
