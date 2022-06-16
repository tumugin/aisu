package com.tumugin.aisu.testing.seeder

import com.tumugin.aisu.domain.cheki.Cheki
import com.tumugin.aisu.domain.cheki.ChekiQuantity
import com.tumugin.aisu.domain.cheki.ChekiRepository
import com.tumugin.aisu.domain.cheki.ChekiShotAt
import com.tumugin.aisu.domain.idol.IdolId
import com.tumugin.aisu.domain.regulation.RegulationId
import com.tumugin.aisu.domain.user.UserId
import kotlinx.datetime.Instant
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ChekiSeeder : KoinComponent {
  private val chekiRepository by inject<ChekiRepository>()

  suspend fun seedCheki(
    userId: UserId,
    idolId: IdolId,
    regulationId: RegulationId?,
    chekiQuantity: ChekiQuantity = ChekiQuantity(1),
    chekiShotAt: ChekiShotAt = ChekiShotAt(Instant.parse("2021-12-01T00:10:30+09:00"))
  ): Cheki {
    return chekiRepository.getCheki(
      chekiRepository.addCheki(
        userId,
        idolId,
        regulationId,
        chekiQuantity,
        chekiShotAt
      ).chekiId
    )!!
  }
}