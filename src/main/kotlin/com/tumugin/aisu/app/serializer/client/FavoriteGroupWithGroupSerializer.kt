package com.tumugin.aisu.app.serializer.client

import com.expediagroup.graphql.generator.scalars.ID
import com.expediagroup.graphql.server.extensions.getValueFromDataLoader
import com.tumugin.aisu.app.graphql.dataLoader.LimitedUserDataLoaderName
import com.tumugin.aisu.app.serializer.IDSerializer
import com.tumugin.aisu.domain.favoritegroup.FavoriteGroupWithGroup
import graphql.schema.DataFetchingEnvironment
import kotlinx.serialization.Serializable
import java.util.concurrent.CompletableFuture

@Serializable
data class FavoriteGroupWithGroupSerializer(
  @Serializable(with = IDSerializer::class) val favoriteGroupId: ID,
  @Serializable(with = IDSerializer::class) val userId: ID,
  @Serializable(with = IDSerializer::class) val groupId: ID,
  val group: GroupSerializer?
) {
  fun user(dataFetchingEnvironment: DataFetchingEnvironment): CompletableFuture<LimitedUserSerializer?> {
    return dataFetchingEnvironment.getValueFromDataLoader(LimitedUserDataLoaderName, userId)
  }

  companion object {
    fun from(favoriteGroupWithGroup: FavoriteGroupWithGroup): FavoriteGroupWithGroupSerializer {
      return FavoriteGroupWithGroupSerializer(favoriteGroupId = ID(favoriteGroupWithGroup.favoriteGroupId.value.toString()),
        userId = ID(favoriteGroupWithGroup.userId.value.toString()),
        groupId = ID(favoriteGroupWithGroup.groupId.value.toString()),
        group = favoriteGroupWithGroup.group?.let { GroupSerializer.from(it) })
    }
  }
}
