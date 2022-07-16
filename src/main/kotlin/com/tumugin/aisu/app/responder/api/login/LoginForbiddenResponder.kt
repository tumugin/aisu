package com.tumugin.aisu.app.responder.api.login

import com.tumugin.aisu.app.responder.BaseResponder
import com.tumugin.aisu.app.responder.ResponseStatusType
import kotlinx.serialization.Serializable

@Serializable
data class LoginForbiddenResponder(
  val message: String = "email or password is incorrect."
) : BaseResponder {
  override val status = ResponseStatusType.FAILED
}
