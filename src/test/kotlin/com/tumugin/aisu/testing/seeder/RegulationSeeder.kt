package com.tumugin.aisu.testing.seeder

import com.tumugin.aisu.domain.group.GroupId
import com.tumugin.aisu.domain.regulation.*
import com.tumugin.aisu.domain.user.UserId
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class RegulationSeeder : KoinComponent {
  private val regulationRepository by inject<RegulationRepository>()

  suspend fun seedRegulation(
    groupId: GroupId,
    userId: UserId,
    regulationStatus: RegulationStatus = RegulationStatus.ACTIVE
  ): Regulation {
    return regulationRepository.getRegulation(
      regulationRepository.addRegulation(
        groupId,
        userId,
        RegulationName("コメント付きチェキ券"),
        RegulationComment("メンバー指定チェキ券"),
        RegulationUnitPrice(1500),
        regulationStatus
      ).regulationId
    )!!
  }
}
