package com.tumugin.aisu.usecase.client.group

import com.tumugin.aisu.domain.base.PaginatorParam
import com.tumugin.aisu.domain.base.PaginatorResult
import com.tumugin.aisu.domain.exception.HasNoPermissionException
import com.tumugin.aisu.domain.group.Group
import com.tumugin.aisu.domain.group.GroupId
import com.tumugin.aisu.domain.group.GroupRepository
import com.tumugin.aisu.domain.group.GroupStatus
import com.tumugin.aisu.domain.idol.Idol
import com.tumugin.aisu.domain.idol.IdolId
import com.tumugin.aisu.domain.user.UserId
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GetGroup : KoinComponent {
  private val groupRepository by inject<GroupRepository>()

  suspend fun getGroup(sessionUserId: UserId?, groupId: GroupId): Group? {
    val group = groupRepository.getGroup(groupId) ?: return null
    if (group.isVisibleToUser(sessionUserId)) {
      return group
    }
    throw HasNoPermissionException()
  }

  suspend fun getGroupsById(sessionUserId: UserId?, groupIds: List<GroupId>): List<Group> {
    return groupRepository.getGroupsByIds(groupIds).filter { it.isVisibleToUser(sessionUserId) }
  }

  suspend fun getIdolsOfGroup(sessionUserId: UserId, group: Group): List<Idol> {
    val idols = groupRepository.getIdolsOfGroup(group.groupId)
    return idols.filter { it.isVisibleToUser(sessionUserId) }
  }

  suspend fun getIdolIdsOfGroups(sessionUserId: UserId?, groupIds: List<GroupId>): Map<GroupId, List<IdolId>> {
    return groupRepository.getIdolIdsOfGroups(groupIds)
  }

  suspend fun getAllUserCreatedGroups(sessionUserId: UserId, paginatorParam: PaginatorParam): PaginatorResult<Group> {
    return groupRepository.getAllGroupsByStatuses(paginatorParam, GroupStatus.values().toList(), sessionUserId)
  }
}
