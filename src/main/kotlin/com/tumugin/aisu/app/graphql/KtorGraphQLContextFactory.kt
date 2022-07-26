package com.tumugin.aisu.app.graphql

import com.expediagroup.graphql.generator.execution.GraphQLContext
import com.expediagroup.graphql.server.execution.GraphQLContextFactory
import io.ktor.server.request.*

class KtorGraphQLContextFactory : GraphQLContextFactory<GraphQLContext, ApplicationRequest> {
  override suspend fun generateContext(request: ApplicationRequest): GraphQLContext? {
    return null
  }
}
