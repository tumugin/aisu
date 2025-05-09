package com.tumugin.aisu.infra.repository.exposed.repository

import com.tumugin.aisu.domain.base.PaginatorParam
import com.tumugin.aisu.domain.base.PaginatorResult
import com.tumugin.aisu.domain.group.*
import com.tumugin.aisu.domain.idol.Idol
import com.tumugin.aisu.domain.idol.IdolId
import com.tumugin.aisu.domain.user.UserId
import com.tumugin.aisu.infra.repository.exposed.models.GroupIdols
import com.tumugin.aisu.infra.repository.exposed.models.GroupIdol as GroupIdolModel
import com.tumugin.aisu.infra.repository.exposed.models.Groups
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.dao.with
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.inList
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import com.tumugin.aisu.infra.repository.exposed.models.User as UserModel
import org.jetbrains.exposed.sql.transactions.transaction
import com.tumugin.aisu.infra.repository.exposed.models.Group as GroupModel
import com.tumugin.aisu.infra.repository.exposed.models.Idol as IdolModel

class GroupRepositoryImpl : GroupRepository {
  private val withModels = listOf(GroupModel::user).toTypedArray()

  override suspend fun getGroup(groupId: GroupId): Group? {
    return newSuspendedTransaction(Dispatchers.IO) {
      GroupModel.findById(groupId.value)?.toDomain()
    }
  }

  override suspend fun getGroupsByIds(groupIds: List<GroupId>): List<Group> {
    return newSuspendedTransaction(Dispatchers.IO) {
      GroupModel.find { Groups.id inList groupIds.map { it.value } }
        .map { it.toDomain() }
    }
  }

  override suspend fun addGroup(userId: UserId?, groupName: GroupName, groupStatus: GroupStatus): Group {
    return newSuspendedTransaction(Dispatchers.IO) {
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
    return newSuspendedTransaction(Dispatchers.IO) {
      val model = GroupModel[groupId.value]
      model.apply {
        this.user = userId?.value?.let { UserModel[it] }
        this.name = groupName.value
        this.status = groupStatus.name
      }.toDomain()
    }
  }

  override suspend fun deleteGroup(groupId: GroupId) {
    newSuspendedTransaction(Dispatchers.IO) {
      val model = GroupModel[groupId.value]
      model.delete()
    }
  }

  override suspend fun getAllGroupsByStatuses(
    paginatorParam: PaginatorParam, statues: List<GroupStatus>, userId: UserId?
  ): PaginatorResult<Group> {
    return newSuspendedTransaction(Dispatchers.IO) {
      val query = GroupModel.find(Groups.status.inList(statues.map { it.name }).let {
        if (userId != null) {
          it.and(Groups.user.eq(userId.value))
        } else {
          it
        }
      })
      val results =
        query.limit(paginatorParam.limit.toInt())
          .offset(paginatorParam.offset)
          .with(*withModels)
          .map { it.toDomain() }
      val count = query.count()
      paginatorParam.createPaginatorResult(
        count, results
      )
    }
  }

  override suspend fun getIdolsOfGroup(groupId: GroupId): List<Idol> {
    return newSuspendedTransaction(Dispatchers.IO) {
      GroupModel[groupId.value].idols.with(IdolModel::user).map { it.toDomain() }
    }
  }

  override suspend fun getIdolIdsOfGroups(groupIds: List<GroupId>): Map<GroupId, List<IdolId>> {
    return newSuspendedTransaction(Dispatchers.IO) {
      GroupIdolModel.find { GroupIdols.group inList groupIds.map { it.value } }
        .groupBy { GroupId(it.group.id.value) }
        .mapValues { it.value.map { IdolId(it.idol.id.value) } }
    }
  }

  override suspend fun addIdolToGroup(groupId: GroupId, idolId: IdolId) {
    return newSuspendedTransaction(Dispatchers.IO) {
      val idolModel = IdolModel[idolId.value]
      val groupModel = GroupModel[groupId.value]
      GroupIdolModel.new {
        idol = idolModel
        group = groupModel
      }
    }
  }

  override suspend fun removeIdolFromGroup(groupId: GroupId, idolId: IdolId) {
    newSuspendedTransaction(Dispatchers.IO) {
      val idolModel = IdolModel[idolId.value]
      val groupModel = GroupModel[groupId.value]
      GroupIdolModel
        .find(GroupIdols.idol.eq(idolModel.id) and GroupIdols.group.eq(groupModel.id))
        .first()
        .delete()
    }
  }
}
