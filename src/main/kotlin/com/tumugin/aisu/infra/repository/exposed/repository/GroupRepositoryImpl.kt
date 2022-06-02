package com.tumugin.aisu.infra.repository.exposed.repository

import com.tumugin.aisu.domain.group.*
import com.tumugin.aisu.domain.user.UserId
import com.tumugin.aisu.infra.repository.exposed.models.User as UserModel
import org.jetbrains.exposed.sql.transactions.transaction
import com.tumugin.aisu.infra.repository.exposed.models.Group as GroupModel

class GroupRepositoryImpl : GroupRepository {
  override suspend fun getGroup(groupId: GroupId): Group? {
    return transaction {
      GroupModel.findById(groupId.value)?.toDomain()
    }
  }

  override suspend fun addGroup(userId: UserId?, groupName: GroupName, groupStatus: GroupStatus): Group {
    return transaction {
      GroupModel.new {
        this.user = userId?.value?.let { UserModel[it] }
        this.name = groupName.value
        this.status = groupStatus.name
      }.toDomain()
    }
  }

  override suspend fun updateGroup(
    groupId: GroupId, userId: UserId?, groupName: GroupName, groupStatus: GroupStatus
  ): Group {
    return transaction {
      val model = GroupModel[groupId.value]
      model.apply {
        this.user = userId?.value?.let { UserModel[it] }
        this.name = groupName.value
        this.status = groupStatus.name
      }.toDomain()
    }
  }

  override suspend fun deleteGroup(groupId: GroupId) {
    transaction {
      val model = GroupModel[groupId.value]
      model.delete()
    }
  }
}
