package com.tumugin.aisu.app.graphql.params

import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import com.expediagroup.graphql.generator.scalars.ID
import com.tumugin.aisu.app.request.BaseRequest
import com.tumugin.aisu.app.serializer.IDSerializer
import com.tumugin.aisu.domain.idol.IdolId
import io.konform.validation.Validation
import io.konform.validation.jsonschema.enum
import kotlinx.datetime.TimeZone
import kotlinx.serialization.Serializable

class GetChekiMonthCountByIdolParams(
  val baseTimezone: String,
  @Serializable(with = IDSerializer::class) val idolId: ID,
) : BaseRequest<GetChekiMonthCountByIdolParams> {
  @GraphQLIgnore
  override val validator: Validation<GetChekiMonthCountByIdolParams> = Validation {
    GetChekiMonthCountByIdolParams::baseTimezone required {
      enum(*TimeZone.availableZoneIds.toTypedArray())
    }
  }

  @GraphQLIgnore
  val castedBaseTimezone: TimeZone
    get() = TimeZone.of(baseTimezone)

  @GraphQLIgnore
  val castedIdolId: IdolId
    get() = IdolId(idolId.value.toLong())

  init {
    validate(this)
  }
}
