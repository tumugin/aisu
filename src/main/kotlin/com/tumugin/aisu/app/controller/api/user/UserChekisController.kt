package com.tumugin.aisu.app.controller.api.user

import com.tumugin.aisu.app.plugins.UserAuthSession
import com.tumugin.aisu.app.request.api.chekis.ChekisIndexRequest
import com.tumugin.aisu.app.responder.chekis.ChekisIndexResponder
import com.tumugin.aisu.usecase.client.cheki.GetCheki
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.sessions.*

class UserChekisController {
  private val getCheki = GetCheki()

  suspend fun index(call: ApplicationCall) {
    val session = call.sessions.get<UserAuthSession>()!!
    val request = ChekisIndexRequest(
      chekiShotAtStart = call.request.queryParameters["chekiShotAtStart"],
      chekiShotAtEnd = call.request.queryParameters["chekiShotAtEnd"],
      idolId = call.request.queryParameters["idolId"]?.toLong(),
    )

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

    call.respond(ChekisIndexResponder.createResponse(chekis))
  }
}
