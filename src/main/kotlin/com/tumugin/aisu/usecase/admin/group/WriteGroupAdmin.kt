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
    clientUserId: UserId?,
    groupName: GroupName,
    groupStatus: GroupStatus,
  ): Group {
    return groupRepository.addGroup(
      clientUserId, groupName, groupStatus
    )
  }

  suspend fun deleteGroup(clientUserId: UserId?, group: Group) {
    if (!group.isEditableByAdmin()) {
      throw HasNoPermissionException()
    }
    groupRepository.deleteGroup(group.groupId)
  }

  suspend fun updateGroup(
    group: Group,
    groupName: GroupName,
    groupStatus: GroupStatus,
  ): Group {
    if (!group.isEditableByAdmin()) {
      throw HasNoPermissionException()
    }
    return groupRepository.updateGroup(
      group.groupId, group.userId, groupName, groupStatus
    )
  }

  suspend fun addIdolToGroup(
    group: Group, idol: Idol
  ) {
    if (!(group.isEditableByAdmin() && idol.isEditableByAdmin())) {
      throw HasNoPermissionException()
    }
    return groupRepository.addIdolToGroup(
      group.groupId, idol.idolId
    )
  }

  suspend fun removeIdolFromGroup(
    group: Group, idol: Idol
  ) {
    if (!(group.isEditableByAdmin() && idol.isEditableByAdmin())) {
      throw HasNoPermissionException()
    }
    groupRepository.removeIdolFromGroup(
      group.groupId, idol.idolId
    )
  }
}
