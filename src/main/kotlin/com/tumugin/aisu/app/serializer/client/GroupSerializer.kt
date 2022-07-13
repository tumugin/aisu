package com.tumugin.aisu.app.serializer.client

import com.tumugin.aisu.domain.group.Group

@kotlinx.serialization.Serializable
data class GroupSerializer(
  val groupId: Long,
  val userId: Long?,
  val groupName: String,
  val groupStatus: String,
  val groupCreatedAt: String,
  val groupUpdatedAt: String
) {
  companion object {
    fun from(group: Group): GroupSerializer {
      return GroupSerializer(
        groupId = group.groupId.value,
        userId = group.userId?.value,
        groupName = group.groupName.value,
        groupStatus = group.groupStatus.toString(),
        groupCreatedAt = group.groupCreatedAt.toString(),
        groupUpdatedAt = group.groupUpdatedAt.toString()
      )
    }
  }
}
