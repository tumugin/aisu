package com.tumugin.aisu.app.request.api

import com.tumugin.aisu.app.request.BaseRequest
import com.tumugin.aisu.app.request.ValidatorPatterns.RFC5322EmailPattern
import io.konform.validation.Validation
import io.konform.validation.jsonschema.minLength
import io.konform.validation.jsonschema.pattern
import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(val email: String, val password: String): BaseRequest<LoginRequest> {
  override val validator = Validation<LoginRequest> {
    LoginRequest::email {
      pattern(RFC5322EmailPattern)
    }
    LoginRequest::password {
      minLength(1)
    }
  }

  init {
    validate(this)
  }
}
