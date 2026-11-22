package com.tumugin.aisu.app.graphql.params

import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import com.tumugin.aisu.app.request.BaseRequest
import com.tumugin.aisu.domain.idol.IdolName
import com.tumugin.aisu.domain.idol.IdolStatus
import io.konform.validation.Validation
import io.konform.validation.constraints.enum
import io.konform.validation.constraints.minLength
import kotlinx.serialization.Serializable

@Serializable
class AddOrUpdateIdolParams(
  val idolName: String,
  val idolStatus: IdolStatus
) : BaseRequest<AddOrUpdateIdolParams> {
  @GraphQLIgnore
  override val validator: Validation<AddOrUpdateIdolParams> = Validation {
    AddOrUpdateIdolParams::idolName {
      minLength(1)
    }
    AddOrUpdateIdolParams::idolStatus {
      enum(*IdolStatus.allUserCanSpecifyStatus.toTypedArray())
    }
  }

  @GraphQLIgnore
  val castedIdolName
    get() = IdolName(idolName)

  init {
    validate(this)
  }
}
