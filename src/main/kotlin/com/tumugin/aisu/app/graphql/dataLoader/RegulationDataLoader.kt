package com.tumugin.aisu.app.graphql.dataLoader

import com.expediagroup.graphql.dataloader.KotlinDataLoader
import com.expediagroup.graphql.generator.scalars.ID
import com.tumugin.aisu.app.graphql.AisuGraphQLContext
import com.tumugin.aisu.app.serializer.client.RegulationSerializer
import com.tumugin.aisu.domain.regulation.RegulationId
import com.tumugin.aisu.usecase.client.regulation.GetRegulation
import graphql.GraphQLContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.future.future
import org.dataloader.DataLoader
import org.dataloader.DataLoaderFactory

const val RegulationDataLoaderName = "RegulationDataLoader"

class RegulationDataLoader : KotlinDataLoader<ID, RegulationSerializer?> {
  override val dataLoaderName = RegulationDataLoaderName
  private val getRegulation = GetRegulation()

  override fun getDataLoader(graphQLContext: GraphQLContext): DataLoader<ID, RegulationSerializer?> =
    DataLoaderFactory.newDataLoader { ids, dfe ->
      val aisuGraphQLContext = graphQLContext.get<AisuGraphQLContext>(AisuGraphQLContext::class)

      CoroutineScope(aisuGraphQLContext.coroutineContext).future {
        val regulations = getRegulation.getRegulationsByIds(
          aisuGraphQLContext.userAuthSession?.castedUserId,
          ids.filterNotNull().map { RegulationId(it.value.toLong()) }
        )
        ids.map { regulationId ->
          if (regulationId == null) {
            return@map null
          }
          regulations.find { it.regulationId.value == regulationId.value.toLong() }?.let {
            RegulationSerializer.from(it)
          }
        }
      }
    }
}
