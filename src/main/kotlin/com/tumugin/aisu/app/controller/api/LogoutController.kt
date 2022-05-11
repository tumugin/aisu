package com.tumugin.aisu.app.controller.api

import com.tumugin.aisu.app.plugins.UserAuthSession
import com.tumugin.aisu.app.responder.OKResponder
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.sessions.*

class LogoutController {
  suspend fun post(call: ApplicationCall) {
    call.sessions.clear<UserAuthSession>()
    call.respond(OKResponder())
  }
}
