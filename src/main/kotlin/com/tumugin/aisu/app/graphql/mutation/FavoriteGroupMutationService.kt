package com.tumugin.aisu.app.graphql.mutation

import com.expediagroup.graphql.generator.scalars.ID
import com.expediagroup.graphql.server.operations.Mutation
import com.tumugin.aisu.app.graphql.AisuGraphQLContext
import com.tumugin.aisu.app.graphql.params.aisuIdsValidator
import com.tumugin.aisu.app.graphql.params.assertValidationResult
import com.tumugin.aisu.app.serializer.client.FavoriteGroupSerializer
import com.tumugin.aisu.domain.exception.NotAuthorizedException
import com.tumugin.aisu.domain.favoritegroup.FavoriteGroupId
import com.tumugin.aisu.domain.group.GroupId
import com.tumugin.aisu.usecase.client.favoritegroup.WriteFavoriteGroup
import graphql.schema.DataFetchingEnvironment

class FavoriteGroupMutationService : Mutation {
  fun favoriteGroup(dfe: DataFetchingEnvironment): FavoriteGroupMutationServices {
    // ログインしないと使えない機能
    val aisuGraphQLContext = AisuGraphQLContext.createFromDataFetchingEnvironment(dfe)
    if (aisuGraphQLContext.userAuthSession?.userId == null) {
      throw NotAuthorizedException()
    }

    return FavoriteGroupMutationServices()
  }

  class FavoriteGroupMutationServices {
    private val writeFavoriteGroup = WriteFavoriteGroup()

    suspend fun addFavoriteGroup(dfe: DataFetchingEnvironment, groupId: ID): FavoriteGroupSerializer {
      val aisuGraphQLContext = AisuGraphQLContext.createFromDataFetchingEnvironment(dfe)
      assertValidationResult(aisuIdsValidator.validate(groupId))
      val favoriteGroup = writeFavoriteGroup.addFavoriteGroup(
        aisuGraphQLContext.userAuthSession!!.castedUserId, GroupId(groupId.value.toLong())
      )

      return FavoriteGroupSerializer.from(favoriteGroup)
    }

    suspend fun deleteFavoriteGroup(dfe: DataFetchingEnvironment, favoriteGroupID: ID): String {
      val aisuGraphQLContext = AisuGraphQLContext.createFromDataFetchingEnvironment(dfe)
      assertValidationResult(aisuIdsValidator.validate(favoriteGroupID))
      writeFavoriteGroup.deleteFavoriteGroup(
        aisuGraphQLContext.userAuthSession!!.castedUserId, FavoriteGroupId(favoriteGroupID.value.toLong())
      )

      return "favorite group deleted."
    }
  }
}
