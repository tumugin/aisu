package com.tumugin.aisu.app.graphql.dataLoader

import com.expediagroup.graphql.dataloader.KotlinDataLoader
import com.expediagroup.graphql.generator.scalars.ID
import com.tumugin.aisu.app.graphql.AisuGraphQLContext
import com.tumugin.aisu.domain.idol.IdolId
import com.tumugin.aisu.usecase.client.idol.GetIdol
import graphql.GraphQLContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.future.future
import org.dataloader.DataLoader
import org.dataloader.DataLoaderFactory

const val IdolGroupIdsDataLoaderName = "IdolGroupIdsDataLoader"

class IdolGroupIdsDataLoader : KotlinDataLoader<ID, List<ID>> {
  override val dataLoaderName = IdolGroupIdsDataLoaderName
  private val getIdol = GetIdol()

  override fun getDataLoader(graphQLContext: GraphQLContext): DataLoader<ID, List<ID>> {
    return DataLoaderFactory.newDataLoader { ids, dfe ->
      val aisuGraphQLContext = graphQLContext.get<AisuGraphQLContext>(AisuGraphQLContext::class)

      CoroutineScope(aisuGraphQLContext.coroutineContext).future {
        val idolIdAndGroupIdsMap = getIdol.getGroupIdsOfIdols(
          aisuGraphQLContext.userAuthSession?.castedUserId,
          ids.map { IdolId(it.value.toLong()) }
        )
        ids.map { idolId ->
          idolIdAndGroupIdsMap[IdolId(idolId.value.toLong())]?.map { ID(it.value.toString()) } ?: emptyList()
        }
      }
    }
  }
}
