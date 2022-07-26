package com.tumugin.aisu.app.graphql

import com.expediagroup.graphql.generator.SchemaGeneratorConfig
import com.expediagroup.graphql.generator.TopLevelObject
import com.expediagroup.graphql.generator.toSchema
import graphql.GraphQL

class GraphQLSchema {
  companion object {
    private val config =
      SchemaGeneratorConfig(supportedPackages = listOf("com.expediagroup.graphql.examples.server.ktor"))
    private val queries = listOf<TopLevelObject>()
    private val mutations = listOf<TopLevelObject>()
    val graphQLSchema = toSchema(config, queries, mutations)

    fun getGraphQLObject(): GraphQL = GraphQL.newGraphQL(graphQLSchema).build()
  }
}
