package com.tumugin.aisu.domain.cheki

import com.tumugin.aisu.domain.idol.IdolId
import com.tumugin.aisu.domain.regulation.RegulationId
import com.tumugin.aisu.domain.user.UserId
import kotlinx.datetime.TimeZone

interface ChekiRepository {
  suspend fun getCheki(chekiId: ChekiId): Cheki?

  suspend fun getChekiByUserIdAndShotDateTimeRange(
    userId: UserId, chekiShotAtStart: ChekiShotAt, chekiShotEnd: ChekiShotAt
  ): List<Cheki>

  suspend fun getChekiByUserIdAndShotDateTimeRangeAndIdolId(
    userId: UserId, idolId: IdolId, chekiShotAtStart: ChekiShotAt, chekiShotEnd: ChekiShotAt
  ): List<Cheki>

  suspend fun getChekiIdolCountByUserId(
    userId: UserId, chekiShotAtStart: ChekiShotAt, chekiShotEnd: ChekiShotAt
  ): List<ChekiIdolCount>

  suspend fun getChekiMonthIdolCountByUserIdAndIdol(
    userId: UserId, idolId: IdolId, baseTimezone: TimeZone
  ): List<ChekiMonthIdolCount>

  suspend fun addCheki(
    userId: UserId,
    idolId: IdolId,
    regulationId: RegulationId?,
    chekiQuantity: ChekiQuantity,
    chekiShotAt: ChekiShotAt,
  ): Cheki

  suspend fun updateCheki(
    chekiId: ChekiId,
    userId: UserId,
    idolId: IdolId,
    regulationId: RegulationId?,
    chekiQuantity: ChekiQuantity,
    chekiShotAt: ChekiShotAt,
  ): Cheki

  suspend fun deleteCheki(
    chekiId: ChekiId
  )
}
