package com.tumugin.aisu.app.graphql.params

import com.tumugin.aisu.app.request.BaseRequest
import io.konform.validation.Validation
import io.konform.validation.jsonschema.enum
import kotlinx.datetime.TimeZone

class GetChekiMonthIdolCountParams(private val baseTimezone: String) : BaseRequest<GetChekiMonthIdolCountParams> {
  override val validator: Validation<GetChekiMonthIdolCountParams> = Validation {
    GetChekiMonthIdolCountParams::baseTimezone required {
      enum(*TimeZone.availableZoneIds.toTypedArray())
    }
  }

  val castedBaseTimezone: TimeZone
    get() = TimeZone.of(baseTimezone)

  init {
    validate(this)
  }
}
