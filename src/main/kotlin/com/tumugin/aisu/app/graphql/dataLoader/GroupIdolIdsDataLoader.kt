package com.tumugin.aisu.app.graphql.dataLoader

import com.expediagroup.graphql.dataloader.KotlinDataLoader
import com.expediagroup.graphql.generator.scalars.ID
import com.tumugin.aisu.app.graphql.AisuGraphQLContext
import com.tumugin.aisu.domain.group.GroupId
import com.tumugin.aisu.usecase.client.group.GetGroup
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.future.future
import org.dataloader.DataLoader
import org.dataloader.DataLoaderFactory

const val GroupIdolIdsDataLoaderName = "GroupIdolIdsDataLoader"

class GroupIdolIdsDataLoader : KotlinDataLoader<ID, List<ID>> {
  override val dataLoaderName = GroupIdolIdsDataLoaderName
  private val getGroup = GetGroup()

  override fun getDataLoader(): DataLoader<ID, List<ID>> {
    return DataLoaderFactory.newDataLoader { ids, dfe ->
      val aisuGraphQLContext = dfe.keyContextsList[0] as AisuGraphQLContext
      CoroutineScope(aisuGraphQLContext.coroutineContext).future {
        val groupIdAndIdolIdsMap = getGroup.getIdolIdsOfGroups(aisuGraphQLContext.userAuthSession?.castedUserId,
          ids.map { GroupId(it.value.toLong()) })
        ids.map { groupId ->
          groupIdAndIdolIdsMap[GroupId(groupId.value.toLong())]?.map { ID(it.value.toString()) } ?: emptyList()
        }
      }
    }
  }
}
