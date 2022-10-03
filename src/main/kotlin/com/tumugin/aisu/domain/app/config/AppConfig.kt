package com.tumugin.aisu.domain.app.config

data class AppConfig(
  val appConfigDatabaseJdbcUrl: AppConfigDatabaseJdbcUrl,
  val appConfigDatabaseUserName: AppConfigDatabaseUserName,
  val appConfigDatabasePassword: AppConfigDatabasePassword,
  val appConfigRedisHost: AppConfigRedisHost,
  val appConfigRedisPort: AppConfigRedisPort,
  val appEnvironment: AppEnvironment,
  val appConfigCookieSecretSignKey: AppConfigCookieSecretSignKey,
  val appConfigAuth0ClientId: AppConfigAuth0ClientId,
  val appConfigAuth0ClientSecret: AppConfigAuth0ClientSecret,
  val appConfigAuth0Domain: AppConfigAuth0Domain
)
