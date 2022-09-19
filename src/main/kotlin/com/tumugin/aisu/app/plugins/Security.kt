package com.tumugin.aisu.app.plugins

import com.tumugin.aisu.app.plugins.security.KVSSessionStorage
import com.tumugin.aisu.domain.adminUser.AdminUser
import com.tumugin.aisu.domain.adminUser.AdminUserId
import com.tumugin.aisu.domain.app.config.AppConfigRepository
import com.tumugin.aisu.domain.user.User
import com.tumugin.aisu.domain.user.UserId
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.sessions.*
import io.ktor.util.*
import org.koin.core.Koin

data class UserAuthSession(val userId: Long, val validThroughTimestamp: String, val forceLogoutGeneration: Int) {
  val castedUserId
    get() = UserId(userId)
}

data class AdminUserAuthSession(
  val adminUserId: Long, val validThroughTimestamp: String, val forceLogoutGeneration: Int
) {
  val castedAdminUserId
    get() = AdminUserId(adminUserId)
}

data class UserPrincipal(val user: User) : Principal

data class AdminUserPrincipal(val adminUser: AdminUser) : Principal

fun Application.configureSecurity(koin: Koin) {
  val appConfigRepository = koin.get<AppConfigRepository>()
  val secretSignKey = hex(appConfigRepository.appConfig.appConfigCookieSecretSignKey.value)
  install(Sessions) {
    cookie<UserAuthSession>("USER_AUTH", KVSSessionStorage()) {
      cookie.httpOnly = true
      cookie.extensions["SameSite"] = "lax"
      transform(SessionTransportTransformerMessageAuthentication(secretSignKey))
    }
    cookie<AdminUserAuthSession>("ADMIN_USER_AUTH", KVSSessionStorage()) {
      cookie.httpOnly = true
      cookie.extensions["SameSite"] = "lax"
      transform(SessionTransportTransformerMessageAuthentication(secretSignKey))
    }
  }
}
