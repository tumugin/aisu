package com.tumugin.aisu.app.graphql.params

import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import com.tumugin.aisu.app.request.BaseRequest
import com.tumugin.aisu.domain.group.GroupName
import com.tumugin.aisu.domain.group.GroupStatus
import io.konform.validation.Validation
import io.konform.validation.jsonschema.enum
import io.konform.validation.jsonschema.minLength
import kotlinx.serialization.Serializable

@Serializable
class AddOrUpdateGroupParams(
  val groupName: String,
  val groupStatus: GroupStatus
) : BaseRequest<AddOrUpdateGroupParams> {
  @GraphQLIgnore
  override val validator: Validation<AddOrUpdateGroupParams> = Validation {
    AddOrUpdateGroupParams::groupName required {
      minLength(1)
    }
    AddOrUpdateGroupParams::groupStatus required {
      enum(*GroupStatus.allUserCanSpecifyStatus.toTypedArray())
    }
  }

  @GraphQLIgnore
  val castedGroupName
    get() = GroupName(groupName)

  init {
    validate(this)
  }
}
