package com.tumugin.aisu

import com.tumugin.aisu.di.AisuDIModule
import com.tumugin.aisu.domain.app.database.JDBCConnectionRepository
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.BeforeClass
import org.koin.core.context.GlobalContext
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.get
import kotlin.test.BeforeTest

abstract class BaseTest : KoinTest {
  private fun setupApplication() {
    if (GlobalContext.getOrNull() !== null) {
      return
    }
    AisuDIModule.startTesting()
    GlobalContext.get().get<JDBCConnectionRepository>().prepareORM()
  }

  @BeforeTest
  fun beforeTest() {
    setupApplication()
    truncateDatabase()
  }

  private fun truncateDatabase() {
    transaction {
      db.dialect.allTablesNames().filterNot { it.contains("flyway_schema_history") }.forEach {
        TransactionManager.current().exec("TRUNCATE TABLE $it")
      }
    }
  }
}
