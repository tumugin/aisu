package com.tumugin.aisu.usecase.admin.group

import com.tumugin.aisu.domain.exception.HasNoPermissionException
import com.tumugin.aisu.domain.group.Group
import com.tumugin.aisu.domain.group.GroupId
import com.tumugin.aisu.domain.group.GroupRepository
import com.tumugin.aisu.domain.idol.Idol
import com.tumugin.aisu.domain.user.UserId
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GetGroupAdmin : KoinComponent {
  private val groupRepository by inject<GroupRepository>()

  suspend fun getGroup(groupId: GroupId): Group? {
    val group = groupRepository.getGroup(groupId) ?: return null
    if (group.isVisibleToAdmin()) {
      return group
    }
    throw HasNoPermissionException()
  }

  suspend fun getIdolsOfGroup(group: Group): List<Idol> {
    val idols = groupRepository.getIdolsOfGroup(group.groupId)
    return idols.filter { it.isEditableByAdmin() }
  }
}
