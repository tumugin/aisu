package com.tumugin.aisu.usecase.client.cheki

import com.tumugin.aisu.domain.cheki.*
import com.tumugin.aisu.domain.exception.HasNoPermissionException
import com.tumugin.aisu.domain.idol.IdolId
import com.tumugin.aisu.domain.user.UserId
import kotlinx.datetime.TimeZone
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GetCheki : KoinComponent {
  private val chekiRepository by inject<ChekiRepository>()

  suspend fun getCheki(sessionUserId: UserId, chekiId: ChekiId): Cheki? {
    val cheki = chekiRepository.getCheki(chekiId) ?: return null
    if (cheki.isVisibleToUser(sessionUserId)) {
      return cheki
    }
    throw HasNoPermissionException()
  }

  suspend fun getChekisByIds(sessionUserId: UserId, chekiIds: List<ChekiId>): List<Cheki> {
    return chekiRepository.getChekisByIds(chekiIds)
      .filter { it.isVisibleToUser(sessionUserId) }
  }

  suspend fun getChekiByUserIdAndShotDateTimeRange(
    sessionUserId: UserId, chekiShotAtStart: ChekiShotAt, chekiShotEnd: ChekiShotAt
  ): List<Cheki> {
    return chekiRepository.getChekiByUserIdAndShotDateTimeRange(sessionUserId, chekiShotAtStart, chekiShotEnd)
  }

  suspend fun getChekiByUserIdAndShotDateTimeRangeAndIdolId(
    sessionUserId: UserId, idolId: IdolId, chekiShotAtStart: ChekiShotAt, chekiShotEnd: ChekiShotAt
  ): List<Cheki> {
    return chekiRepository.getChekiByUserIdAndShotDateTimeRangeAndIdolId(
      sessionUserId, idolId, chekiShotAtStart, chekiShotEnd
    )
  }

  suspend fun getChekiIdolCountByUserId(
    sessionUserId: UserId, chekiShotAtStart: ChekiShotAt, chekiShotEnd: ChekiShotAt
  ): List<ChekiIdolCount> {
    return chekiRepository.getChekiIdolCountByUserId(sessionUserId, chekiShotAtStart, chekiShotEnd)
  }

  suspend fun getChekiMonthIdolCountByUserIdAndIdol(
    sessionUserId: UserId, idolId: IdolId, baseTimezone: TimeZone
  ): List<ChekiMonthIdolCount> {
    return chekiRepository.getChekiMonthIdolCountByUserIdAndIdol(sessionUserId, idolId, baseTimezone)
  }
}
