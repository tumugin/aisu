package com.tumugin.aisu.app.graphql.params.admin

import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import com.tumugin.aisu.app.request.BaseRequest
import com.tumugin.aisu.app.request.ValidatorPatterns
import com.tumugin.aisu.domain.adminUser.AdminUserEmail
import com.tumugin.aisu.domain.adminUser.AdminUserName
import io.konform.validation.Validation
import io.konform.validation.jsonschema.minLength
import io.konform.validation.jsonschema.pattern
import kotlinx.serialization.Serializable

@Serializable
class UpdateAdminUserParams(
  val name: String,
  val email: String,
) : BaseRequest<UpdateAdminUserParams> {
  @GraphQLIgnore
  override val validator = Validation {
    UpdateAdminUserParams::email required {
      pattern(ValidatorPatterns.RFC5322EmailPattern)
    }
    UpdateAdminUserParams::name required {
      minLength(1)
    }
  }

  @GraphQLIgnore
  val castedAdminUserName: AdminUserName
    get() = AdminUserName(name)

  @GraphQLIgnore
  val castedAdminUserEmail: AdminUserEmail
    get() = AdminUserEmail(email)

  init {
    validate(this)
  }
}
