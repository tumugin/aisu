package com.tumugin.aisu.app.graphql.params

import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import com.tumugin.aisu.app.request.BaseRequest
import com.tumugin.aisu.domain.user.UserName
import io.konform.validation.Validation
import io.konform.validation.constraints.maxLength
import io.konform.validation.constraints.minLength
import kotlinx.serialization.Serializable

@Serializable
class UpdateUserNameParams(
  val userName: String
) : BaseRequest<UpdateUserNameParams> {
  @GraphQLIgnore
  override val validator = Validation {
    UpdateUserNameParams::userName {
      minLength(1)
      maxLength(255)
    }
  }

  @GraphQLIgnore
  val castedUserName
    get() = UserName(userName)

  init {
    validate(this)
  }
}
