package com.tumugin.aisu.app.graphql.dataLoader

import com.expediagroup.graphql.dataloader.KotlinDataLoader
import com.expediagroup.graphql.generator.scalars.ID
import com.tumugin.aisu.app.graphql.AisuGraphQLContext
import com.tumugin.aisu.app.serializer.client.GroupSerializer
import com.tumugin.aisu.domain.group.GroupId
import com.tumugin.aisu.usecase.client.group.GetGroup
import graphql.GraphQLContext
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.future.future
import org.dataloader.DataLoader
import org.dataloader.DataLoaderFactory

const val GroupDataLoaderName = "GroupDataLoader"

class GroupDataLoader : KotlinDataLoader<ID, GroupSerializer> {
  override val dataLoaderName = GroupDataLoaderName
  private val getGroup = GetGroup()

  override fun getDataLoader(): DataLoader<ID, GroupSerializer> =
    DataLoaderFactory.newDataLoader { ids, dfe ->
      val aisuGraphQLContext =
        (dfe.keyContextsList.first() as GraphQLContext).get<AisuGraphQLContext>(AisuGraphQLContext::class)
      GlobalScope.future {
        val groups = getGroup.getGroupsById(
          aisuGraphQLContext.userAuthSession?.castedUserId,
          ids.map { GroupId(it.value.toLong()) }
        )
        groups.map { GroupSerializer.from(it) }
      }
    }
}
