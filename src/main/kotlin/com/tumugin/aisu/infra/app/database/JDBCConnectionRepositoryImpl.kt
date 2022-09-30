package com.tumugin.aisu.infra.app.database

import com.tumugin.aisu.domain.app.config.AppConfigRepository
import com.tumugin.aisu.domain.app.database.JDBCConnectionRepository
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database

class JDBCConnectionRepositoryImpl(private val appConfigRepository: AppConfigRepository) : JDBCConnectionRepository {
  override val dataSource by lazy {
    HikariDataSource(this.createHikariConfig())
  }

  override fun prepareORM() {
    Database.connect(this.dataSource)
  }

  override fun closeConnection() {
    this.dataSource.close()
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
    return config
  }
}
