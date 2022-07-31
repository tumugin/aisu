package com.tumugin.aisu.app.graphql.params

import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import com.tumugin.aisu.app.request.BaseRequest
import com.tumugin.aisu.app.request.ValidatorPatterns
import com.tumugin.aisu.domain.cheki.ChekiShotAt
import io.konform.validation.Validation
import io.konform.validation.jsonschema.pattern
import kotlinx.datetime.Instant

@kotlinx.serialization.Serializable
class GetUserChekiIdolCountParams(
  val chekiShotAtStart: String,
  val chekiShotAtEnd: String,
) : BaseRequest<GetUserChekiIdolCountParams> {
  @GraphQLIgnore
  val chekiShotAtStartCasted: ChekiShotAt
    get() = ChekiShotAt(Instant.parse(chekiShotAtStart))

  @GraphQLIgnore
  val chekiShotAtEndCasted: ChekiShotAt
    get() = ChekiShotAt(Instant.parse(chekiShotAtEnd))

  @GraphQLIgnore
  override val validator = Validation {
    GetUserChekiIdolCountParams::chekiShotAtStart required {
      pattern(ValidatorPatterns.ISO8601Pattern)
    }
    GetUserChekiIdolCountParams::chekiShotAtEnd required {
      pattern(ValidatorPatterns.ISO8601Pattern)
    }
  }

  init {
    validate(this)
  }
}
