package com.tumugin.aisu.app.graphql.query

import com.expediagroup.graphql.server.operations.Query
import com.tumugin.aisu.app.graphql.AisuGraphQLContext
import com.tumugin.aisu.app.serializer.client.UserSerializer
import com.tumugin.aisu.usecase.client.user.GetUser

class UserQueryService : Query {
  private val getuser = GetUser()

  suspend fun currentUser(aisuGraphQLContext: AisuGraphQLContext): UserSerializer? {
    val user = aisuGraphQLContext.userAuthSession?.let { getuser.getUserBySessionUserId(it.castedUserId) }
    return user?.let { UserSerializer.from(it) }
  }
}
