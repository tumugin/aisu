package com.tumugin.aisu.app.serializer.client

import com.expediagroup.graphql.generator.scalars.ID
import com.tumugin.aisu.app.serializer.IDSerializer
import com.tumugin.aisu.domain.group.Group
import kotlinx.serialization.Serializable

@Serializable
data class GroupSerializer(
  val groupId: Long,
  @Serializable(with = IDSerializer::class)
  val userId: ID?,
  val user: LimitedUserSerializer? = null,
  val groupName: String,
  val groupStatus: String,
  val groupCreatedAt: String,
  val groupUpdatedAt: String
) {
  companion object {
    fun from(group: Group): GroupSerializer {
      return GroupSerializer(
        groupId = group.groupId.value,
        userId = group.userId?.let { ID(it.value.toString()) },
        groupName = group.groupName.value,
        groupStatus = group.groupStatus.toString(),
        groupCreatedAt = group.groupCreatedAt.toString(),
        groupUpdatedAt = group.groupUpdatedAt.toString()
      )
    }
  }
}
