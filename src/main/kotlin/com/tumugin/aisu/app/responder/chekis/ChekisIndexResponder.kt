package com.tumugin.aisu.app.responder.chekis

import com.tumugin.aisu.app.responder.BaseResponder
import com.tumugin.aisu.app.serializer.client.ChekiSerializer
import com.tumugin.aisu.domain.cheki.Cheki

@kotlinx.serialization.Serializable
class ChekisIndexResponder(
  val value: List<ChekiSerializer>
) : BaseResponder {
  override val status = "ok"

  companion object {
    fun createResponse(resultChekis: List<Cheki>): ChekisIndexResponder {
      return ChekisIndexResponder(
        value = resultChekis.map { ChekiSerializer.from(it) }
      )
    }
  }
}
