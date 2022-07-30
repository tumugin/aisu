package com.tumugin.aisu.app.serializer.client

import com.tumugin.aisu.domain.cheki.ChekiShotAtMonth
import kotlinx.serialization.Serializable

@Serializable
class ChekiShotAtMonthSerializer(val year: Int, val month: Int, val baseTimezone: String) {
  companion object {
    fun from(chekiShotAtMonth: ChekiShotAtMonth): ChekiShotAtMonthSerializer {
      return ChekiShotAtMonthSerializer(
        chekiShotAtMonth.year,
        chekiShotAtMonth.month,
        chekiShotAtMonth.baseTimezone.toString()
      )
    }
  }
}
