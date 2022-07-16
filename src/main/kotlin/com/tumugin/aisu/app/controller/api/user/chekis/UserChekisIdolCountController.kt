package com.tumugin.aisu.app.controller.api.user.chekis

import com.tumugin.aisu.app.plugins.UserAuthSession
import com.tumugin.aisu.app.request.api.user.chekis.UserChekisIdolCountIndexRequest
import com.tumugin.aisu.app.responder.api.user.chekis.UserChekisIdolCountResponder
import com.tumugin.aisu.usecase.client.cheki.GetCheki
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.sessions.*

/**
 * @url /api/user/chekis/idol_count
 */
class UserChekisIdolCountController {
  private val getCheki = GetCheki()

  suspend fun get(call: ApplicationCall) {
    val session = call.sessions.get<UserAuthSession>()!!
    val request = UserChekisIdolCountIndexRequest.createByGetParameters(call.parameters)

    val chekiIdolCount = getCheki.getChekiIdolCountByUserId(
      session.castedUserId,
      request.chekiShotAtStartCasted,
      request.chekiShotAtEndCasted
    )

    call.respond(UserChekisIdolCountResponder.createResponse(chekiIdolCount))
  }
}
