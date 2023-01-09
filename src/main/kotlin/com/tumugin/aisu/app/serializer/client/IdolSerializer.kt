package com.tumugin.aisu.app.serializer.client

import com.expediagroup.graphql.generator.scalars.ID
import com.expediagroup.graphql.server.extensions.getValueFromDataLoader
import com.tumugin.aisu.app.graphql.dataLoader.GroupDataLoaderName
import com.tumugin.aisu.app.graphql.dataLoader.LimitedUserDataLoaderName
import com.tumugin.aisu.domain.group.GroupId
import com.tumugin.aisu.domain.idol.*
import graphql.schema.DataFetchingEnvironment
import java.util.concurrent.CompletableFuture

class IdolSerializer(
  val idolId: ID,
  val userId: ID?,
  val idolName: String,
  val idolStatus: IdolStatus,
  val idolCreatedAt: String,
  val idolUpdatedAt: String,
  val groupIds: List<ID>
) {
  fun user(dataFetchingEnvironment: DataFetchingEnvironment): CompletableFuture<LimitedUserSerializer?> {
    return dataFetchingEnvironment.getValueFromDataLoader(LimitedUserDataLoaderName, userId)
  }

  fun groups(dataFetchingEnvironment: DataFetchingEnvironment): CompletableFuture<List<GroupSerializer>> {
    return dataFetchingEnvironment.getValueFromDataLoader(GroupDataLoaderName, groupIds)
  }

  companion object {
    fun from(idol: Idol, groupIds: List<GroupId>): IdolSerializer {
      return IdolSerializer(
        idolId = ID(idol.idolId.value.toString()),
        userId = idol.userId?.let { ID(it.value.toString()) },
        idolName = idol.idolName.value,
        idolStatus = idol.idolStatus,
        idolCreatedAt = idol.idolCreatedAt.value.toString(),
        idolUpdatedAt = idol.idolUpdatedAt.value.toString(),
        groupIds = groupIds.map { ID(it.value.toString()) }
      )
    }
  }
}
