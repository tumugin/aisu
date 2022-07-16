package com.tumugin.aisu.app.controller.api.user

import com.tumugin.aisu.app.plugins.UserAuthSession
import com.tumugin.aisu.app.request.api.chekis.ChekisIndexRequest
import com.tumugin.aisu.app.responder.api.user.UserChekisResponder
import com.tumugin.aisu.usecase.client.cheki.GetCheki
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.sessions.*

/**
 * @url /api/user/chekis
 */
class UserChekisController {
  private val getCheki = GetCheki()

  suspend fun get(call: ApplicationCall) {
    val session = call.sessions.get<UserAuthSession>()!!
    val request = ChekisIndexRequest.createByGetParameters(call.parameters)

    val chekis = if (request.idolIdCasted != null) {
      getCheki.getChekiByUserIdAndShotDateTimeRangeAndIdolId(
        session.castedUserId,
        request.idolIdCasted!!,
        request.chekiShotAtStartCasted,
        request.chekiShotAtEndCasted
      )
    } else {
      getCheki.getChekiByUserIdAndShotDateTimeRange(
        session.castedUserId,
        request.chekiShotAtStartCasted,
        request.chekiShotAtEndCasted
      )
    }

    call.respond(UserChekisResponder.createResponse(chekis))
  }
}
