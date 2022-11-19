package com.tumugin.aisu.app.controller.auth0

import com.tumugin.aisu.app.plugins.UserAuthSession
import com.tumugin.aisu.usecase.client.auth0.LoginOrCreateUserByAuth0Callback
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.*
import io.ktor.server.response.*
import io.ktor.server.sessions.*
import kotlinx.datetime.Clock
import kotlin.time.Duration.Companion.days

class Auth0CallbackController {
  private val loginOrCreateUserByAuth0Callback = LoginOrCreateUserByAuth0Callback()

  suspend fun get(call: ApplicationCall) {
    val returnTo = call.request.queryParameters["return_to"]
    val principal: OAuthAccessTokenResponse.OAuth2 = call.principal() ?: throw BadRequestException("Invalid token")
    val user = loginOrCreateUserByAuth0Callback.getOrCreateUserByPrincipal(principal).user
    call.sessions.set(
      UserAuthSession(
        userId = user.userId.value,
        validThroughTimestamp = Clock.System.now().plus(30.days).toString(),
        forceLogoutGeneration = user.userForceLogoutGeneration.value
      )
    )
    if (returnTo?.isNotBlank() == true) {
      call.respondRedirect(returnTo)
      return
    }
    call.respondRedirect("/")
  }
}
