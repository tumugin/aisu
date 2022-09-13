package com.tumugin.aisu.app.serializer.client

import com.expediagroup.graphql.generator.scalars.ID
import com.expediagroup.graphql.server.extensions.getValueFromDataLoader
import com.tumugin.aisu.app.graphql.dataLoader.GroupDataLoaderName
import com.tumugin.aisu.app.graphql.dataLoader.LimitedUserDataLoaderName
import com.tumugin.aisu.app.serializer.IDSerializer
import com.tumugin.aisu.domain.favoritegroup.FavoriteGroup
import graphql.schema.DataFetchingEnvironment
import kotlinx.serialization.Serializable
import java.util.concurrent.CompletableFuture

@Serializable
class FavoriteGroupSerializer(
  @Serializable(with = IDSerializer::class) val favoriteGroupId: ID,
  @Serializable(with = IDSerializer::class) val userId: ID,
  @Serializable(with = IDSerializer::class) val groupId: ID
) {
  fun user(dataFetchingEnvironment: DataFetchingEnvironment): CompletableFuture<LimitedUserSerializer?> {
    return dataFetchingEnvironment.getValueFromDataLoader(LimitedUserDataLoaderName, userId)
  }

  fun group(dataFetchingEnvironment: DataFetchingEnvironment): CompletableFuture<GroupSerializer?> {
    return dataFetchingEnvironment.getValueFromDataLoader(GroupDataLoaderName, groupId)
  }

  companion object {
    fun from(favoriteGroup: FavoriteGroup): FavoriteGroupSerializer {
      return FavoriteGroupSerializer(
        favoriteGroupId = ID(favoriteGroup.favoriteGroupId.value.toString()),
        userId = ID(favoriteGroup.userId.value.toString()),
        groupId = ID(favoriteGroup.groupId.value.toString())
      )
    }
  }
}
