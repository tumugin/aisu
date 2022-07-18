package com.tumugin.aisu.app.responder.api.user.chekis

import com.tumugin.aisu.app.responder.BaseResponder
import com.tumugin.aisu.app.responder.ResponseStatusType
import com.tumugin.aisu.app.serializer.client.ChekiIdolCountSerializer
import com.tumugin.aisu.app.serializer.client.IdolSerializer
import com.tumugin.aisu.domain.cheki.ChekiIdolCount

@kotlinx.serialization.Serializable
class UserChekisIdolCountResponder(val value: List<ChekiIdolCountSerializer>) : BaseResponder {
  override val status = ResponseStatusType.SUCCESS

  companion object {
    fun createResponse(chekiIdolCount: List<ChekiIdolCount>): UserChekisIdolCountResponder {
      return UserChekisIdolCountResponder(chekiIdolCount.map {
        ChekiIdolCountSerializer(
          it.idol?.let { idol -> IdolSerializer.from(idol) },
          it.chekiCount.value
        )
      })
    }
  }
}
