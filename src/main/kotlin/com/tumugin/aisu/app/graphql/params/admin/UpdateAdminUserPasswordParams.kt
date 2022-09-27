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
class UpdateAdminUserPasswordParams(
  val password: String,
) : BaseRequest<UpdateAdminUserPasswordParams> {
  @GraphQLIgnore
  override val validator = Validation {
    UpdateAdminUserPasswordParams::password required {
      minLength(10)
    }
  }

  @GraphQLIgnore
  val castedAdminUserRawPassword: AdminUserRawPassword
    get() = AdminUserRawPassword(password)

  init {
    validate(this)
  }
}
