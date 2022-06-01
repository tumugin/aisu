package com.tumugin.aisu.domain.cheki

import kotlinx.datetime.TimeZone

data class ChekiShotAtMonth(val year: Int, val month: Int, val baseTimezone: TimeZone) {
  companion object {
    fun fromString(yearString: String, monthString: String, baseTimezone: TimeZone): ChekiShotAtMonth {
      return ChekiShotAtMonth(
        year = yearString.toInt(),
        month = monthString.toInt(),
        baseTimezone = baseTimezone
      )
    }
  }
}
