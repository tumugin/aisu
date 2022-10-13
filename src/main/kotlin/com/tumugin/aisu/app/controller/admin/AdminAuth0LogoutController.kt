package com.tumugin.aisu.app.controller.admin

import com.tumugin.aisu.app.plugins.AdminUserAuthSession
import com.tumugin.aisu.domain.app.config.AppConfigRepository
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.sessions.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.net.URLEncoder

class AdminAuth0LogoutController : KoinComponent {
  private val appConfigRepository by inject<AppConfigRepository>()

  suspend fun post(call: ApplicationCall) {
    call.sessions.clear<AdminUserAuthSession>()
    call.respondRedirect(
      "https://${appConfigRepository.appConfig.appConfigAdminAuth0Domain.value}/v2/logout?" +
        "returnTo=${URLEncoder.encode(appConfigRepository.appConfig.appConfigAdminAppUrl.value, "utf-8")}&" +
        "client_id=${appConfigRepository.appConfig.appConfigAdminAuth0ClientId.value}"
    )
  }
}
