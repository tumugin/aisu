package com.tumugin.aisu.app.request.api.user.chekis

import com.tumugin.aisu.app.request.BaseRequest
import com.tumugin.aisu.app.request.ValidatorPatterns
import com.tumugin.aisu.domain.cheki.ChekiShotAt
import io.konform.validation.Validation
import io.konform.validation.jsonschema.pattern
import io.ktor.http.*
import kotlinx.datetime.Instant

class UserChekisIdolCountIndexRequest(
  private val chekiShotAtStart: String?,
  private val chekiShotAtEnd: String?,
) : BaseRequest<UserChekisIdolCountIndexRequest> {
  val chekiShotAtStartCasted: ChekiShotAt
    get() = ChekiShotAt(Instant.parse(chekiShotAtStart!!))

  val chekiShotAtEndCasted: ChekiShotAt
    get() = ChekiShotAt(Instant.parse(chekiShotAtEnd!!))

  override val validator = Validation {
    UserChekisIdolCountIndexRequest::chekiShotAtStart required {
      pattern(ValidatorPatterns.ISO8601Pattern)
    }
    UserChekisIdolCountIndexRequest::chekiShotAtEnd required {
      pattern(ValidatorPatterns.ISO8601Pattern)
    }
  }

  companion object {
    fun createByGetParameters(parameters: Parameters): UserChekisIdolCountIndexRequest {
      return UserChekisIdolCountIndexRequest(
        chekiShotAtStart = parameters["chekiShotAtStart"],
        chekiShotAtEnd = parameters["chekiShotAtEnd"],
      )
    }
  }

  init {
    validate(this)
  }
}
