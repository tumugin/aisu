package com.tumugin.aisu.app.serializer.client

import com.expediagroup.graphql.generator.scalars.ID
import com.expediagroup.graphql.server.extensions.getValueFromDataLoader
import com.tumugin.aisu.app.graphql.dataLoader.LimitedUserDataLoaderName
import com.tumugin.aisu.app.graphql.dataLoader.RegulationOfGroupDataLoaderName
import com.tumugin.aisu.app.serializer.IDSerializer
import com.tumugin.aisu.domain.group.Group
import graphql.schema.DataFetchingEnvironment
import kotlinx.serialization.Serializable
import java.util.concurrent.CompletableFuture

@Serializable
data class GroupSerializer(
  @Serializable(with = IDSerializer::class) val groupId: ID,
  @Serializable(with = IDSerializer::class) val userId: ID?,
  val groupName: String,
  val groupStatus: String,
  val groupCreatedAt: String,
  val groupUpdatedAt: String
) {
  fun user(dataFetchingEnvironment: DataFetchingEnvironment): CompletableFuture<LimitedUserSerializer?> {
    return dataFetchingEnvironment.getValueFromDataLoader(LimitedUserDataLoaderName, userId)
  }

  fun regulations(dataFetchingEnvironment: DataFetchingEnvironment): CompletableFuture<List<RegulationSerializer>> {
    return dataFetchingEnvironment.getValueFromDataLoader(RegulationOfGroupDataLoaderName, groupId)
  }

  companion object {
    fun from(group: Group): GroupSerializer {
      return GroupSerializer(
        groupId = ID(group.groupId.value.toString()),
        userId = group.userId?.let { ID(it.value.toString()) },
        groupName = group.groupName.value,
        groupStatus = group.groupStatus.toString(),
        groupCreatedAt = group.groupCreatedAt.toString(),
        groupUpdatedAt = group.groupUpdatedAt.toString()
      )
    }
  }
}
