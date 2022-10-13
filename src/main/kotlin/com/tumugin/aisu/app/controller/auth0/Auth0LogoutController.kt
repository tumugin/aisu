package com.tumugin.aisu.app.controller.auth0

import com.tumugin.aisu.app.plugins.UserAuthSession
import com.tumugin.aisu.domain.app.config.AppConfigRepository
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.sessions.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.net.URLEncoder

class Auth0LogoutController : KoinComponent {
  private val appConfigRepository by inject<AppConfigRepository>()

  suspend fun post(call: ApplicationCall) {
    call.sessions.clear<UserAuthSession>()
    call.respondRedirect(
      "https://${appConfigRepository.appConfig.appConfigAuth0Domain.value}/v2/logout?" +
        "returnTo=${URLEncoder.encode(appConfigRepository.appConfig.appConfigAppUrl.value, "utf-8")}&" +
        "client_id=${appConfigRepository.appConfig.appConfigAuth0ClientId.value}"
    )
  }
}
