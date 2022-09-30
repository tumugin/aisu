package com.tumugin.aisu.app.graphql.params.admin

import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import com.tumugin.aisu.app.request.BaseRequest
import com.tumugin.aisu.app.request.ValidatorPatterns
import com.tumugin.aisu.domain.adminUser.AdminUserEmail
import com.tumugin.aisu.domain.adminUser.AdminUserName
import com.tumugin.aisu.domain.adminUser.AdminUserRawPassword
import io.konform.validation.Validation
import io.konform.validation.jsonschema.minLength
import io.konform.validation.jsonschema.pattern
import kotlinx.serialization.Serializable

@Serializable
class AddAdminUserParams(
  val name: String,
  val email: String,
  val password: String,
) : BaseRequest<AddAdminUserParams> {
  @GraphQLIgnore
  override val validator = Validation {
    AddAdminUserParams::email required {
      pattern(ValidatorPatterns.RFC5322EmailPattern)
    }
    AddAdminUserParams::password required {
      minLength(10)
    }
    AddAdminUserParams::name required {
      minLength(1)
    }
  }

  @GraphQLIgnore
  val castedAdminUserName: AdminUserName
    get() = AdminUserName(name)

  @GraphQLIgnore
  val castedAdminUserEmail: AdminUserEmail
    get() = AdminUserEmail(email)

  @GraphQLIgnore
  val castedAdminUserRawPassword: AdminUserRawPassword
    get() = AdminUserRawPassword(password)

  init {
    validate(this)
  }
}
