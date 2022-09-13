package com.tumugin.aisu.app.graphql

import com.expediagroup.graphql.generator.SchemaGeneratorConfig
import com.expediagroup.graphql.generator.TopLevelObject
import com.expediagroup.graphql.generator.scalars.IDValueUnboxer
import com.expediagroup.graphql.generator.toSchema
import com.tumugin.aisu.app.graphql.mutation.*
import com.tumugin.aisu.app.graphql.query.*
import graphql.GraphQL

class GraphQLSchema {
  private val config = SchemaGeneratorConfig(supportedPackages = listOf("com.tumugin.aisu", "kotlin"))
  private val queries = listOf(
    TopLevelObject(UserQueryService()),
    TopLevelObject(ChekiQueryService()),
    TopLevelObject(IdolQueryService()),
    TopLevelObject(GroupQueryService()),
    TopLevelObject(RegulationQueryService()),
    TopLevelObject(FavoriteGroupQueryService())
  )
  private val mutations = listOf(
    TopLevelObject(UserMutationService()),
    TopLevelObject(ChekiMutationService()),
    TopLevelObject(IdolMutationService()),
    TopLevelObject(GroupMutationService()),
    TopLevelObject(RegulationMutationService()),
    TopLevelObject(FavoriteGroupMutationService())
  )
  val graphQLSchema = toSchema(config, queries, mutations)

  fun getGraphQLObject(): GraphQL =
    GraphQL.newGraphQL(graphQLSchema).defaultDataFetcherExceptionHandler(AisuDataFetcherExceptionHandler())
      .valueUnboxer(IDValueUnboxer()).build()
}
