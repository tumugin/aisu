package com.tumugin.aisu.app.graphql.query

import com.expediagroup.graphql.server.operations.Query
import com.tumugin.aisu.app.graphql.AisuGraphQLContext
import com.tumugin.aisu.app.serializer.client.FavoriteGroupWithGroupSerializer
import com.tumugin.aisu.domain.exception.NotAuthorizedException
import com.tumugin.aisu.usecase.client.favoritegroup.GetFavoriteGroup
import graphql.schema.DataFetchingEnvironment

class FavoriteGroupQueryService : Query {
  fun userFavoriteGroups(dfe: DataFetchingEnvironment): UserFavoriteGroup {
    val aisuGraphQLContext = AisuGraphQLContext.createFromDataFetchingEnvironment(dfe)
    if (aisuGraphQLContext.userAuthSession?.userId == null) {
      throw NotAuthorizedException()
    }
    return UserFavoriteGroup()
  }

  class UserFavoriteGroup() {
    private val getFavoriteGroup = GetFavoriteGroup()

    suspend fun favoriteGroups(dfe: DataFetchingEnvironment): List<FavoriteGroupWithGroupSerializer> {
      val aisuGraphQLContext = AisuGraphQLContext.createFromDataFetchingEnvironment(dfe)
      val favoriteGroups = getFavoriteGroup.getFavoriteGroupsByUserId(aisuGraphQLContext.userAuthSession!!.castedUserId)
      return favoriteGroups.map { FavoriteGroupWithGroupSerializer.from(it) }
    }
  }
}
