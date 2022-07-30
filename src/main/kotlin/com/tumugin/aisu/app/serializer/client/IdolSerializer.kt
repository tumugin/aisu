package com.tumugin.aisu.app.serializer.client

import com.expediagroup.graphql.generator.scalars.ID
import com.tumugin.aisu.app.serializer.IDSerializer
import com.tumugin.aisu.domain.idol.*
import kotlinx.serialization.Serializable

@Serializable
class IdolSerializer(
  @Serializable(with = IDSerializer::class)
  val idolId: ID,
  val idol: IdolSerializer? = null,
  @Serializable(with = IDSerializer::class)
  val userId: ID?,
  val user: LimitedUserSerializer? = null,
  val idolName: String,
  val idolStatus: String,
  val idolCreatedAt: String,
  val idolUpdatedAt: String,
) {
  companion object {
    fun from(idol: Idol): IdolSerializer {
      return IdolSerializer(
        idolId = ID(idol.idolId.value.toString()),
        userId = idol.userId?.let { ID(it.value.toString()) },
        idolName = idol.idolName.value,
        idolStatus = idol.idolStatus.name,
        idolCreatedAt = idol.idolCreatedAt.value.toString(),
        idolUpdatedAt = idol.idolUpdatedAt.value.toString(),
      )
    }
  }
}
