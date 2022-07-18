package com.tumugin.aisu.app.responder.api

import com.tumugin.aisu.app.responder.BaseResponder
import com.tumugin.aisu.app.responder.ResponseStatusType
import com.tumugin.aisu.app.serializer.client.UserSerializer
import com.tumugin.aisu.domain.user.User

@kotlinx.serialization.Serializable
class MetadataResponder(val value: MetadataResponderValue) : BaseResponder {
  override val status = ResponseStatusType.SUCCESS

  companion object {
    fun from(csrfToken: String, user: User?): MetadataResponder {
      return MetadataResponder(MetadataResponderValue(csrfToken, user?.let { UserSerializer.from(it) }))
    }
  }
}

@kotlinx.serialization.Serializable
data class MetadataResponderValue(val csrfToken: String, val user: UserSerializer?) {
}
