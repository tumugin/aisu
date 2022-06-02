package com.tumugin.aisu.testing

import com.tumugin.aisu.di.AisuDIModule
import com.tumugin.aisu.domain.app.database.JDBCConnectionRepository
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.core.context.GlobalContext
import org.koin.test.KoinTest
import kotlin.test.BeforeTest

abstract class BaseDatabaseTest : KoinTest {
  private fun setupApplication() {
    if (GlobalContext.getOrNull() !== null) {
      return
    }
    AisuDIModule.startTesting()
    GlobalContext.get().get<JDBCConnectionRepository>().prepareORM()
  }

  @BeforeTest
  fun beforeBaseTest() {
    setupApplication()
    truncateDatabase()
  }

  private fun truncateDatabase() {
    transaction {
      // 外部キー制約があるテーブルをTRUNCATEするために一時的に制約を取る
      TransactionManager.current().exec("SET FOREIGN_KEY_CHECKS = 0")
      // flywayのテーブルを除いて全てのテーブルをTRUNCATEする
      db.dialect.allTablesNames().filterNot { it.contains("flyway_schema_history") }.forEach {
        TransactionManager.current().exec("TRUNCATE TABLE $it")
      }
      // 一時的に外していた制約を戻す
      TransactionManager.current().exec("SET FOREIGN_KEY_CHECKS = 1")
    }
  }
}
