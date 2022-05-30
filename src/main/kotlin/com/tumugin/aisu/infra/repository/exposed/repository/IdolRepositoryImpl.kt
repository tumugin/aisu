package com.tumugin.aisu.infra.repository.exposed.repository

import com.tumugin.aisu.domain.group.GroupId
import com.tumugin.aisu.domain.idol.*
import com.tumugin.aisu.domain.user.UserId
import org.jetbrains.exposed.sql.transactions.transaction
import com.tumugin.aisu.infra.repository.exposed.models.Idol as IdolModel

class IdolRepositoryImpl : IdolRepository {
  override suspend fun getIdol(idolId: IdolId): Idol? {
    return toDomain(transaction {
      IdolModel[idolId.value]
    })
  }

  override suspend fun addIdol(groupId: GroupId, userId: UserId, idolName: IdolName, idolStatus: IdolStatus): Idol {
    return toDomain(transaction {
      IdolModel.new {
        this.groupId = groupId.value
        this.userId = userId.value
        this.name = idolName.value
        this.status = idolStatus.name
      }
    })
  }

  override suspend fun updateIdol(
    idolId: IdolId, groupId: GroupId, userId: UserId, idolName: IdolName, idolStatus: IdolStatus
  ): Idol {
    return toDomain(transaction {
      val model = IdolModel[idolId.value]
      model.groupId = groupId.value
      model.userId = userId.value
      model.name = idolName.value
      model.status = idolStatus.name
      model
    })
  }

  override suspend fun deleteIdol(idolId: IdolId) {
    transaction {
      val model = IdolModel[idolId.value]
      model.delete()
    }
  }

  companion object {
    fun toDomain(model: IdolModel): Idol {
      return Idol(
        idolId = IdolId(model.id.value),
        groupId = model.groupId?.let { GroupId(it) },
        group = model.group?.let { GroupRepositoryImpl.toDomain(it) },
        userId = model.userId?.let { UserId(it) },
        user = model.user?.let { UserRepositoryImpl.toDomain(it) },
        idolName = IdolName(model.name),
        idolStatus = IdolStatus.valueOf(model.status),
        idolCreatedAt = IdolCreatedAt(model.createdAt),
        idolUpdatedAt = IdolUpdatedAt(model.updatedAt)
      )
    }
  }
}
