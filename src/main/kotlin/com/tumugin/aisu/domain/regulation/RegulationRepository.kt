package com.tumugin.aisu.domain.regulation

import com.tumugin.aisu.domain.group.GroupId
import com.tumugin.aisu.domain.user.UserId

interface RegulationRepository {
  suspend fun getRegulation(regulationId: RegulationId): Regulation?

  suspend fun deleteRegulation(regulationId: RegulationId)

  suspend fun addRegulation(
    groupId: GroupId,
    userId: UserId?,
    regulationName: RegulationName,
    regulationComment: RegulationComment,
    regulationUnitPrice: RegulationUnitPrice,
    regulationStatus: RegulationStatus,
  ): Regulation

  suspend fun updateRegulation(
    regulationId: RegulationId,
    groupId: GroupId,
    userId: UserId,
    regulationName: RegulationName,
    regulationComment: RegulationComment,
    regulationUnitPrice: RegulationUnitPrice,
    regulationStatus: RegulationStatus,
  )
}
