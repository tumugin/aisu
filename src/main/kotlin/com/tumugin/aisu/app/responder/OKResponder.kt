package com.tumugin.aisu.app.responder

import kotlinx.serialization.Serializable

@Serializable
class OKResponder : BaseResponder {
  override val status = ResponseStatusType.SUCCESS
}
