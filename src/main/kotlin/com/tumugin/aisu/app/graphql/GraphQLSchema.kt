package com.tumugin.aisu.app.graphql

import com.expediagroup.graphql.generator.SchemaGeneratorConfig
import com.expediagroup.graphql.generator.TopLevelObject
import com.expediagroup.graphql.generator.scalars.IDValueUnboxer
import com.expediagroup.graphql.generator.toSchema
import com.tumugin.aisu.app.graphql.mutation.*
import com.tumugin.aisu.app.graphql.mutation.admin.AdminMutationService
import com.tumugin.aisu.app.graphql.query.*
import com.tumugin.aisu.app.graphql.query.admin.AdminQueryService
import com.tumugin.aisu.domain.app.config.AppConfigRepository
import com.tumugin.aisu.domain.app.config.AppEnvironment
import graphql.GraphQL
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class GraphQLSchema : KoinComponent {
  private val appConfigRepository by inject<AppConfigRepository>()

  private val config = SchemaGeneratorConfig(
    supportedPackages = listOf("com.tumugin.aisu", "kotlin"),
    introspectionEnabled = appConfigRepository.appConfig.appEnvironment != AppEnvironment.PRODUCTION
  )
  private val queries = listOf(
    TopLevelObject(UserQueryService()),
    TopLevelObject(ChekiQueryService()),
    TopLevelObject(IdolQueryService()),
    TopLevelObject(GroupQueryService()),
    TopLevelObject(RegulationQueryService()),
    TopLevelObject(FavoriteGroupQueryService()),
    TopLevelObject(AdminQueryService()),
    TopLevelObject(CsrfTokenQueryService())
  )
  private val mutations = listOf(
    TopLevelObject(UserMutationService()),
    TopLevelObject(ChekiMutationService()),
    TopLevelObject(IdolMutationService()),
    TopLevelObject(GroupMutationService()),
    TopLevelObject(RegulationMutationService()),
    TopLevelObject(FavoriteGroupMutationService()),
    TopLevelObject(AdminMutationService())
  )
  val graphQLSchema = toSchema(config, queries, mutations)

  fun getGraphQLObject(): GraphQL =
    GraphQL.newGraphQL(graphQLSchema).defaultDataFetcherExceptionHandler(AisuDataFetcherExceptionHandler())
      .valueUnboxer(IDValueUnboxer()).build()
}
