package com.tumugin.aisu.app.graphql.dataLoader

import com.expediagroup.graphql.dataloader.KotlinDataLoader
import com.expediagroup.graphql.generator.scalars.ID
import com.tumugin.aisu.app.graphql.AisuGraphQLContext
import com.tumugin.aisu.app.serializer.client.RegulationSerializer
import com.tumugin.aisu.domain.regulation.RegulationId
import com.tumugin.aisu.usecase.client.regulation.GetRegulation
import graphql.GraphQLContext
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.future.future
import org.dataloader.DataLoader
import org.dataloader.DataLoaderFactory

class RegulationDataLoader : KotlinDataLoader<ID, RegulationSerializer> {
  override val dataLoaderName = "RegulationDataLoader"
  private val getRegulation = GetRegulation()

  override fun getDataLoader(): DataLoader<ID, RegulationSerializer> =
    DataLoaderFactory.newDataLoader { ids, dfe ->
      val aisuGraphQLContext =
        dfe.getContext<GraphQLContext>().get<AisuGraphQLContext>(AisuGraphQLContext::class)
      GlobalScope.future {
        val regulations = getRegulation.getRegulationsByIds(
          aisuGraphQLContext.userAuthSession?.castedUserId,
          ids.map { RegulationId(it.value.toLong()) }
        )
        regulations.map { RegulationSerializer.from(it) }
      }
    }
}
