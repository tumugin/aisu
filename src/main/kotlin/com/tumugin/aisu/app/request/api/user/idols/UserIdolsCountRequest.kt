package com.tumugin.aisu.app.request.api.user.idols

import com.tumugin.aisu.app.request.BaseRequest
import io.konform.validation.Validation
import io.konform.validation.jsonschema.enum
import io.konform.validation.jsonschema.minLength
import kotlinx.datetime.TimeZone

class UserIdolsCountRequest(private val baseTimezone: String?) : BaseRequest<UserIdolsCountRequest> {
  override val validator: Validation<UserIdolsCountRequest> = Validation {
    UserIdolsCountRequest::baseTimezone required {
      enum(*TimeZone.availableZoneIds.toTypedArray())
    }
  }

  val castedBaseTimezone: TimeZone
    get() = TimeZone.of(baseTimezone!!)

  init {
    validate(this)
  }
}
