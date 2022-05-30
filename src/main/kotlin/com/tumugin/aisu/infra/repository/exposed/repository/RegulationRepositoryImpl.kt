package com.tumugin.aisu.infra.repository.exposed.repository

import com.tumugin.aisu.domain.group.GroupId
import com.tumugin.aisu.domain.regulation.*
import com.tumugin.aisu.domain.user.UserId
import org.jetbrains.exposed.sql.transactions.transaction
import com.tumugin.aisu.infra.repository.exposed.models.Regulation as RegulationModel

class RegulationRepositoryImpl : RegulationRepository {
  override suspend fun getRegulation(regulationId: RegulationId): Regulation? {
    val rawModel = transaction {
      RegulationModel.findById(regulationId.value)
    }
    if (rawModel === null) {
      return null
    }
    return toDomain(rawModel)
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
    val rawModel = transaction {
      RegulationModel.new {
        this.groupId = groupId.value
        this.userId = userId.value
        this.name = regulationName.value
        this.comment = regulationComment.value
        this.unitPrice = regulationUnitPrice.value
        this.status = regulationStatus.name
      }
    }
    return toDomain(rawModel)
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
    val rawModel = transaction {
      val model = RegulationModel[regulationId.value]
      model.apply {
        this.groupId = groupId.value
        this.userId = userId.value
        this.name = regulationName.value
        this.comment = regulationComment.value
        this.unitPrice = regulationUnitPrice.value
        this.status = regulationStatus.name
      }
    }
    return toDomain(rawModel)
  }

  private fun toDomain(model: RegulationModel): Regulation {
    return Regulation(
      regulationId = RegulationId(model.id.value),
      groupId = GroupId(model.groupId),
      userId = model.userId?.let { UserId(it) },
      regulationName = RegulationName(model.name),
      regulationComment = RegulationComment(model.comment),
      regulationUnitPrice = RegulationUnitPrice(model.unitPrice),
      regulationStatus = RegulationStatus.valueOf(model.status),
      regulationCreatedAt = RegulationCreatedAt(model.createdAt),
      regulationUpdatedAt = RegulationUpdatedAt(model.updatedAt)
    )
  }
}
