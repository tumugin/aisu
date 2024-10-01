package com.tumugin.aisu.app.serializer.client

import com.expediagroup.graphql.generator.extensions.get
import com.expediagroup.graphql.generator.scalars.ID
import com.expediagroup.graphql.server.extensions.getValueFromDataLoader
import com.tumugin.aisu.app.graphql.AisuGraphQLContext
import com.tumugin.aisu.app.graphql.dataLoader.GroupDataLoaderName
import com.tumugin.aisu.app.graphql.dataLoader.IdolGroupIdsDataLoaderName
import com.tumugin.aisu.app.graphql.dataLoader.LimitedUserDataLoaderName
import com.tumugin.aisu.domain.idol.*
import graphql.schema.DataFetchingEnvironment
import java.util.concurrent.CompletableFuture

class IdolSerializer(
  val idolId: ID,
  val userId: ID?,
  val idolName: String,
  val idolStatus: IdolStatus,
  val idolCreatedAt: String,
  val idolUpdatedAt: String,
) {
  fun user(dataFetchingEnvironment: DataFetchingEnvironment): CompletableFuture<LimitedUserSerializer?> {
    return dataFetchingEnvironment.getValueFromDataLoader(LimitedUserDataLoaderName, userId)
  }

  fun groups(dataFetchingEnvironment: DataFetchingEnvironment): CompletableFuture<List<GroupSerializer?>> {
    val idolGroupIdsDataLoader = dataFetchingEnvironment.getDataLoader<ID, List<ID>>(IdolGroupIdsDataLoaderName)
      ?: throw IllegalStateException("DataLoader not found")
    val groupDataLoader = dataFetchingEnvironment.getDataLoader<ID, GroupSerializer?>(GroupDataLoaderName)
      ?: throw IllegalStateException("DataLoader not found")

    val context = dataFetchingEnvironment.graphQlContext.get<AisuGraphQLContext>()
    return idolGroupIdsDataLoader.load(idolId, context).thenCompose { groupIds ->
      groupDataLoader.loadMany(groupIds, Array(groupIds.size) { context }.toList())
        // NOTE: dispatchIfNeededだと何故か動かない
        .apply { dataFetchingEnvironment.dataLoaderRegistry.dispatchAll() }
    }
  }

  companion object {
    fun from(idol: Idol): IdolSerializer {
      return IdolSerializer(
        idolId = ID(idol.idolId.value.toString()),
        userId = idol.userId?.let { ID(it.value.toString()) },
        idolName = idol.idolName.value,
        idolStatus = idol.idolStatus,
        idolCreatedAt = idol.idolCreatedAt.value.toString(),
        idolUpdatedAt = idol.idolUpdatedAt.value.toString(),
      )
    }
  }
}
