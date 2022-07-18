package com.tumugin.aisu.app.serializer.client

import com.tumugin.aisu.domain.cheki.ChekiMonthIdolCount

@kotlinx.serialization.Serializable
class ChekiMonthIdolCountSerializer(
  val idol: IdolSerializer?,
  val chekiCount: Int,
  val chekiShotAtMonth: ChekiShotAtMonthSerializer
) {
  companion object {
    fun from(chekiMonthIdolCount: ChekiMonthIdolCount): ChekiMonthIdolCountSerializer {
      return ChekiMonthIdolCountSerializer(
        chekiMonthIdolCount.idol?.let { IdolSerializer.from(it) },
        chekiMonthIdolCount.chekiCount.value,
        ChekiShotAtMonthSerializer.from(chekiMonthIdolCount.chekiShotAtMonth)
      )
    }
  }
}
