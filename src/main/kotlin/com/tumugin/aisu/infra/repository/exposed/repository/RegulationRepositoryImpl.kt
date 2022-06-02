package com.tumugin.aisu.infra.repository.exposed.repository

import com.tumugin.aisu.domain.group.GroupId
import com.tumugin.aisu.domain.regulation.*
import com.tumugin.aisu.domain.user.UserId
import com.tumugin.aisu.infra.repository.exposed.models.User as UserModel
import com.tumugin.aisu.infra.repository.exposed.models.Group as GroupModel
import org.jetbrains.exposed.sql.transactions.transaction
import com.tumugin.aisu.infra.repository.exposed.models.Regulation as RegulationModel

class RegulationRepositoryImpl : RegulationRepository {
  override suspend fun getRegulation(regulationId: RegulationId): Regulation? {
    return transaction {
      RegulationModel.findById(regulationId.value)?.toDomain()
    }
  }

  override suspend fun deleteRegulation(regulationId: RegulationId) {
    transaction {
      val rawModel = RegulationModel[regulationId.value]
      rawModel.delete()
    }
  }

  override suspend fun addRegulation(
    groupId: GroupId,
    userId: UserId,
    regulationName: RegulationName,
    regulationComment: RegulationComment,
    regulationUnitPrice: RegulationUnitPrice,
    regulationStatus: RegulationStatus
  ): Regulation {
    return transaction {
      RegulationModel.new {
        this.group = GroupModel[groupId.value]
        this.user = UserModel[userId.value]
        this.name = regulationName.value
        this.comment = regulationComment.value
        this.unitPrice = regulationUnitPrice.value
        this.status = regulationStatus.name
      }.toDomain()
    }
  }

  override suspend fun updateRegulation(
    regulationId: RegulationId,
    groupId: GroupId,
    userId: UserId,
    regulationName: RegulationName,
    regulationComment: RegulationComment,
    regulationUnitPrice: RegulationUnitPrice,
    regulationStatus: RegulationStatus
  ): Regulation {
    return transaction {
      val model = RegulationModel[regulationId.value]
      model.apply {
        this.group = GroupModel[groupId.value]
        this.user = UserModel[userId.value]
        this.name = regulationName.value
        this.comment = regulationComment.value
        this.unitPrice = regulationUnitPrice.value
        this.status = regulationStatus.name
      }.toDomain()
    }
  }
}
