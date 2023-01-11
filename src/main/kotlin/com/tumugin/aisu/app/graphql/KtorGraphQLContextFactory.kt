package com.tumugin.aisu.app.graphql

import com.expediagroup.graphql.generator.execution.GraphQLContext
import com.expediagroup.graphql.server.execution.GraphQLContextFactory
import com.tumugin.aisu.app.plugins.AdminUserAuthSession
import com.tumugin.aisu.app.plugins.UserAuthSession
import graphql.schema.DataFetchingEnvironment
import io.ktor.server.request.*
import io.ktor.server.sessions.*
import kotlinx.coroutines.currentCoroutineContext
import kotlin.coroutines.CoroutineContext

class KtorGraphQLContextFactory : GraphQLContextFactory<GraphQLContext, ApplicationRequest> {
  override suspend fun generateContext(request: ApplicationRequest): GraphQLContext {
    // FIXME: GraphQLContextへの謎依存は7.xから外れるっぽいのでそのタイミングで依存を引き剥がす対応する
    return AisuGraphQLContext(request.call.sessions.get(), request.call.sessions.get(), currentCoroutineContext())
  }

  override suspend fun generateContextMap(request: ApplicationRequest): Map<*, Any> {
    return mapOf(
      AisuGraphQLContext::class to AisuGraphQLContext(
        request.call.sessions.get(), request.call.sessions.get(), currentCoroutineContext()
      ), ApplicationRequest::class to request, CoroutineContext::class to currentCoroutineContext()
    )
  }
}

class AisuGraphQLContext(
  val userAuthSession: UserAuthSession?,
  val adminUserAuthSession: AdminUserAuthSession?,
  val coroutineContext: CoroutineContext
) : GraphQLContext {
  companion object {
    fun createFromDataFetchingEnvironment(dfe: DataFetchingEnvironment): AisuGraphQLContext {
      return dfe.graphQlContext.get(AisuGraphQLContext::class)
    }
  }
}
