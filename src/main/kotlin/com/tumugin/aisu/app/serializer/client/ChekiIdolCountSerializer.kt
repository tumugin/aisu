package com.tumugin.aisu.app.serializer.client

import com.expediagroup.graphql.generator.scalars.ID
import com.tumugin.aisu.app.serializer.IDSerializer
import com.tumugin.aisu.domain.cheki.ChekiIdolCount
import kotlinx.serialization.Serializable

@Serializable
class ChekiIdolCountSerializer(
  val idol: IdolSerializer?,
  @Serializable(with = IDSerializer::class)
  val idolId: ID,
  val chekiCount: Int
) {
  companion object {
    fun from(chekiIdolCount: ChekiIdolCount): ChekiIdolCountSerializer {
      return ChekiIdolCountSerializer(
        chekiIdolCount.idol?.let { IdolSerializer.from(it) },
        ID(chekiIdolCount.idolId.toString()),
        chekiIdolCount.chekiCount.value
      )
    }
  }
}
