package com.tumugin.aisu.app.serializer.client

import com.tumugin.aisu.domain.idol.*

@kotlinx.serialization.Serializable
class IdolSerializer(
  val idolId: Long,
  val userId: Long?,
  val idolName: String,
  val idolStatus: String,
  val idolCreatedAt: String,
  val idolUpdatedAt: String,
) {
  companion object {
    fun fromIdol(idol: Idol): IdolSerializer {
      return IdolSerializer(
        idolId = idol.idolId.value,
        userId = idol.userId?.value,
        idolName = idol.idolName.value,
        idolStatus = idol.idolStatus.name,
        idolCreatedAt = idol.idolCreatedAt.value.toString(),
        idolUpdatedAt = idol.idolUpdatedAt.value.toString(),
      )
    }
  }
}
