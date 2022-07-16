package com.tumugin.aisu.app.responder.api.chekis

import com.tumugin.aisu.app.responder.BaseResponder
import com.tumugin.aisu.app.responder.ResponseStatusType
import com.tumugin.aisu.app.serializer.client.ChekiSerializer
import com.tumugin.aisu.domain.cheki.Cheki

/**
 * @url /api/chekis/{id}
 */
@kotlinx.serialization.Serializable
class ChekisGetResponder(val value: ChekiSerializer) : BaseResponder {
  override val status = ResponseStatusType.SUCCESS

  companion object {
    fun createResponse(value: Cheki): ChekisGetResponder {
      return ChekisGetResponder(ChekiSerializer.from(value))
    }
  }
}
