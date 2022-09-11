package com.tumugin.aisu.usecase.client.regulation

import com.tumugin.aisu.domain.exception.HasNoPermissionException
import com.tumugin.aisu.domain.exception.InvalidContextException
import com.tumugin.aisu.domain.exception.NotFoundException
import com.tumugin.aisu.domain.group.GroupId
import com.tumugin.aisu.domain.regulation.*
import com.tumugin.aisu.domain.user.UserId
import com.tumugin.aisu.usecase.client.group.GetGroup
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class WriteRegulation : KoinComponent {
  private val getRegulation = GetRegulation()
  private val getGroup = GetGroup()
  private val regulationRepository by inject<RegulationRepository>()

  suspend fun deleteRegulation(sessionUserId: UserId?, regulationId: RegulationId) {
    val regulation = getRegulation.getRegulation(sessionUserId, regulationId) ?: throw NotFoundException()
    if (!regulation.isEditableByUser(sessionUserId)) {
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
    // 自分が管理出来ないグループには勝手にレギュレーションを追加出来ない
    val targetGroup = getGroup.getGroup(sessionUserId, groupId) ?: throw InvalidContextException()
    if (!targetGroup.isEditableByUser(sessionUserId)) {
      throw HasNoPermissionException()
    }

    return regulationRepository.addRegulation(
      groupId, sessionUserId, regulationName, regulationComment, regulationUnitPrice, regulationStatus
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
    if (!regulation.isEditableByUser(sessionUserId)) {
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
