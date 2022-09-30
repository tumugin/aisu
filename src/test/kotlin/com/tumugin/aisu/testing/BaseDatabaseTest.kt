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
      // 外部キー制約があるテーブルをTRUNCATEするために一時的に制約を取る
      TransactionManager.current().exec("SET FOREIGN_KEY_CHECKS = 0")
      // flywayのテーブルを除いて全てのテーブルをTRUNCATEする
      db.dialect.allTablesNames().filterNot {
        it.contains("flyway_schema_history") || it.contains("FLYWAY_SCHEMA_HISTORY")
      }.forEach {
        TransactionManager.current().exec("TRUNCATE TABLE $it")
      }
      // 一時的に外していた制約を戻す
      TransactionManager.current().exec("SET FOREIGN_KEY_CHECKS = 1")
    }
  }
}
