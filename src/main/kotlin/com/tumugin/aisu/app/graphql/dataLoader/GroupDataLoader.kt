package com.tumugin.aisu.app.graphql.dataLoader

import com.expediagroup.graphql.dataloader.KotlinDataLoader
import com.expediagroup.graphql.generator.scalars.ID
import com.tumugin.aisu.app.graphql.AisuGraphQLContext
import com.tumugin.aisu.app.serializer.client.GroupSerializer
import com.tumugin.aisu.domain.group.GroupId
import com.tumugin.aisu.usecase.client.group.GetGroup
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.future.future
import org.dataloader.DataLoader
import org.dataloader.DataLoaderFactory

const val GroupDataLoaderName = "GroupDataLoader"

class GroupDataLoader : KotlinDataLoader<ID, GroupSerializer?> {
  override val dataLoaderName = GroupDataLoaderName
  private val getGroup = GetGroup()

  override fun getDataLoader(): DataLoader<ID, GroupSerializer?> = DataLoaderFactory.newDataLoader { ids, dfe ->
    val aisuGraphQLContext = dfe.keyContextsList[0] as AisuGraphQLContext
    CoroutineScope(aisuGraphQLContext.coroutineContext).future {
      val groups =
        getGroup.getGroupsById(aisuGraphQLContext.userAuthSession?.castedUserId, ids.map { GroupId(it.value.toLong()) })
      ids.map { groupId ->
        groups.find { it.groupId.value == groupId.value.toLong() }?.let { GroupSerializer.from(it) }
      }
    }
  }
}
