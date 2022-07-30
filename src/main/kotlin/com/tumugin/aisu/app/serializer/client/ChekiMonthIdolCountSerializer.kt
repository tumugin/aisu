package com.tumugin.aisu.app.serializer.client

import com.expediagroup.graphql.generator.scalars.ID
import com.tumugin.aisu.app.serializer.IDSerializer
import com.tumugin.aisu.domain.cheki.ChekiMonthIdolCount
import kotlinx.serialization.Serializable

@Serializable
class ChekiMonthIdolCountSerializer(
  val idol: IdolSerializer?,
  @Serializable(with = IDSerializer::class)
  val idolId: ID,
  val chekiCount: Int,
  val chekiShotAtMonth: ChekiShotAtMonthSerializer
) {
  companion object {
    fun from(chekiMonthIdolCount: ChekiMonthIdolCount): ChekiMonthIdolCountSerializer {
      return ChekiMonthIdolCountSerializer(
        chekiMonthIdolCount.idol?.let { IdolSerializer.from(it) },
        ID(chekiMonthIdolCount.idolId.toString()),
        chekiMonthIdolCount.chekiCount.value,
        ChekiShotAtMonthSerializer.from(chekiMonthIdolCount.chekiShotAtMonth)
      )
    }
  }
}
