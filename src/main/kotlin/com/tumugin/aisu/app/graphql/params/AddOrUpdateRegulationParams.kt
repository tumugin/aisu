package com.tumugin.aisu.app.graphql.params

import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import com.expediagroup.graphql.generator.scalars.ID
import com.tumugin.aisu.app.request.BaseRequest
import com.tumugin.aisu.app.serializer.IDSerializer
import com.tumugin.aisu.domain.group.GroupId
import com.tumugin.aisu.domain.regulation.RegulationComment
import com.tumugin.aisu.domain.regulation.RegulationName
import com.tumugin.aisu.domain.regulation.RegulationStatus
import com.tumugin.aisu.domain.regulation.RegulationUnitPrice
import io.konform.validation.Validation
import io.konform.validation.jsonschema.enum
import io.konform.validation.jsonschema.minLength
import io.konform.validation.jsonschema.minimum
import kotlinx.serialization.Serializable

@Serializable
class AddOrUpdateRegulationParams(
  @Serializable(with = IDSerializer::class) val groupId: ID,
  val regulationName: String,
  val regulationComment: String,
  val regulationUnitPrice: Int,
  val regulationStatus: RegulationStatus
) : BaseRequest<AddOrUpdateRegulationParams> {
  @GraphQLIgnore
  override val validator = Validation<AddOrUpdateRegulationParams> {
    AddOrUpdateRegulationParams::groupId required {
      run(aisuIdsValidator)
    }

    AddOrUpdateRegulationParams::regulationComment required {
      minLength(0)
    }

    AddOrUpdateRegulationParams::regulationName required {
      minLength(1)
    }

    AddOrUpdateRegulationParams::regulationUnitPrice required {
      minimum(0)
    }

    AddOrUpdateRegulationParams::regulationStatus required {
      enum(*RegulationStatus.allUserCanSpecifyStatus.toTypedArray())
    }
  }

  @GraphQLIgnore
  val castedGroupId
    get() = GroupId(groupId.value.toLong())

  @GraphQLIgnore
  val castedRegulationName
    get() = RegulationName(regulationName)

  @GraphQLIgnore
  val castedRegulationComment
    get() = RegulationComment(regulationComment)

  @GraphQLIgnore
  val castedRegulationUnitPrice
    get() = RegulationUnitPrice(regulationUnitPrice)

  init {
    validate(this)
  }
}
