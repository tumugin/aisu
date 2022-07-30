package com.tumugin.aisu.app.serializer.client

import com.expediagroup.graphql.generator.scalars.ID
import com.expediagroup.graphql.server.extensions.getValueFromDataLoader
import com.tumugin.aisu.app.graphql.dataLoader.GroupDataLoaderName
import com.tumugin.aisu.app.graphql.dataLoader.LimitedUserDataLoaderName
import com.tumugin.aisu.app.serializer.IDSerializer
import com.tumugin.aisu.domain.regulation.Regulation
import graphql.schema.DataFetchingEnvironment
import kotlinx.serialization.Serializable
import java.util.concurrent.CompletableFuture

@Serializable
data class RegulationSerializer(
  @Serializable(with = IDSerializer::class)
  val regulationId: ID,
  @Serializable(with = IDSerializer::class)
  val groupId: ID,
  @Serializable(with = IDSerializer::class)
  val userId: ID?,
  val regulationName: String,
  val regulationComment: String,
  val regulationUnitPrice: Int,
  val regulationStatus: String,
  val regulationCreatedAt: String,
  val regulationUpdatedAt: String
) {
  fun user(dataFetchingEnvironment: DataFetchingEnvironment): CompletableFuture<LimitedUserSerializer?> {
    return dataFetchingEnvironment.getValueFromDataLoader(LimitedUserDataLoaderName, userId)
  }

  fun group(dataFetchingEnvironment: DataFetchingEnvironment): CompletableFuture<GroupSerializer?> {
    return dataFetchingEnvironment.getValueFromDataLoader(GroupDataLoaderName, groupId)
  }

  companion object {
    fun from(regulation: Regulation): RegulationSerializer {
      return RegulationSerializer(
        regulationId = ID(regulation.regulationId.value.toString()),
        groupId = ID(regulation.groupId.value.toString()),
        userId = regulation.userId?.let { ID(it.value.toString()) },
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
