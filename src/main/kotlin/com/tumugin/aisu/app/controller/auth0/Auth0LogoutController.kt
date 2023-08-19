package com.tumugin.aisu.app.controller.auth0

import com.tumugin.aisu.app.plugins.UserAuthSession
import com.tumugin.aisu.usecase.client.auth0.GetAuth0LogoutUrl
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.sessions.*
import org.koin.core.component.KoinComponent

class Auth0LogoutController : KoinComponent {
  private val getAuth0LogoutUrl = GetAuth0LogoutUrl()

  suspend fun post(call: ApplicationCall) {
    call.sessions.clear<UserAuthSession>()
    call.respondRedirect(
      getAuth0LogoutUrl.getAuth0LogoutUrl().value
    )
  }
}
