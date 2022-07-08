package com.tumugin.aisu.usecase.admin.cheki

import com.tumugin.aisu.domain.cheki.*
import com.tumugin.aisu.domain.exception.HasNoPermissionException
import com.tumugin.aisu.domain.idol.IdolId
import com.tumugin.aisu.domain.user.UserId
import kotlinx.datetime.TimeZone
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GetChekiAdmin : KoinComponent {
  private val chekiRepository by inject<ChekiRepository>()

  suspend fun getCheki(chekiId: ChekiId): Cheki? {
    val cheki = chekiRepository.getCheki(chekiId) ?: return null
    if (cheki.isVisibleToAdmin()) {
      return cheki
    }
    throw HasNoPermissionException()
  }

  suspend fun getChekiByUserIdAndShotDateTimeRange(
    clientUserId: UserId, chekiShotAtStart: ChekiShotAt, chekiShotEnd: ChekiShotAt
  ): List<Cheki> {
    return chekiRepository.getChekiByUserIdAndShotDateTimeRange(clientUserId, chekiShotAtStart, chekiShotEnd)
  }

  suspend fun getChekiByUserIdAndShotDateTimeRangeAndIdolId(
    clientUserId: UserId, idolId: IdolId, chekiShotAtStart: ChekiShotAt, chekiShotEnd: ChekiShotAt
  ): List<Cheki> {
    return chekiRepository.getChekiByUserIdAndShotDateTimeRangeAndIdolId(
      clientUserId, idolId, chekiShotAtStart, chekiShotEnd
    )
  }

  suspend fun getChekiIdolCountByUserId(
    clientUserId: UserId, chekiShotAtStart: ChekiShotAt, chekiShotEnd: ChekiShotAt
  ): List<ChekiIdolCount> {
    return chekiRepository.getChekiIdolCountByUserId(clientUserId, chekiShotAtStart, chekiShotEnd)
  }

  suspend fun getChekiMonthIdolCountByUserIdAndIdol(
    clientUserId: UserId, idolId: IdolId, baseTimezone: TimeZone
  ): List<ChekiMonthIdolCount> {
    return chekiRepository.getChekiMonthIdolCountByUserIdAndIdol(clientUserId, idolId, baseTimezone)
  }
}
