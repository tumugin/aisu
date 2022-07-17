package com.tumugin.aisu.app.request.api.user.idols

import com.tumugin.aisu.app.request.BaseRequest
import io.konform.validation.Validation

class UserIdolsCountRequest(private val baseTimezone: String?) : BaseRequest<UserIdolsCountRequest> {
  override val validator: Validation<UserIdolsCountRequest> = Validation {
    UserIdolsCountRequest::baseTimezone required {

    }
  }

  init {
    validate(this)
  }
}
