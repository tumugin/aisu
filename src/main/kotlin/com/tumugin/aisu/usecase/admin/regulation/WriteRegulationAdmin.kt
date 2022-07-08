package com.tumugin.aisu.usecase.admin.regulation

import com.tumugin.aisu.domain.exception.HasNoPermissionException
import com.tumugin.aisu.domain.exception.NotFoundException
import com.tumugin.aisu.domain.group.GroupId
import com.tumugin.aisu.domain.regulation.*
import com.tumugin.aisu.domain.user.UserId
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class WriteRegulationAdmin : KoinComponent {
  private val getRegulation = GetRegulationAdmin()
  private val regulationRepository by inject<RegulationRepository>()

  suspend fun deleteRegulation(regulationId: RegulationId) {
    val regulation = getRegulation.getRegulation(regulationId) ?: throw NotFoundException()
    if (!regulation.isEditableByAdmin()) {
      throw HasNoPermissionException()
    }
    regulationRepository.deleteRegulation(regulationId)
  }

  suspend fun addRegulation(
    groupId: GroupId,
    clientUserId: UserId,
    regulationName: RegulationName,
    regulationComment: RegulationComment,
    regulationUnitPrice: RegulationUnitPrice,
    regulationStatus: RegulationStatus,
  ): Regulation {
    return regulationRepository.addRegulation(
      groupId,
      clientUserId,
      regulationName,
      regulationComment,
      regulationUnitPrice,
      regulationStatus
    )
  }

  suspend fun updateRegulation(
    regulationId: RegulationId,
    clientUserId: UserId,
    regulationName: RegulationName,
    regulationComment: RegulationComment,
    regulationUnitPrice: RegulationUnitPrice,
    regulationStatus: RegulationStatus,
  ): Regulation {
    val regulation = getRegulation.getRegulation(regulationId) ?: throw NotFoundException()
    if (!regulation.isEditableByAdmin()) {
      throw HasNoPermissionException()
    }
    return regulationRepository.updateRegulation(
      regulationId,
      regulation.group.groupId,
      clientUserId,
      regulationName,
      regulationComment,
      regulationUnitPrice,
      regulationStatus
    )
  }
}
