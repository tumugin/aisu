package com.tumugin.aisu.infra.repository.exposed.repository

import com.tumugin.aisu.domain.group.*
import com.tumugin.aisu.domain.user.UserId
import org.jetbrains.exposed.sql.transactions.transaction
import com.tumugin.aisu.infra.repository.exposed.models.Group as GroupModel

class GroupRepositoryImpl : GroupRepository {
  override suspend fun getGroup(groupId: GroupId): Group? {
    return transaction {
      GroupModel.findById(groupId.value)
    }?.let { toDomain(it) }
  }

  override suspend fun addGroup(userId: UserId?, groupName: GroupName, groupStatus: GroupStatus): Group {
    return toDomain(transaction {
      GroupModel.new {
        this.userId = userId?.value
        this.name = groupName.value
        this.status = groupStatus.name
      }
    })
  }

  override suspend fun updateGroup(
    groupId: GroupId, userId: UserId?, groupName: GroupName, groupStatus: GroupStatus
  ): Group {
    return toDomain(transaction {
      val model = GroupModel[groupId.value]
      model.apply {
        this.userId = userId?.value
        this.name = groupName.value
        this.status = groupStatus.name
      }
    })
  }

  override suspend fun deleteGroup(groupId: GroupId) {
    transaction {
      val model = GroupModel[groupId.value]
      model.delete()
    }
  }

  companion object {
    fun toDomain(model: GroupModel): Group {
      return Group(
        groupId = GroupId(model.id.value),
        userId = model.userId?.let { UserId(it) },
        groupName = GroupName(model.name),
        groupStatus = GroupStatus.valueOf(model.status),
        groupCreatedAt = GroupCreatedAt(model.createdAt),
        groupUpdatedAt = GroupUpdatedAt(model.updatedAt)
      )
    }
  }
}
