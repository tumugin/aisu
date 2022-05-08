package com.tumugin.aisu.app.plugins

import com.tumugin.aisu.app.plugins.security.KVSSessionStorage
import com.tumugin.aisu.domain.user.User
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.sessions.*

data class UserAuthSession(val userId: Int, val validThroughTimestamp: String, val forceLogoutGeneration: Int)

data class UserPrincipal(val user: User) : Principal

fun Application.configureSecurity() {
  install(Sessions) {
    cookie<UserAuthSession>("USER_AUTH", KVSSessionStorage()) {
      cookie.httpOnly = true
      cookie.extensions["SameSite"] = "lax"
    }
  }
}
