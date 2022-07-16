package com.tumugin.aisu.app.request.api.chekis

import com.tumugin.aisu.app.request.BaseRequest
import com.tumugin.aisu.app.request.ValidatorPatterns.ISO8601Pattern
import com.tumugin.aisu.domain.cheki.ChekiShotAt
import com.tumugin.aisu.domain.idol.IdolId
import io.konform.validation.Validation
import io.konform.validation.jsonschema.minimum
import io.konform.validation.jsonschema.pattern
import io.ktor.http.*
import kotlinx.datetime.Instant
import kotlinx.serialization.Transient

@kotlinx.serialization.Serializable
class ChekisIndexRequest(
  private val chekiShotAtStart: String?,
  private val chekiShotAtEnd: String?,
  private val idolId: Long?
) :
  BaseRequest<ChekisIndexRequest> {

  val chekiShotAtStartCasted: ChekiShotAt
    get() = ChekiShotAt(Instant.parse(chekiShotAtStart!!))

  val chekiShotAtEndCasted: ChekiShotAt
    get() = ChekiShotAt(Instant.parse(chekiShotAtEnd!!))

  val idolIdCasted: IdolId?
    get() = idolId?.let { IdolId(it) }

  @Transient
  override val validator: Validation<ChekisIndexRequest> = Validation {
    ChekisIndexRequest::chekiShotAtStart required {
      pattern(ISO8601Pattern)
    }
    ChekisIndexRequest::chekiShotAtEnd required {
      pattern(ISO8601Pattern)
    }
    ChekisIndexRequest::idolId ifPresent {
      minimum(1)
    }
  }

  companion object {
    fun createByGetParameters(parameters: Parameters): ChekisIndexRequest {
      val chekiShotAtStart = parameters["chekiShotAtStart"]
      val chekiShotAtEnd = parameters["chekiShotAtEnd"]
      val idolId = parameters["idolId"]
      return ChekisIndexRequest(chekiShotAtStart, chekiShotAtEnd, idolId?.toLong())
    }
  }

  init {
    validate(this)
  }
}
