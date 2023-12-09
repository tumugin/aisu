package com.tumugin.aisu.app.graphql.dataLoader

import com.expediagroup.graphql.dataloader.KotlinDataLoader
import com.expediagroup.graphql.generator.scalars.ID
import com.tumugin.aisu.app.graphql.AisuGraphQLContext
import com.tumugin.aisu.app.serializer.client.IdolSerializer
import com.tumugin.aisu.domain.idol.IdolId
import com.tumugin.aisu.usecase.client.idol.GetIdol
import graphql.GraphQLContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.future.future
import org.dataloader.DataLoader
import org.dataloader.DataLoaderFactory

const val IdolDataLoaderName = "idolDataLoader"

class IdolDataLoader : KotlinDataLoader<ID, IdolSerializer?> {
  override val dataLoaderName = IdolDataLoaderName
  private val getIdol = GetIdol()

  override fun getDataLoader(graphQLContext: GraphQLContext): DataLoader<ID, IdolSerializer?> =
    DataLoaderFactory.newDataLoader { ids, dfe ->
      val aisuGraphQLContext = graphQLContext.get<AisuGraphQLContext>(AisuGraphQLContext::class)

      CoroutineScope(aisuGraphQLContext.coroutineContext).future {
        val idols =
          getIdol.getIdolsByIds(
            aisuGraphQLContext.userAuthSession?.castedUserId,
            ids.map { IdolId(it.value.toLong()) }
          )
        ids.map { idolId ->
          idols.find { it.idolId.value == idolId.value.toLong() }?.let { IdolSerializer.from(it) }
        }
      }
    }
}
