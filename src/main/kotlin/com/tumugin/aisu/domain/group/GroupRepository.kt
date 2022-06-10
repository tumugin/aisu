package com.tumugin.aisu.domain.group

import com.tumugin.aisu.domain.base.PaginatorParam
import com.tumugin.aisu.domain.base.PaginatorResult
import com.tumugin.aisu.domain.user.UserId

interface GroupRepository {
  suspend fun getGroup(groupId: GroupId): Group?

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

  suspend fun getAllGroupsByStatuses(paginatorParam: PaginatorParam, statues: List<GroupStatus>): PaginatorResult<Group>
}
