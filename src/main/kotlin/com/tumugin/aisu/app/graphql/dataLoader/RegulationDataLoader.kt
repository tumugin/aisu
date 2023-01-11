package com.tumugin.aisu.app.graphql.dataLoader

import com.expediagroup.graphql.dataloader.KotlinDataLoader
import com.expediagroup.graphql.generator.scalars.ID
import com.tumugin.aisu.app.graphql.AisuGraphQLContext
import com.tumugin.aisu.app.serializer.client.RegulationSerializer
import com.tumugin.aisu.domain.regulation.RegulationId
import com.tumugin.aisu.usecase.client.regulation.GetRegulation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.future.future
import org.dataloader.DataLoader
import org.dataloader.DataLoaderFactory

const val RegulationDataLoaderName = "RegulationDataLoader"

class RegulationDataLoader : KotlinDataLoader<ID, RegulationSerializer?> {
  override val dataLoaderName = RegulationDataLoaderName
  private val getRegulation = GetRegulation()

  override fun getDataLoader(): DataLoader<ID, RegulationSerializer?> =
    DataLoaderFactory.newDataLoader { ids, dfe ->
      val aisuGraphQLContext = dfe.keyContextsList[0] as AisuGraphQLContext
      CoroutineScope(aisuGraphQLContext.coroutineContext).future {
        val regulations = getRegulation.getRegulationsByIds(
          aisuGraphQLContext.userAuthSession?.castedUserId,
          ids.map { RegulationId(it.value.toLong()) }
        )
        ids.map { regulationId ->
          regulations.find { it.regulationId.value == regulationId.value.toLong() }?.let {
            RegulationSerializer.from(it)
          }
        }
      }
    }
}
