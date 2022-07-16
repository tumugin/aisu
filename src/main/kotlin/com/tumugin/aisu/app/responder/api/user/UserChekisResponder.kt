package com.tumugin.aisu.app.responder.api.user

import com.tumugin.aisu.app.responder.BaseResponder
import com.tumugin.aisu.app.responder.ResponseStatusType
import com.tumugin.aisu.app.serializer.client.ChekiSerializer
import com.tumugin.aisu.domain.cheki.Cheki

/**
 * @url /api/user/chekis
 */
@kotlinx.serialization.Serializable
class UserChekisResponder(
  val value: List<ChekiSerializer>
) : BaseResponder {
  override val status = ResponseStatusType.SUCCESS

  companion object {
    fun createResponse(resultChekis: List<Cheki>): UserChekisResponder {
      return UserChekisResponder(
        value = resultChekis.map { ChekiSerializer.from(it) }
      )
    }
  }
}
