package com.tumugin.aisu.app.graphql.query

import com.expediagroup.graphql.server.operations.Query
import com.tumugin.aisu.app.graphql.AisuGraphQLContext
import com.tumugin.aisu.app.serializer.client.UserSerializer
import com.tumugin.aisu.usecase.client.user.GetUser
import graphql.schema.DataFetchingEnvironment

class UserQueryService : Query {
  private val getuser = GetUser()

  suspend fun currentUser(dfe: DataFetchingEnvironment): UserSerializer? {
    val aisuGraphQLContext = dfe.graphQlContext.get<AisuGraphQLContext>(AisuGraphQLContext::class)
    val user = aisuGraphQLContext.userAuthSession?.let { getuser.getUserBySessionUserId(it.castedUserId) }
    return user?.let { UserSerializer.from(it) }
  }
}
