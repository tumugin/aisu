package com.tumugin.aisu.app.graphql.params

import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import com.tumugin.aisu.app.request.BaseRequest
import com.tumugin.aisu.app.request.ValidatorPatterns
import com.tumugin.aisu.domain.auth0.Auth0MailAddress
import io.konform.validation.Validation
import io.konform.validation.jsonschema.minLength
import io.konform.validation.jsonschema.pattern
import kotlinx.serialization.Serializable

@Serializable
class SendAuth0PasswordResetEmailParams(
  val auth0EmailAddress: String
) : BaseRequest<SendAuth0PasswordResetEmailParams> {
  @GraphQLIgnore
  override val validator = Validation {
    SendAuth0PasswordResetEmailParams::auth0EmailAddress required {
      minLength(1)
      pattern(ValidatorPatterns.RFC5322EmailPattern)
    }
  }

  @GraphQLIgnore
  val castedAuth0EmailAddress
    get() = Auth0MailAddress(auth0EmailAddress)

  init {
    validate(this)
  }
}
