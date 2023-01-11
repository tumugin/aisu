package com.tumugin.aisu.app.graphql.dataLoader

import com.expediagroup.graphql.dataloader.KotlinDataLoader
import com.expediagroup.graphql.generator.scalars.ID
import com.tumugin.aisu.app.graphql.AisuGraphQLContext
import com.tumugin.aisu.app.serializer.client.ChekiSerializer
import com.tumugin.aisu.domain.cheki.ChekiId
import com.tumugin.aisu.domain.user.UserId
import com.tumugin.aisu.usecase.client.cheki.GetCheki
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.future.future
import org.dataloader.DataLoader
import org.dataloader.DataLoaderFactory

const val ChekiDataLoaderName = "ChekiDataLoader"

class ChekiDataLoader : KotlinDataLoader<ID, ChekiSerializer?> {
  override val dataLoaderName = ChekiDataLoaderName
  private val getCheki = GetCheki()

  override fun getDataLoader(): DataLoader<ID, ChekiSerializer?> = DataLoaderFactory.newDataLoader { ids, dfe ->
    val aisuGraphQLContext = dfe.keyContextsList[0] as AisuGraphQLContext
    CoroutineScope(aisuGraphQLContext.coroutineContext).future {
      val chekis =
        getCheki.getChekisByIds(
          aisuGraphQLContext.userAuthSession?.userId?.let { UserId(it) },
          ids.map { ChekiId(it.value.toLong()) })
      ids.map{ chekiId ->
        chekis.find { it.chekiId.value == chekiId.value.toLong() }?.let { ChekiSerializer.from(it) }
      }
    }
  }
}
