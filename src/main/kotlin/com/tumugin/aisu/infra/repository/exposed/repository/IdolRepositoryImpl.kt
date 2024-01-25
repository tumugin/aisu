package com.tumugin.aisu.infra.repository.exposed.repository

import com.tumugin.aisu.domain.base.PaginatorParam
import com.tumugin.aisu.domain.base.PaginatorResult
import com.tumugin.aisu.domain.group.Group
import com.tumugin.aisu.domain.group.GroupId
import com.tumugin.aisu.domain.idol.*
import com.tumugin.aisu.domain.user.UserId
import com.tumugin.aisu.infra.repository.exposed.models.Idols
import org.jetbrains.exposed.dao.with
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.inList
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import com.tumugin.aisu.infra.repository.exposed.models.User as UserModel
import com.tumugin.aisu.infra.repository.exposed.models.Group as GroupModel
import com.tumugin.aisu.infra.repository.exposed.models.Idol as IdolModel

class IdolRepositoryImpl : IdolRepository {
  private val withModels = listOf(IdolModel::user).toTypedArray()

  override suspend fun getIdol(idolId: IdolId): Idol? {
    return newSuspendedTransaction {
      IdolModel.findById(idolId.value)?.toDomain()
    }
  }

  override suspend fun getIdolsByIds(idolIds: List<IdolId>): List<Idol> {
    return newSuspendedTransaction {
      IdolModel.find { Idols.id inList idolIds.map { it.value } }.with(*withModels).map { it.toDomain() }
    }
  }

  override suspend fun addIdol(userId: UserId, idolName: IdolName, idolStatus: IdolStatus): Idol {
    return newSuspendedTransaction {
      IdolModel.new {
        this.user = UserModel[userId.value]
        this.name = idolName.value
        this.status = idolStatus.name
      }.toDomain()
    }
  }

  override suspend fun updateIdol(
    idolId: IdolId, userId: UserId, idolName: IdolName, idolStatus: IdolStatus
  ): Idol {
    return newSuspendedTransaction {
      val model = IdolModel[idolId.value]
      model.user = UserModel[userId.value]
      model.name = idolName.value
      model.status = idolStatus.name
      model.toDomain()
    }
  }

  override suspend fun deleteIdol(idolId: IdolId) {
    newSuspendedTransaction {
      val model = IdolModel[idolId.value]
      model.delete()
    }
  }

  override suspend fun getAllIdolsByStatues(
    paginatorParam: PaginatorParam, statues: List<IdolStatus>, userId: UserId?
  ): PaginatorResult<Idol> {
    return newSuspendedTransaction {
      val query = IdolModel.find(Idols.status.inList(statues.map { it.name }).let {
        if (userId != null) {
          it.and(Idols.user.eq(userId.value))
        } else {
          it
        }
      })
      val results =
        query.limit(paginatorParam.limit.toInt(), paginatorParam.offset).with(*withModels).map { it.toDomain() }
      paginatorParam.createPaginatorResult(
        query.count(), results
      )
    }
  }

  override suspend fun getGroupsOfIdol(idolId: IdolId): List<Group> {
    return newSuspendedTransaction {
      val idol = IdolModel[idolId.value]
      idol.groups.with(GroupModel::user).map { it.toDomain() }
    }
  }

  override suspend fun getGroupIdsOfIdols(idolIds: List<IdolId>): Map<IdolId, List<GroupId>> {
    return newSuspendedTransaction {
      IdolModel.find { Idols.id inList idolIds.map { it.value } }.with(IdolModel::groups).associate {
        IdolId(it.id.value) to it.groups.map { group -> GroupId(group.id.value) }.toList()
      }
    }
  }
}
