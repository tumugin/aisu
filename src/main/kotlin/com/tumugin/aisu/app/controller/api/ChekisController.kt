package com.tumugin.aisu.app.controller.api

import com.tumugin.aisu.app.plugins.UserAuthSession
import com.tumugin.aisu.app.responder.chekis.ChekisGetResponder
import com.tumugin.aisu.domain.cheki.ChekiId
import com.tumugin.aisu.usecase.client.cheki.GetCheki
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.response.*
import io.ktor.server.sessions.*

class ChekisController {
  private val getCheki = GetCheki()
  suspend fun get(call: ApplicationCall, id: ChekiId) {
    val session = call.sessions.get<UserAuthSession>()!!
    val cheki = getCheki.getCheki(session.castedUserId, id) ?: throw NotFoundException()
    call.respond(ChekisGetResponder.createResponse(cheki))
  }
}
