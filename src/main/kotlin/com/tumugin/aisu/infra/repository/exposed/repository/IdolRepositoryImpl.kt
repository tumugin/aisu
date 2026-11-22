package com.tumugin.aisu.infra.repository.exposed.repository

import com.tumugin.aisu.domain.base.PaginatorParam
import com.tumugin.aisu.domain.base.PaginatorResult
import com.tumugin.aisu.domain.group.Group
import com.tumugin.aisu.domain.group.GroupId
import com.tumugin.aisu.domain.idol.*
import com.tumugin.aisu.domain.user.UserId
import com.tumugin.aisu.infra.repository.exposed.models.Idols
import org.jetbrains.exposed.v1.core.*
import org.jetbrains.exposed.v1.dao.with
import com.tumugin.aisu.infra.repository.exposed.models.User as UserModel
import com.tumugin.aisu.infra.repository.exposed.models.Group as GroupModel
import com.tumugin.aisu.infra.repository.exposed.models.Idol as IdolModel

class IdolRepositoryImpl : IdolRepository {
  private val withModels = listOf(IdolModel::user).toTypedArray()

  override suspend fun getIdol(idolId: IdolId): Idol? {
    return withJdbcTransaction {
      IdolModel.findById(idolId.value)?.toDomain()
    }
  }

  override suspend fun getIdolsByIds(idolIds: List<IdolId>): List<Idol> {
    return withJdbcTransaction {
      IdolModel.find { Idols.id inList idolIds.map { it.value } }.with(*withModels).map { it.toDomain() }
    }
  }

  override suspend fun addIdol(userId: UserId, idolName: IdolName, idolStatus: IdolStatus): Idol {
    return withJdbcTransaction {
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
    return withJdbcTransaction {
      val model = IdolModel[idolId.value]
      model.user = UserModel[userId.value]
      model.name = idolName.value
      model.status = idolStatus.name
      model.toDomain()
    }
  }

  override suspend fun deleteIdol(idolId: IdolId) {
    withJdbcTransaction {
      val model = IdolModel[idolId.value]
      model.delete()
    }
  }

  override suspend fun getAllIdolsByStatues(
    paginatorParam: PaginatorParam, statues: List<IdolStatus>, userId: UserId?
  ): PaginatorResult<Idol> {
    return withJdbcTransaction {
      val query = IdolModel.find(Idols.status.inList(statues.map { it.name }).let {
        if (userId != null) {
          it.and(Idols.user.eq(userId.value))
        } else {
          it
        }
      })
      val results =
        query.limit(paginatorParam.limit.toInt())
          .offset(paginatorParam.offset)
          .with(*withModels)
          .map { it.toDomain() }
      paginatorParam.createPaginatorResult(
        query.count(), results
      )
    }
  }

  override suspend fun getGroupsOfIdol(idolId: IdolId): List<Group> {
    return withJdbcTransaction {
      val idol = IdolModel[idolId.value]
      idol.groups.with(GroupModel::user).map { it.toDomain() }
    }
  }

  override suspend fun getGroupIdsOfIdols(idolIds: List<IdolId>): Map<IdolId, List<GroupId>> {
    return withJdbcTransaction {
      IdolModel.find { Idols.id inList idolIds.map { it.value } }.with(IdolModel::groups).associate {
        IdolId(it.id.value) to it.groups.map { group -> GroupId(group.id.value) }.toList()
      }
    }
  }
}
