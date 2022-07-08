package com.tumugin.aisu.usecase.admin.regulation

import com.tumugin.aisu.domain.exception.HasNoPermissionException
import com.tumugin.aisu.domain.exception.NotFoundException
import com.tumugin.aisu.domain.group.GroupId
import com.tumugin.aisu.domain.regulation.Regulation
import com.tumugin.aisu.domain.regulation.RegulationId
import com.tumugin.aisu.domain.regulation.RegulationRepository
import com.tumugin.aisu.domain.regulation.RegulationStatus
import com.tumugin.aisu.domain.user.UserId
import com.tumugin.aisu.usecase.admin.group.GetGroupAdmin
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GetRegulationAdmin : KoinComponent {
  private val getGroup = GetGroupAdmin()
  private val regulationRepository by inject<RegulationRepository>()

  suspend fun getRegulation(sessionUserId: UserId?, regulationId: RegulationId): Regulation? {
    val regulation = regulationRepository.getRegulation(regulationId) ?: return null
    if (!regulation.isVisibleToUser(sessionUserId)) {
      throw HasNoPermissionException()
    }
    return regulation
  }

  suspend fun getRegulationsByGroupId(
    sessionUserId: UserId?,
    groupId: GroupId,
    regulationStatues: List<RegulationStatus>
  ): List<Regulation> {
    val group = getGroup.getGroup(sessionUserId, groupId) ?: throw NotFoundException()

    if (!group.isVisibleToUser(sessionUserId)) {
      throw HasNoPermissionException()
    }

    // 自分自身が作成したものの場合は削除ステータスの場合でも返す
    if (sessionUserId == group.userId) {
      return regulationRepository.getRegulationsByGroupId(groupId, regulationStatues)
    }

    return regulationRepository.getRegulationsByGroupId(
      groupId,
      regulationStatues.filter { it != RegulationStatus.OPERATION_DELETED }
    )
  }
}
