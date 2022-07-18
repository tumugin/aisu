package com.tumugin.aisu.app.controller.api

import com.tumugin.aisu.app.plugins.UserAuthSession
import com.tumugin.aisu.app.responder.api.MetadataResponder
import com.tumugin.aisu.usecase.client.user.GetUser
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.sessions.*

class MetadataController {
  private val getuser = GetUser()

  suspend fun get(call: ApplicationCall) {
    val session = call.sessions.get<UserAuthSession>()
    val user = session?.let { getuser.getUserBySessionUserId(it.castedUserId) }

    call.respond(MetadataResponder.from("", user))
  }
}
