package com.tumugin.aisu.infra.repository.exposed.repository

import com.tumugin.aisu.domain.cheki.*
import com.tumugin.aisu.domain.idol.IdolId
import com.tumugin.aisu.domain.regulation.RegulationId
import com.tumugin.aisu.domain.user.UserId
import kotlinx.datetime.TimeZone

class ChekiRepositoryImpl : ChekiRepository {
  override suspend fun getCheki(chekiId: ChekiId): Cheki? {
    TODO("Not yet implemented")
  }

  override suspend fun getChekiByUserIdAndShotDateTimeRange(
    userId: UserId,
    chekiShotAtStart: ChekiShotAt,
    chekiShotEnd: ChekiShotAt
  ): List<Cheki> {
    TODO("Not yet implemented")
  }

  override suspend fun getChekiByUserIdAndShotDateTimeRangeAndIdolId(
    userId: UserId,
    idolId: IdolId,
    chekiShotAtStart: ChekiShotAt,
    chekiShotEnd: ChekiShotAt
  ): List<Cheki> {
    TODO("Not yet implemented")
  }

  override suspend fun getChekiIdolCountByUserId(
    userId: UserId,
    chekiShotAtStart: ChekiShotAt,
    chekiShotEnd: ChekiShotAt
  ): List<ChekiIdolCount> {
    TODO("Not yet implemented")
  }

  override suspend fun getChekiMonthIdolCountByUserIdAndIdol(
    userId: UserId,
    idolId: IdolId,
    baseTimezone: TimeZone
  ): List<ChekiMonthIdolCount> {
    TODO("Not yet implemented")
  }

  override suspend fun addCheki(
    userId: UserId,
    idolId: IdolId,
    regulationId: RegulationId?,
    chekiQuantity: ChekiQuantity,
    chekiShotAt: ChekiShotAt
  ): Cheki {
    TODO("Not yet implemented")
  }

  override suspend fun updateCheki(
    chekiId: ChekiId,
    userId: UserId,
    idolId: IdolId,
    regulationId: RegulationId?,
    chekiQuantity: ChekiQuantity,
    chekiShotAt: ChekiShotAt
  ): Cheki {
    TODO("Not yet implemented")
  }

  override suspend fun deleteCheki(chekiId: ChekiId) {
    TODO("Not yet implemented")
  }
}