package com.tumugin.aisu.infra.app.database

import com.tumugin.aisu.domain.app.config.AppConfigRepository
import com.tumugin.aisu.domain.app.database.JDBCConnectionRepository
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database

class JDBCConnectionRepositoryImpl(private val appConfigRepository: AppConfigRepository) : JDBCConnectionRepository {
  var isConnected: Boolean = false
    private set

  private var hikariConnection: HikariDataSource? = null

  override val dataSource
    get() = this.hikariConnection ?: throw IllegalStateException("Hikari connection is not initialized.")

  private fun prepareHikari() {
    if (hikariConnection == null) {
      hikariConnection = HikariDataSource(this.createHikariConfig())
    }
  }

  override fun prepareORM() {
    if (isConnected) {
      return
    }
    prepareHikari()
    Database.connect(this.dataSource)
    isConnected = true
  }

  override fun closeConnection() {
    isConnected = false
    this.hikariConnection?.close()
    this.hikariConnection = null
  }

  override fun migrate() {
    val flyway = Flyway.configure().dataSource(
      appConfigRepository.appConfig.appConfigDatabaseJdbcUrl.value,
      appConfigRepository.appConfig.appConfigDatabaseUserName.value,
      appConfigRepository.appConfig.appConfigDatabasePassword.value,
    ).load()
    flyway.migrate()
  }

  private fun createHikariConfig(): HikariConfig {
    val config = HikariConfig()
    config.jdbcUrl = appConfigRepository.appConfig.appConfigDatabaseJdbcUrl.value
    config.username = appConfigRepository.appConfig.appConfigDatabaseUserName.value
    config.password = appConfigRepository.appConfig.appConfigDatabasePassword.value
    config.minimumIdle = 1
    config.maximumPoolSize = 20
    return config
  }
}
