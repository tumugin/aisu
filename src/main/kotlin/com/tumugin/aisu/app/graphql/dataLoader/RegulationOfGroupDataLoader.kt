package com.tumugin.aisu.app.graphql.dataLoader

import com.expediagroup.graphql.dataloader.KotlinDataLoader
import com.expediagroup.graphql.generator.scalars.ID
import com.tumugin.aisu.app.graphql.AisuGraphQLContext
import com.tumugin.aisu.app.serializer.client.RegulationSerializer
import com.tumugin.aisu.domain.group.GroupId
import com.tumugin.aisu.domain.regulation.RegulationStatus
import com.tumugin.aisu.domain.user.UserId
import com.tumugin.aisu.usecase.client.regulation.GetRegulation
import graphql.GraphQLContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.future.future
import org.dataloader.DataLoader
import org.dataloader.DataLoaderFactory

const val RegulationOfGroupDataLoaderName = "RegulationOfGroupDataLoader"

class RegulationOfGroupDataLoader : KotlinDataLoader<ID, List<RegulationSerializer>> {
  private val getRegulation = GetRegulation()
  override val dataLoaderName = RegulationOfGroupDataLoaderName

  override fun getDataLoader(graphQLContext: GraphQLContext): DataLoader<ID, List<RegulationSerializer>> =
    DataLoaderFactory.newDataLoader { ids, dfe ->
      val aisuGraphQLContext = graphQLContext.get<AisuGraphQLContext>(AisuGraphQLContext::class)

      CoroutineScope(aisuGraphQLContext.coroutineContext).future {
        val regulations = getRegulation.getRegulationsByGroupIds(
          aisuGraphQLContext.userAuthSession?.userId?.let { UserId(it) },
          ids.map { GroupId(it.value.toLong()) },
          RegulationStatus.allStatuses
        )
        ids.map { groupId ->
          regulations[GroupId(groupId.value.toLong())]?.map { RegulationSerializer.from(it) } ?: emptyList()
        }
      }
    }
}
