package com.tumugin.aisu.app.controller.admin

import com.tumugin.aisu.app.plugins.AdminUserAuthSession
import com.tumugin.aisu.usecase.admin.adminUser.LoginOrCreateAdminUserByAuth0Callback
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.*
import io.ktor.server.response.*
import io.ktor.server.sessions.*
import kotlinx.datetime.Clock
import kotlin.time.Duration.Companion.days

class AdminAuth0CallbackController {
  private val loginOrCreateAdminUserByAuth0Callback = LoginOrCreateAdminUserByAuth0Callback()

  suspend fun get(call: ApplicationCall) {
    val principal: OAuthAccessTokenResponse.OAuth2 = call.principal() ?: throw BadRequestException("Invalid token")
    val adminUser = loginOrCreateAdminUserByAuth0Callback.getOrCreateAdminUserByPrincipal(principal)
    call.sessions.set(
      AdminUserAuthSession(
        adminUserId = adminUser.adminUserId.value,
        validThroughTimestamp = Clock.System.now().plus(AdminUserAuthSession.defaultValidDays).toString(),
        forceLogoutGeneration = adminUser.adminUserForceLogoutGeneration.value
      )
    )
    call.respondRedirect("/")
  }
}
