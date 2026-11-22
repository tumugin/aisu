package com.tumugin.aisu.app.request.api

import com.tumugin.aisu.app.request.BaseRequest
import com.tumugin.aisu.app.request.ValidatorPatterns
import com.tumugin.aisu.domain.user.UserEmail
import com.tumugin.aisu.domain.user.UserName
import com.tumugin.aisu.domain.user.UserRawPassword
import io.konform.validation.Validation
import io.konform.validation.constraints.maxLength
import io.konform.validation.constraints.minLength
import io.konform.validation.constraints.pattern
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class CreateUserRequest(val email: String, val password: String, val name: String) :
  BaseRequest<CreateUserRequest> {
  @Transient
  override val validator = Validation {
    CreateUserRequest::email {
      pattern(ValidatorPatterns.RFC5322EmailPattern)
      maxLength(255)
    }
    CreateUserRequest::password {
      minLength(7)
      maxLength(1000)
    }
    CreateUserRequest::name {
      minLength(1)
      maxLength(255)
    }
  }

  val castedEmail: UserEmail
    get() = UserEmail(email)

  val castedPassword: UserRawPassword
    get() = UserRawPassword(password)

  val castedName: UserName
    get() = UserName(name)

  init {
    validate(this)
  }
}
