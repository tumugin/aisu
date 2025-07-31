@file:OptIn(kotlin.time.ExperimentalTime::class)
package com.tumugin.aisu.testing.seeder

import com.tumugin.aisu.domain.cheki.Cheki
import com.tumugin.aisu.domain.cheki.ChekiQuantity
import com.tumugin.aisu.domain.cheki.ChekiRepository
import com.tumugin.aisu.domain.cheki.ChekiShotAt
import com.tumugin.aisu.domain.idol.IdolId
import com.tumugin.aisu.domain.regulation.RegulationId
import com.tumugin.aisu.domain.user.UserId
import kotlin.time.ExperimentalTime
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ChekiSeeder : KoinComponent {
  private val chekiRepository by inject<ChekiRepository>()

  suspend fun seedCheki(
    userId: UserId,
    idolId: IdolId,
    regulationId: RegulationId?,
    chekiQuantity: ChekiQuantity = ChekiQuantity(1),
    chekiShotAt: ChekiShotAt = ChekiShotAt(kotlin.time.Instant.parse("2021-12-01T00:10:30+09:00"))
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

  suspend fun seedChekiWithAttachments(userId: UserId): Cheki {
    val idol = IdolSeeder().seedIdol(userId)
    val group = GroupSeeder().seedGroup(userId)
    GroupIdolSeeder().seedGroupIdol(group.groupId, idol.idolId)
    val regulation = RegulationSeeder().seedRegulation(group.groupId, userId)
    return this.seedCheki(
      userId,
      idol.idolId,
      regulation.regulationId
    )
  }
}
