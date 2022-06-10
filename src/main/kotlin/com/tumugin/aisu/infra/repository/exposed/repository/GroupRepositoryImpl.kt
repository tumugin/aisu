package com.tumugin.aisu.infra.repository.exposed.repository

import com.tumugin.aisu.domain.base.PaginatorParam
import com.tumugin.aisu.domain.base.PaginatorResult
import com.tumugin.aisu.domain.group.*
import com.tumugin.aisu.domain.user.UserId
import com.tumugin.aisu.infra.repository.exposed.models.Groups
import org.jetbrains.exposed.dao.with
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.inList
import org.jetbrains.exposed.sql.and
import com.tumugin.aisu.infra.repository.exposed.models.User as UserModel
import org.jetbrains.exposed.sql.transactions.transaction
import com.tumugin.aisu.infra.repository.exposed.models.Group as GroupModel

class GroupRepositoryImpl : GroupRepository {
  private val withModels = listOf(GroupModel::user).toTypedArray()

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

  override suspend fun getAllGroupsByStatuses(
    paginatorParam: PaginatorParam, statues: List<GroupStatus>, userId: UserId?
  ): PaginatorResult<Group> {
    return transaction {
      val query = GroupModel.find(Groups.status.inList(statues.map { it.name }).let {
        if (userId != null) {
          it.and(Groups.user.eq(userId.value))
        } else {
          it
        }
      })
      val results =
        query.limit(paginatorParam.limit.toInt(), paginatorParam.offset).with(*withModels).map { it.toDomain() }
      val count = query.count()
      paginatorParam.createPaginatorResult(
        count, results
      )
    }
  }
}
