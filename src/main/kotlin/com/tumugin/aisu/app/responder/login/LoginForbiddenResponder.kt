package com.tumugin.aisu.app.responder.login

import com.tumugin.aisu.app.responder.BaseResponder

data class LoginForbiddenResponder(
  override val status: String = "error",
  val message: String = "email or password is incorrect."
) : BaseResponder
