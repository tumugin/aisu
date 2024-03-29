package com.tumugin.aisu.app.graphql.params.admin

import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import com.tumugin.aisu.app.request.BaseRequest
import com.tumugin.aisu.app.request.ValidatorPatterns
import io.konform.validation.Validation
import io.konform.validation.jsonschema.minLength
import io.konform.validation.jsonschema.pattern
import kotlinx.serialization.Serializable

@Serializable
class AdminUserLoginParams(
  val email: String, val password: String
) : BaseRequest<AdminUserLoginParams> {
  @GraphQLIgnore
  override val validator = Validation {
    AdminUserLoginParams::email required {
      pattern(ValidatorPatterns.RFC5322EmailPattern)
    }
    AdminUserLoginParams::password required {
      minLength(1)
    }
  }

  init {
    validate(this)
  }
}
