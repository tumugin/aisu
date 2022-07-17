package com.tumugin.aisu.app.serializer.client

import com.tumugin.aisu.domain.cheki.Cheki

@kotlinx.serialization.Serializable
data class ChekiSerializer(
  val chekiId: Long,
  val userId: Long,
  val idolId: Long?,
  val regulationId: Long?,
  val regulation: RegulationSerializer?,
  val chekiQuantity: Int,
  val chekiShotAt: String,
  val chekiCreatedAt: String,
  val chekiUpdatedAt: String
) {
  companion object {
    fun from(cheki: Cheki): ChekiSerializer {
      return ChekiSerializer(
        chekiId = cheki.chekiId.value,
        userId = cheki.userId.value,
        idolId = cheki.idolId?.value,
        regulationId = cheki.regulationId?.value,
        regulation = cheki.regulation?.let { RegulationSerializer.from(it) },
        chekiQuantity = cheki.chekiQuantity.value,
        chekiShotAt = cheki.chekiShotAt.value.toString(),
        chekiCreatedAt = cheki.chekiCreatedAt.value.toString(),
        chekiUpdatedAt = cheki.chekiUpdatedAt.value.toString()
      )
    }
  }
}