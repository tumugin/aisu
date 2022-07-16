package com.tumugin.aisu.app.responder.api.login

import com.tumugin.aisu.app.responder.BaseResponder
import kotlinx.serialization.Serializable

@Serializable
data class LoginForbiddenResponder(
  override val status: String = "error",
  val message: String = "email or password is incorrect."
) : BaseResponder
