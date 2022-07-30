package com.tumugin.aisu.app.serializer.client

import com.expediagroup.graphql.generator.scalars.ID
import com.expediagroup.graphql.server.extensions.getValueFromDataLoader
import com.tumugin.aisu.app.graphql.dataLoader.IdolDataLoaderName
import com.tumugin.aisu.app.graphql.dataLoader.LimitedUserDataLoaderName
import com.tumugin.aisu.app.serializer.IDSerializer
import com.tumugin.aisu.domain.idol.*
import graphql.schema.DataFetchingEnvironment
import kotlinx.serialization.Serializable
import java.util.concurrent.CompletableFuture

@Serializable
class IdolSerializer(
  @Serializable(with = IDSerializer::class)
  val idolId: ID,
  @Serializable(with = IDSerializer::class)
  val userId: ID?,
  val idolName: String,
  val idolStatus: String,
  val idolCreatedAt: String,
  val idolUpdatedAt: String,
) {
  fun user(dataFetchingEnvironment: DataFetchingEnvironment): CompletableFuture<LimitedUserSerializer?> {
    return dataFetchingEnvironment.getValueFromDataLoader(LimitedUserDataLoaderName, userId)
  }

  companion object {
    fun from(idol: Idol): IdolSerializer {
      return IdolSerializer(
        idolId = ID(idol.idolId.value.toString()),
        userId = idol.userId?.let { ID(it.value.toString()) },
        idolName = idol.idolName.value,
        idolStatus = idol.idolStatus.name,
        idolCreatedAt = idol.idolCreatedAt.value.toString(),
        idolUpdatedAt = idol.idolUpdatedAt.value.toString(),
      )
    }
  }
}
