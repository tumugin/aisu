package com.tumugin.aisu.domain.app.config

data class AppConfig(
  val appConfigDatabaseJdbcUrl: AppConfigDatabaseJdbcUrl,
  val appConfigDatabaseUserName: AppConfigDatabaseUserName,
  val appConfigDatabasePassword: AppConfigDatabasePassword
)