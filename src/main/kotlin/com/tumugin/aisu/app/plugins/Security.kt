package com.tumugin.aisu.app.plugins

import com.tumugin.aisu.app.plugins.security.KVSSessionStorage
import com.tumugin.aisu.domain.adminUser.AdminUser
import com.tumugin.aisu.domain.adminUser.AdminUserId
import com.tumugin.aisu.domain.user.User
import com.tumugin.aisu.domain.user.UserId
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.sessions.*

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

fun Application.configureSecurity() {
  install(Sessions) {
    // FIXME: protect sessions. see: https://ktor.io/docs/sessions.html#protect_session
    cookie<UserAuthSession>("USER_AUTH", KVSSessionStorage()) {
      cookie.httpOnly = true
      cookie.extensions["SameSite"] = "lax"
    }
    cookie<AdminUserAuthSession>("ADMIN_USER_AUTH", KVSSessionStorage()) {
      cookie.httpOnly = true
      cookie.extensions["SameSite"] = "lax"
    }
  }
}
