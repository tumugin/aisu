package com.tumugin.aisu.app.graphql.params

import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import com.expediagroup.graphql.generator.scalars.ID
import com.tumugin.aisu.app.request.BaseRequest
import com.tumugin.aisu.app.request.ValidatorPatterns.ISO8601Pattern
import com.tumugin.aisu.app.serializer.IDSerializer
import com.tumugin.aisu.domain.cheki.ChekiShotAt
import com.tumugin.aisu.domain.idol.IdolId
import io.konform.validation.Validation
import io.konform.validation.jsonschema.pattern
import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
class GetUserChekisParams(
  val chekiShotAtStart: String,
  val chekiShotAtEnd: String,
  @Serializable(with = IDSerializer::class)
  val idolId: ID?
) :
  BaseRequest<GetUserChekisParams> {

  @GraphQLIgnore
  val chekiShotAtStartCasted: ChekiShotAt
    get() = ChekiShotAt(Instant.parse(chekiShotAtStart))

  @GraphQLIgnore
  val chekiShotAtEndCasted: ChekiShotAt
    get() = ChekiShotAt(Instant.parse(chekiShotAtEnd))

  @GraphQLIgnore
  val idolIdCasted: IdolId?
    get() = idolId?.let { IdolId(it.value.toLong()) }

  @GraphQLIgnore
  override val validator: Validation<GetUserChekisParams> = Validation {
    GetUserChekisParams::chekiShotAtStart required {
      pattern(ISO8601Pattern)
    }
    GetUserChekisParams::chekiShotAtEnd required {
      pattern(ISO8601Pattern)
    }
  }

  init {
    validate(this)
  }
}
