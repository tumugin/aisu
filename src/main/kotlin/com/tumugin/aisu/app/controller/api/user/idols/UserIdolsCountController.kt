package com.tumugin.aisu.app.controller.api.user.idols

import com.tumugin.aisu.app.plugins.UserAuthSession
import com.tumugin.aisu.app.request.api.user.idols.UserIdolsCountRequest
import com.tumugin.aisu.domain.idol.IdolId
import com.tumugin.aisu.usecase.client.cheki.GetCheki
import io.ktor.server.application.*
import io.ktor.server.sessions.*

/**
 * @url /user/idols/{idolId}/count
 */
class UserIdolsCountController {
  private val getCheki = GetCheki()

  suspend fun get(call: ApplicationCall, idolId: IdolId) {
    val session = call.sessions.get<UserAuthSession>()!!
    val request = UserIdolsCountRequest.fromGetParameters(call.parameters)

    val chekiMonthIdolCount = getCheki.getChekiMonthIdolCountByUserIdAndIdol(
      session.castedUserId,
      idolId,
      request.castedBaseTimezone
    )
  }
}
