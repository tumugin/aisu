package com.tumugin.aisu.usecase.client.regulation

import com.tumugin.aisu.domain.exception.HasNoPermissionException
import com.tumugin.aisu.domain.exception.NotFoundException
import com.tumugin.aisu.domain.group.GroupId
import com.tumugin.aisu.domain.regulation.*
import com.tumugin.aisu.domain.user.UserId
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class WriteRegulation : KoinComponent {
  private val getRegulation = GetRegulation()
  private val regulationRepository by inject<RegulationRepository>()

  suspend fun deleteRegulation(sessionUserId: UserId?, regulationId: RegulationId) {
    val regulation = getRegulation.getRegulation(sessionUserId, regulationId) ?: throw NotFoundException()
    if (!regulation.group.isEditableByUser(sessionUserId)) {
      throw HasNoPermissionException()
    }
    regulationRepository.deleteRegulation(regulationId)
  }

  suspend fun addRegulation(
    groupId: GroupId,
    sessionUserId: UserId,
    regulationName: RegulationName,
    regulationComment: RegulationComment,
    regulationUnitPrice: RegulationUnitPrice,
    regulationStatus: RegulationStatus,
  ): Regulation {
    return regulationRepository.addRegulation(
      groupId,
      sessionUserId,
      regulationName,
      regulationComment,
      regulationUnitPrice,
      regulationStatus
    )
  }

  suspend fun updateRegulation(
    regulationId: RegulationId,
    sessionUserId: UserId,
    regulationName: RegulationName,
    regulationComment: RegulationComment,
    regulationUnitPrice: RegulationUnitPrice,
    regulationStatus: RegulationStatus,
  ): Regulation {
    val regulation = getRegulation.getRegulation(sessionUserId, regulationId) ?: throw NotFoundException()
    if (!regulation.group.isEditableByUser(sessionUserId)) {
      throw HasNoPermissionException()
    }
    return regulationRepository.updateRegulation(
      regulationId,
      regulation.group.groupId,
      sessionUserId,
      regulationName,
      regulationComment,
      regulationUnitPrice,
      regulationStatus
    )
  }
}
