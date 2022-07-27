package com.tumugin.aisu.app.graphql

import com.expediagroup.graphql.generator.execution.GraphQLContext
import com.expediagroup.graphql.server.execution.GraphQLContextFactory
import com.tumugin.aisu.app.plugins.UserAuthSession
import io.ktor.server.request.*
import io.ktor.server.sessions.*

class KtorGraphQLContextFactory : GraphQLContextFactory<GraphQLContext, ApplicationRequest> {
  override suspend fun generateContext(request: ApplicationRequest): GraphQLContext? {
    return null
  }

  override suspend fun generateContextMap(request: ApplicationRequest): Map<*, Any> {
    return mapOf(
      AisuGraphQLContext::class to AisuGraphQLContext(request.call.sessions.get()),
      ApplicationRequest::class to request
    )
  }
}

class AisuGraphQLContext(val userAuthSession: UserAuthSession?) : GraphQLContext
