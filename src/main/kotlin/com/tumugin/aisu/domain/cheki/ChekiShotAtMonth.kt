package com.tumugin.aisu.domain.cheki

import kotlinx.datetime.TimeZone

data class ChekiShotAtMonth(val year: Int, val month: Int, val baseTimezone: TimeZone) {
  companion object {
    fun fromString(yearMonthString: String, baseTimezone: TimeZone): ChekiShotAtMonth {
      TODO("Not yet implemented")
    }
  }
}
