package com.tumugin.aisu.app.serializer.client

import com.tumugin.aisu.domain.cheki.ChekiMonthCount
import kotlinx.serialization.Serializable

@Serializable
class ChekiMonthCountSerializer(
  val month: ChekiShotAtMonthSerializer,
  val count: Int
) {
  companion object {
    fun from(chekiMonthCount: ChekiMonthCount): ChekiMonthCountSerializer {
      return ChekiMonthCountSerializer(
        ChekiShotAtMonthSerializer.from(chekiMonthCount.month),
        chekiMonthCount.count.value
      )
    }
  }
}
