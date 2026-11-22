package com.tumugin.aisu.infra.repository.exposed.repository

import com.tumugin.aisu.domain.group.GroupId
import com.tumugin.aisu.domain.regulation.*
import com.tumugin.aisu.domain.user.UserId
import com.tumugin.aisu.infra.repository.exposed.models.Regulations
import org.jetbrains.exposed.v1.core.*
import org.jetbrains.exposed.v1.dao.with
import com.tumugin.aisu.infra.repository.exposed.models.User as UserModel
import com.tumugin.aisu.infra.repository.exposed.models.Group as GroupModel
import com.tumugin.aisu.infra.repository.exposed.models.Regulation as RegulationModel

class RegulationRepositoryImpl : RegulationRepository {
  private val withModel = listOf(RegulationModel::user, RegulationModel::group).toTypedArray()

  override suspend fun getRegulation(regulationId: RegulationId): Regulation? {
    return withJdbcTransaction {
      RegulationModel.findById(regulationId.value)?.toDomain()
    }
  }

  override suspend fun getRegulationsByIds(regulationIds: List<RegulationId>): List<Regulation> {
    return withJdbcTransaction {
      RegulationModel.find { Regulations.id.inList(regulationIds.map { it.value }) }.with(*withModel)
        .map { it.toDomain() }
    }
  }

  override suspend fun deleteRegulation(regulationId: RegulationId) {
    withJdbcTransaction {
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
    return withJdbcTransaction {
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
    return withJdbcTransaction {
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

  override suspend fun getRegulationsByGroupId(
    groupId: GroupId, regulationStatues: List<RegulationStatus>
  ): List<Regulation> {
    return withJdbcTransaction {
      RegulationModel.find(
        Regulations.group.eq(groupId.value).and(Regulations.status.inList(regulationStatues.map { it.name }))
      ).with(*withModel).map { it.toDomain() }
    }
  }

  override suspend fun getRegulationsByGroupIds(
    groupIds: List<GroupId>, regulationStatues: List<RegulationStatus>
  ): Map<GroupId, List<Regulation>> {
    return withJdbcTransaction {
      val regulation =
        RegulationModel.find(Regulations.group inList groupIds.map { g -> g.value } and Regulations.status.inList(
          regulationStatues.map { it.name })
        ).with(*withModel).map { it.toDomain() }
      regulation.groupBy { it.groupId }
    }
  }
}
