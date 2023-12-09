package com.tumugin.aisu.app.graphql

import com.expediagroup.graphql.server.execution.GraphQLContextFactory
import com.tumugin.aisu.app.plugins.AdminUserAuthSession
import com.tumugin.aisu.app.plugins.UserAuthSession
import graphql.schema.DataFetchingEnvironment
import io.ktor.server.request.*
import io.ktor.server.sessions.*
import kotlinx.coroutines.currentCoroutineContext
import kotlin.coroutines.CoroutineContext

class KtorGraphQLContextFactory : GraphQLContextFactory<ApplicationRequest> {
  override suspend fun generateContext(request: ApplicationRequest): graphql.GraphQLContext {
    val aisuGraphQLContext =
      AisuGraphQLContext(request.call.sessions.get(), request.call.sessions.get(), currentCoroutineContext())

    val graphQLContext = graphql.GraphQLContext.newContext()
      .of(AisuGraphQLContext::class, aisuGraphQLContext)
      .of(ApplicationRequest::class, request)
      .of(CoroutineContext::class, currentCoroutineContext())
      .build()

    return graphQLContext
  }
}

class AisuGraphQLContext(
  val userAuthSession: UserAuthSession?,
  val adminUserAuthSession: AdminUserAuthSession?,
  val coroutineContext: CoroutineContext
) {
  companion object {
    fun createFromDataFetchingEnvironment(dfe: DataFetchingEnvironment): AisuGraphQLContext {
      return dfe.graphQlContext.get(AisuGraphQLContext::class)
    }
  }
}
