package com.tumugin.aisu.infra.repository.exposed.repository

import com.tumugin.aisu.domain.base.PaginatorParam
import com.tumugin.aisu.domain.base.PaginatorResult
import com.tumugin.aisu.domain.group.GroupId
import com.tumugin.aisu.domain.idol.*
import com.tumugin.aisu.domain.user.UserId
import com.tumugin.aisu.infra.repository.exposed.models.Idols
import org.jetbrains.exposed.dao.with
import org.jetbrains.exposed.sql.SqlExpressionBuilder.inList
import com.tumugin.aisu.infra.repository.exposed.models.User as UserModel
import com.tumugin.aisu.infra.repository.exposed.models.Group as GroupModel
import org.jetbrains.exposed.sql.transactions.transaction
import com.tumugin.aisu.infra.repository.exposed.models.Idol as IdolModel

class IdolRepositoryImpl : IdolRepository {
  private val withModels = listOf(IdolModel::group, IdolModel::user).toTypedArray()

  override suspend fun getIdol(idolId: IdolId): Idol? {
    return transaction {
      IdolModel.findById(idolId.value)?.toDomain()
    }
  }

  override suspend fun addIdol(groupId: GroupId, userId: UserId, idolName: IdolName, idolStatus: IdolStatus): Idol {
    return transaction {
      IdolModel.new {
        this.group = GroupModel[groupId.value]
        this.user = UserModel[userId.value]
        this.name = idolName.value
        this.status = idolStatus.name
      }.toDomain()
    }
  }

  override suspend fun updateIdol(
    idolId: IdolId, groupId: GroupId, userId: UserId, idolName: IdolName, idolStatus: IdolStatus
  ): Idol {
    return transaction {
      val model = IdolModel[idolId.value]
      model.group = GroupModel[groupId.value]
      model.user = UserModel[userId.value]
      model.name = idolName.value
      model.status = idolStatus.name
      model.toDomain()
    }
  }

  override suspend fun deleteIdol(idolId: IdolId) {
    transaction {
      val model = IdolModel[idolId.value]
      model.delete()
    }
  }

  override suspend fun getAllIdolsByStatues(
    paginatorParam: PaginatorParam, statues: List<IdolStatus>
  ): PaginatorResult<Idol> {
    return transaction {
      val query = IdolModel.find(Idols.status.inList(statues.map { it.name }))
      val results = query.limit(paginatorParam.limit.toInt(), paginatorParam.offset)
        .with(*withModels)
        .map { it.toDomain() }
      paginatorParam.createPaginatorResult(
        query.count(), results
      )
    }
  }
}
