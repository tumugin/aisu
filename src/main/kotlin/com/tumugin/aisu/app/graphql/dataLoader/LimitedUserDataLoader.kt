package com.tumugin.aisu.app.graphql.dataLoader

import com.expediagroup.graphql.dataloader.KotlinDataLoader
import com.expediagroup.graphql.generator.scalars.ID
import com.tumugin.aisu.app.graphql.AisuGraphQLContext
import com.tumugin.aisu.app.serializer.client.LimitedUserSerializer
import com.tumugin.aisu.domain.user.UserId
import com.tumugin.aisu.usecase.client.user.GetUser
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.future.future
import org.dataloader.DataLoaderFactory

const val LimitedUserDataLoaderName = "LimitedUserDataLoader"

class LimitedUserDataLoader : KotlinDataLoader<ID, LimitedUserSerializer?> {
  override val dataLoaderName = LimitedUserDataLoaderName
  private val getUser = GetUser()

  override fun getDataLoader() =
    DataLoaderFactory.newDataLoader<ID, LimitedUserSerializer?> { ids, dfe ->
      val aisuGraphQLContext = dfe.keyContextsList[0] as AisuGraphQLContext
      CoroutineScope(aisuGraphQLContext.coroutineContext).future {
        val users =
          getUser.getUserByIds(
            aisuGraphQLContext.userAuthSession?.castedUserId,
            ids.map { UserId(it.value.toLong()) }
          )
        ids.map { userId ->
          users.find { it.userId.value == userId.value.toLong() }?.let { LimitedUserSerializer.from(it) }
        }
      }
    }
}
