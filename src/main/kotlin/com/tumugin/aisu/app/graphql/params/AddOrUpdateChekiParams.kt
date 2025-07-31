@file:OptIn(kotlin.time.ExperimentalTime::class)

package com.tumugin.aisu.app.graphql.params

import com.expediagroup.graphql.generator.annotations.GraphQLIgnore
import com.expediagroup.graphql.generator.scalars.ID
import com.tumugin.aisu.app.request.BaseRequest
import com.tumugin.aisu.app.request.ValidatorPatterns.ISO8601Pattern
import com.tumugin.aisu.app.serializer.IDSerializer
import com.tumugin.aisu.domain.cheki.ChekiQuantity
import com.tumugin.aisu.domain.cheki.ChekiShotAt
import com.tumugin.aisu.domain.idol.IdolId
import com.tumugin.aisu.domain.regulation.RegulationId
import io.konform.validation.Validation
import io.konform.validation.jsonschema.minimum
import io.konform.validation.jsonschema.pattern
import kotlin.time.ExperimentalTime
import kotlinx.serialization.Serializable

@Serializable
class AddOrUpdateChekiParams(
  @Serializable(with = IDSerializer::class) val idolId: ID,
  @Serializable(with = IDSerializer::class) val regulationId: ID?,
  val chekiQuantity: Int,
  val chekiShotAt: String
) : BaseRequest<AddOrUpdateChekiParams> {
  @GraphQLIgnore
  override val validator: Validation<AddOrUpdateChekiParams> = Validation {
    AddOrUpdateChekiParams::idolId required {
      run(aisuIdsValidator)
    }
    AddOrUpdateChekiParams::regulationId ifPresent {
      run(aisuIdsValidator)
    }
    AddOrUpdateChekiParams::chekiQuantity required {
      minimum(1)
    }
    AddOrUpdateChekiParams::chekiShotAt required {
      pattern(ISO8601Pattern)
    }
  }

  @GraphQLIgnore
  val castedIdolId
    get() = IdolId(idolId.value.toLong())

  @GraphQLIgnore
  val castedRegulationId
    get() = regulationId?.value?.toLong()?.let { RegulationId(it) }

  @GraphQLIgnore
  val castedChekiQuantity
    get() = ChekiQuantity(chekiQuantity)

  @GraphQLIgnore
  val castedChekiShotAt
    get() = ChekiShotAt(kotlin.time.Instant.parse(chekiShotAt))

  init {
    validate(this)
  }
}
