package com.tumugin.aisu.usecase.client.group

import com.tumugin.aisu.domain.exception.HasNoPermissionException
import com.tumugin.aisu.domain.group.Group
import com.tumugin.aisu.domain.group.GroupId
import com.tumugin.aisu.domain.group.GroupRepository
import com.tumugin.aisu.domain.idol.Idol
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

  suspend fun getIdolsOfGroup(sessionUserId: UserId, group: Group): List<Idol> {
    val idols = groupRepository.getIdolsOfGroup(group.groupId)
    return idols.filter { it.isVisibleToUser(sessionUserId) }
  }
}
