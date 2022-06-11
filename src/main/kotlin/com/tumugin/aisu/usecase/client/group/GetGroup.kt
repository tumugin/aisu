package com.tumugin.aisu.usecase.client.group

import com.tumugin.aisu.domain.group.Group
import com.tumugin.aisu.domain.group.GroupId
import com.tumugin.aisu.domain.group.GroupRepository
import com.tumugin.aisu.domain.group.GroupStatus
import com.tumugin.aisu.domain.user.UserId
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GetGroup : KoinComponent {
  private val groupRepository by inject<GroupRepository>()
  private val sessionUserOwnGroupsVisibleStatuses = listOf(
    GroupStatus.PRIVATE_ACTIVE,
    GroupStatus.PRIVATE_NOT_ACTIVE,
    GroupStatus.PUBLIC_ACTIVE,
    GroupStatus.PUBLIC_NOT_ACTIVE,
  )
  private val publicUserOwnGroupsVisibleStatuses = listOf(
    GroupStatus.PUBLIC_ACTIVE,
    GroupStatus.PUBLIC_NOT_ACTIVE,
  )

  suspend fun getGroup(sessionUserId: UserId, groupId: GroupId): Group? {
    val group = groupRepository.getGroup(groupId) ?: return null

    // 自分自身が持っているグループの場合
    if (group.user?.userId == sessionUserId && sessionUserOwnGroupsVisibleStatuses.contains(group.groupStatus)) {
      return group
    }

    // 他人が作成したグループの場合
    if (publicUserOwnGroupsVisibleStatuses.contains(group.groupStatus)) {
      return group
    }

    return null
  }
}
