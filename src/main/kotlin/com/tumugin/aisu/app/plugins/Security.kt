package com.tumugin.aisu.app.plugins

import com.tumugin.aisu.app.plugins.security.KVSSessionStorage
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

data class AdminUserAuthSession(val userId: Long, val validThroughTimestamp: String, val forceLogoutGeneration: Int) {
  val castedAdminUserId
    get() = AdminUserId(userId)
}

data class UserPrincipal(val user: User) : Principal

fun Application.configureSecurity() {
  install(Sessions) {
    cookie<UserAuthSession>("USER_AUTH", KVSSessionStorage()) {
      cookie.httpOnly = true
      cookie.extensions["SameSite"] = "lax"
    }
  }
}
