package com.tumugin.aisu.app.graphql

import com.expediagroup.graphql.generator.SchemaGeneratorConfig
import com.expediagroup.graphql.generator.TopLevelObject
import com.expediagroup.graphql.generator.toSchema
import com.tumugin.aisu.app.graphql.mutation.UserMutationService
import com.tumugin.aisu.app.graphql.query.UserQueryService
import graphql.GraphQL

class GraphQLSchema {
  private val config =
    SchemaGeneratorConfig(supportedPackages = listOf("com.tumugin.aisu", "kotlin"))
  private val queries = listOf(TopLevelObject(UserQueryService()))
  private val mutations = listOf(TopLevelObject(UserMutationService()))
  val graphQLSchema = toSchema(config, queries, mutations)

  fun getGraphQLObject(): GraphQL = GraphQL.newGraphQL(graphQLSchema).build()
}
