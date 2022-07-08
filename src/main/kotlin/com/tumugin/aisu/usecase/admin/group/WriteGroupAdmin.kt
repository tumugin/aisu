package com.tumugin.aisu.usecase.admin.group

import com.tumugin.aisu.domain.exception.HasNoPermissionException
import com.tumugin.aisu.domain.group.*
import com.tumugin.aisu.domain.idol.Idol
import com.tumugin.aisu.domain.user.UserId
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class WriteGroupAdmin : KoinComponent {
  private val groupRepository by inject<GroupRepository>()

  suspend fun addGroup(
    sessionUserId: UserId?,
    groupName: GroupName,
    groupStatus: GroupStatus,
  ): Group {
    return groupRepository.addGroup(
      sessionUserId, groupName, groupStatus
    )
  }

  suspend fun deleteGroup(sessionUserId: UserId?, group: Group) {
    if (!group.isEditableByUser(sessionUserId)) {
      throw HasNoPermissionException()
    }
    groupRepository.deleteGroup(group.groupId)
  }

  suspend fun updateGroup(
    sessionUserId: UserId?,
    group: Group,
    groupName: GroupName,
    groupStatus: GroupStatus,
  ): Group {
    if (!group.isEditableByUser(sessionUserId)) {
      throw HasNoPermissionException()
    }
    return groupRepository.updateGroup(
      group.groupId, group.userId, groupName, groupStatus
    )
  }

  suspend fun addIdolToGroup(
    sessionUserId: UserId?, group: Group, idol: Idol
  ) {
    if (!(group.isEditableByUser(sessionUserId) && idol.isEditableByUser(sessionUserId))) {
      throw HasNoPermissionException()
    }
    return groupRepository.addIdolToGroup(
      group.groupId, idol.idolId
    )
  }

  suspend fun removeIdolFromGroup(
    sessionUserId: UserId?, group: Group, idol: Idol
  ) {
    if (!(group.isEditableByUser(sessionUserId) && idol.isEditableByUser(sessionUserId))) {
      throw HasNoPermissionException()
    }
    groupRepository.removeIdolFromGroup(
      group.groupId, idol.idolId
    )
  }
}
