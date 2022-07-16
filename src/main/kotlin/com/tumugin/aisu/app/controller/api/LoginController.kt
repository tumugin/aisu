package com.tumugin.aisu.app.controller.api

import com.tumugin.aisu.app.plugins.UserAuthSession
import com.tumugin.aisu.app.request.api.LoginRequest
import com.tumugin.aisu.app.responder.OKResponder
import com.tumugin.aisu.app.responder.api.login.LoginForbiddenResponder
import com.tumugin.aisu.domain.user.UserEmail
import com.tumugin.aisu.domain.user.UserRawPassword
import com.tumugin.aisu.usecase.client.user.AuthUser
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.sessions.*
import kotlinx.datetime.Clock
import kotlin.time.Duration.Companion.days

class LoginController {
  private val authUserUseCase = AuthUser()

  suspend fun post(call: ApplicationCall) {
    val loginRequest = call.receive<LoginRequest>()
    val user = authUserUseCase.authAndGetUser(
      UserEmail(loginRequest.email), UserRawPassword(loginRequest.password)
    )
    if (user == null) {
      call.respond(HttpStatusCode.Forbidden, LoginForbiddenResponder())
      return
    }
    call.sessions.set(
      UserAuthSession(
        userId = user.userId.value,
        validThroughTimestamp = Clock.System.now().plus(30.days).toString(),
        forceLogoutGeneration = user.userForceLogoutGeneration.value
      )
    )
    call.respond(OKResponder())
  }
}
