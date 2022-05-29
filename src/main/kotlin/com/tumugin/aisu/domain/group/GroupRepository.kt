package com.tumugin.aisu.domain.group

import com.tumugin.aisu.domain.user.UserId

interface GroupRepository {
  suspend fun getGroup(groupId: GroupId)

  suspend fun addGroup(
    userId: UserId?,
    groupName: GroupName,
    groupStatus: GroupStatus,
  ): Group

  suspend fun updateGroup(
    groupId: GroupId,
    userId: UserId?,
    groupName: GroupName,
    groupStatus: GroupStatus,
  ): Group

  suspend fun deleteGroup(groupId: GroupId)
}
