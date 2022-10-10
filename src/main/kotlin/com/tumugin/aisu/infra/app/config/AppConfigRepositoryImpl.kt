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
      appConfigCookieSecretSignKey = AppConfigCookieSecretSignKey(env["COOKIE_SECRET_SIGN_KEY"]),
      appConfigAuth0ClientId = AppConfigAuth0ClientId(env["AUTH0_CLIENT_ID"]),
      appConfigAuth0ClientSecret = AppConfigAuth0ClientSecret(env["AUTH0_CLIENT_SECRET"]),
      appConfigAuth0Domain = AppConfigAuth0Domain(env["AUTH0_DOMAIN"]),
      appConfigAppUrl = AppConfigAppUrl(env["APP_URL"]),
      appConfigAdminAuth0ClientId = AppConfigAdminAuth0ClientId(env["ADMIN_AUTH0_CLIENT_ID"]),
      appConfigAdminAuth0ClientSecret = AppConfigAdminAuth0ClientSecret(env["ADMIN_AUTH0_CLIENT_SECRET"]),
      appConfigAdminAuth0Domain = AppConfigAdminAuth0Domain(env["ADMIN_AUTH0_DOMAIN"]),
      appConfigAdminAppUrl = AppConfigAdminAppUrl(env["ADMIN_APP_URL"])
    )
  }
}
