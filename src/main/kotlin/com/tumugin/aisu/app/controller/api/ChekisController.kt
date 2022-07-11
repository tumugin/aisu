package com.tumugin.aisu.app.controller.api

import com.tumugin.aisu.app.plugins.UserAuthSession
import com.tumugin.aisu.usecase.client.cheki.GetCheki
import io.ktor.server.application.*
import io.ktor.server.sessions.*

class ChekisController {
  private val getCheki = GetCheki()

  suspend fun index(call: ApplicationCall) {
    val session = call.sessions.get<UserAuthSession>()
  }
}
