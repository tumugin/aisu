package com.tumugin.aisu.app.graphql

import com.expediagroup.graphql.dataloader.KotlinDataLoaderRegistryFactory
import com.expediagroup.graphql.server.execution.GraphQLRequestHandler
import com.expediagroup.graphql.server.execution.GraphQLServer
import com.fasterxml.jackson.databind.ObjectMapper
import com.tumugin.aisu.app.graphql.dataLoader.*
import io.ktor.server.request.*

class KtorGraphQLServer(
  requestParser: KtorGraphQLRequestParser,
  contextFactory: KtorGraphQLContextFactory,
  requestHandler: GraphQLRequestHandler
) : GraphQLServer<ApplicationRequest>(requestParser, contextFactory, requestHandler) {
  companion object {
    fun getGraphQLServer(mapper: ObjectMapper): KtorGraphQLServer {
      val dataLoaderRegistryFactory = KotlinDataLoaderRegistryFactory(
        listOf(
          IdolDataLoader(),
          LimitedUserDataLoader(),
          RegulationDataLoader(),
          GroupDataLoader(),
          ChekiDataLoader(),
          IdolGroupIdsDataLoader()
        )
      )
      val requestParser = KtorGraphQLRequestParser(mapper)
      val contextFactory = KtorGraphQLContextFactory()
      val graphQL = GraphQLSchema().getGraphQLObject()
      val requestHandler = GraphQLRequestHandler(graphQL, dataLoaderRegistryFactory)

      return KtorGraphQLServer(requestParser, contextFactory, requestHandler)
    }
  }
}
