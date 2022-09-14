package com.tumugin.aisu.infra.app.config

import com.tumugin.aisu.domain.app.config.*
import io.github.cdimascio.dotenv.dotenv

class AppConfigRepositoryImpl(private val isTesting: Boolean = false) : AppConfigRepository {
  override val appConfig by lazy {
    val env = dotenv {
      ignoreIfMissing = true
      filename = if (isTesting) {
        ".env.testing"
      } else {
        ".env"
      }
    }
    AppConfig(
      appConfigDatabaseJdbcUrl = AppConfigDatabaseJdbcUrl(env["DB_JDBC_URL"]),
      appConfigDatabaseUserName = AppConfigDatabaseUserName(env["DB_USERNAME"]),
      appConfigDatabasePassword = AppConfigDatabasePassword(env["DB_PASSWORD"]),
      appConfigRedisHost = AppConfigRedisHost(env["REDIS_HOST"]),
      appConfigRedisPort = AppConfigRedisPort(env["REDIS_PORT"].toInt()),
      appEnvironment = AppEnvironment.valueOf(env["APP_ENV"].uppercase()),
    )
  }
}
