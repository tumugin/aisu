package com.tumugin.aisu.app.responder.chekis

import com.tumugin.aisu.app.responder.BaseResponder
import com.tumugin.aisu.app.serializer.client.ChekiSerializer
import com.tumugin.aisu.domain.cheki.Cheki

class ChekisGetResponder(val value: ChekiSerializer) : BaseResponder {
  override val status = "ok"

  companion object {
    fun createResponse(value: Cheki): ChekisGetResponder {
      return ChekisGetResponder(ChekiSerializer.from(value))
    }
  }
}
