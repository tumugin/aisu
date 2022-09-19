package com.tumugin.aisu.domain.app.config

data class AppConfig(
  val appConfigDatabaseJdbcUrl: AppConfigDatabaseJdbcUrl,
  val appConfigDatabaseUserName: AppConfigDatabaseUserName,
  val appConfigDatabasePassword: AppConfigDatabasePassword,
  val appConfigRedisHost: AppConfigRedisHost,
  val appConfigRedisPort: AppConfigRedisPort,
  val appEnvironment: AppEnvironment,
  val appConfigCookieSecretSignKey: AppConfigCookieSecretSignKey
)
