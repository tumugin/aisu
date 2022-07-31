package com.tumugin.aisu.app.graphql.params

import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import com.tumugin.aisu.app.request.BaseRequest
import io.konform.validation.Validation
import io.konform.validation.jsonschema.enum
import kotlinx.datetime.TimeZone

class GetChekiMonthIdolCountParams(val baseTimezone: String) : BaseRequest<GetChekiMonthIdolCountParams> {
  @GraphQLIgnore
  override val validator: Validation<GetChekiMonthIdolCountParams> = Validation {
    GetChekiMonthIdolCountParams::baseTimezone required {
      enum(*TimeZone.availableZoneIds.toTypedArray())
    }
  }

  @GraphQLIgnore
  val castedBaseTimezone: TimeZone
    get() = TimeZone.of(baseTimezone)

  init {
    validate(this)
  }
}
