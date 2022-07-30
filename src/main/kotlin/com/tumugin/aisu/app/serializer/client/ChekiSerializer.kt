package com.tumugin.aisu.app.serializer.client

import com.expediagroup.graphql.generator.scalars.ID
import com.tumugin.aisu.app.serializer.IDSerializer
import com.tumugin.aisu.domain.cheki.Cheki
import kotlinx.serialization.Serializable

@Serializable
data class ChekiSerializer(
  @Serializable(with = IDSerializer::class)
  val chekiId: ID,
  @Serializable(with = IDSerializer::class)
  val userId: ID,
  val user: LimitedUserSerializer? = null,
  @Serializable(with = IDSerializer::class)
  val idolId: ID?,
  val idol: IdolSerializer? = null,
  @Serializable(with = IDSerializer::class)
  val regulationId: ID?,
  val regulation: RegulationSerializer? = null,
  val chekiQuantity: Int,
  val chekiShotAt: String,
  val chekiCreatedAt: String,
  val chekiUpdatedAt: String
) {
  companion object {
    fun from(cheki: Cheki): ChekiSerializer {
      return ChekiSerializer(
        chekiId = ID(cheki.chekiId.value.toString()),
        userId = ID(cheki.userId.value.toString()),
        idolId = cheki.idolId?.let { ID(it.value.toString()) },
        regulationId = cheki.regulationId?.let { ID(it.value.toString()) },
        chekiQuantity = cheki.chekiQuantity.value,
        chekiShotAt = cheki.chekiShotAt.value.toString(),
        chekiCreatedAt = cheki.chekiCreatedAt.value.toString(),
        chekiUpdatedAt = cheki.chekiUpdatedAt.value.toString()
      )
    }
  }
}
