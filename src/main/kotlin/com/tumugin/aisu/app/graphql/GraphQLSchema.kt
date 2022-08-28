package com.tumugin.aisu.app.graphql

import com.expediagroup.graphql.generator.SchemaGeneratorConfig
import com.expediagroup.graphql.generator.TopLevelObject
import com.expediagroup.graphql.generator.scalars.IDValueUnboxer
import com.expediagroup.graphql.generator.toSchema
import com.tumugin.aisu.app.graphql.mutation.ChekiMutationService
import com.tumugin.aisu.app.graphql.mutation.IdolMutationService
import com.tumugin.aisu.app.graphql.mutation.UserMutationService
import com.tumugin.aisu.app.graphql.query.ChekiQueryService
import com.tumugin.aisu.app.graphql.query.GroupQueryService
import com.tumugin.aisu.app.graphql.query.IdolQueryService
import com.tumugin.aisu.app.graphql.query.UserQueryService
import graphql.GraphQL

class GraphQLSchema {
  private val config = SchemaGeneratorConfig(supportedPackages = listOf("com.tumugin.aisu", "kotlin"))
  private val queries = listOf(
    TopLevelObject(UserQueryService()),
    TopLevelObject(ChekiQueryService()),
    TopLevelObject(IdolQueryService()),
    TopLevelObject(GroupQueryService())
  )
  private val mutations = listOf(
    TopLevelObject(UserMutationService()), TopLevelObject(ChekiMutationService()), TopLevelObject(IdolMutationService())
  )
  val graphQLSchema = toSchema(config, queries, mutations)

  fun getGraphQLObject(): GraphQL =
    GraphQL.newGraphQL(graphQLSchema).defaultDataFetcherExceptionHandler(AisuDataFetcherExceptionHandler())
      .valueUnboxer(IDValueUnboxer()).build()
}
