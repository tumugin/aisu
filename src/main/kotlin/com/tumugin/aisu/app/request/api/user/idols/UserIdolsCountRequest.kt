package com.tumugin.aisu.app.request.api.user.idols

import com.tumugin.aisu.app.request.BaseRequest
import io.konform.validation.Validation
import io.konform.validation.jsonschema.enum
import io.ktor.http.*
import kotlinx.datetime.TimeZone

/**
 * @url /user/idols/{idolId}/count
 */
class UserIdolsCountRequest(private val baseTimezone: String?) : BaseRequest<UserIdolsCountRequest> {
  override val validator: Validation<UserIdolsCountRequest> = Validation {
    UserIdolsCountRequest::baseTimezone required {
      enum(*TimeZone.availableZoneIds.toTypedArray())
    }
  }

  val castedBaseTimezone: TimeZone
    get() = TimeZone.of(baseTimezone!!)

  companion object {
    fun fromGetParameters(parameters: Parameters): UserIdolsCountRequest {
      return UserIdolsCountRequest(parameters["baseTimezone"])
    }
  }

  init {
    validate(this)
  }
}
