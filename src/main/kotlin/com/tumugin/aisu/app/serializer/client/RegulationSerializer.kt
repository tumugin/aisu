package com.tumugin.aisu.app.serializer.client

import com.tumugin.aisu.domain.regulation.Regulation

@kotlinx.serialization.Serializable
data class RegulationSerializer(
  val regulationId: Long,
  val groupId: Long,
  val group: GroupSerializer? = null,
  val userId: Long?,
  val user: UserSerializer? = null,
  val regulationName: String,
  val regulationComment: String,
  val regulationUnitPrice: Int,
  val regulationStatus: String,
  val regulationCreatedAt: String,
  val regulationUpdatedAt: String
) {
  companion object {
    fun from(regulation: Regulation): RegulationSerializer {
      return RegulationSerializer(
        regulationId = regulation.regulationId.value,
        groupId = regulation.groupId.value,
        userId = regulation.userId?.value,
        regulationName = regulation.regulationName.value,
        regulationComment = regulation.regulationComment.value,
        regulationUnitPrice = regulation.regulationUnitPrice.value,
        regulationStatus = regulation.regulationStatus.toString(),
        regulationCreatedAt = regulation.regulationCreatedAt.value.toString(),
        regulationUpdatedAt = regulation.regulationUpdatedAt.value.toString()
      )
    }
  }
}
