package com.tumugin.aisu.app.graphql.dataLoader

import com.expediagroup.graphql.dataloader.KotlinDataLoader
import com.expediagroup.graphql.generator.scalars.ID
import com.tumugin.aisu.app.graphql.AisuGraphQLContext
import com.tumugin.aisu.app.serializer.client.IdolSerializer
import com.tumugin.aisu.domain.idol.IdolId
import com.tumugin.aisu.usecase.client.idol.GetIdol
import graphql.GraphQLContext
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.future.future
import org.dataloader.DataLoader
import org.dataloader.DataLoaderFactory

const val IdolDataLoaderName = "idolDataLoader"

class IdolDataLoader : KotlinDataLoader<ID, IdolSerializer> {
  override val dataLoaderName = IdolDataLoaderName
  private val getIdol = GetIdol()

  override fun getDataLoader(): DataLoader<ID, IdolSerializer> =
    DataLoaderFactory.newDataLoader { ids, dfe ->
      val aisuGraphQLContext =
        (dfe.keyContextsList.first() as GraphQLContext).get<AisuGraphQLContext>(AisuGraphQLContext::class)
      GlobalScope.future {
        val idols = getIdol.getIdolsByIds(
          aisuGraphQLContext.userAuthSession?.castedUserId,
          ids.map { IdolId(it.value.toLong()) }
        )
        idols.map { IdolSerializer.from(it) }
      }
    }
}
