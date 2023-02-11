package com.tumugin.aisu.domain.group

import com.tumugin.aisu.domain.base.PaginatorParam
import com.tumugin.aisu.domain.base.PaginatorResult
import com.tumugin.aisu.domain.idol.Idol
import com.tumugin.aisu.domain.idol.IdolId
import com.tumugin.aisu.domain.user.UserId

interface GroupRepository {
  suspend fun getGroup(groupId: GroupId): Group?

  suspend fun getGroupsByIds(groupIds: List<GroupId>): List<Group>

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

  suspend fun getAllGroupsByStatuses(
    paginatorParam: PaginatorParam,
    statues: List<GroupStatus>,
    userId: UserId?
  ): PaginatorResult<Group>

  suspend fun getIdolsOfGroup(groupId: GroupId): List<Idol>

  suspend fun getIdolIdsOfGroups(groupIds: List<GroupId>): Map<GroupId, List<IdolId>>

  suspend fun addIdolToGroup(groupId: GroupId, idolId: IdolId)

  suspend fun removeIdolFromGroup(groupId: GroupId, idolId: IdolId)
}
