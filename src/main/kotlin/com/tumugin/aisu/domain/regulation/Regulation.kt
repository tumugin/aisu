package com.tumugin.aisu.domain.regulation

import com.tumugin.aisu.domain.group.Group
import com.tumugin.aisu.domain.group.GroupId
import com.tumugin.aisu.domain.user.User
import com.tumugin.aisu.domain.user.UserId

data class Regulation(
  val regulationId: RegulationId,
  val groupId: GroupId,
  val group: Group,
  val userId: UserId?,
  val regulationName: RegulationName,
  val regulationComment: RegulationComment,
  val regulationUnitPrice: RegulationUnitPrice,
  val regulationStatus: RegulationStatus,
  val regulationCreatedAt: RegulationCreatedAt,
  val regulationUpdatedAt: RegulationUpdatedAt
) {
  fun isVisibleToUser(userId: UserId?): Boolean {
    return group.isVisibleToUser(userId)
  }

  fun isEditableByUser(userId: UserId?): Boolean {
    return group.isEditableByUser(userId)
  }
}
