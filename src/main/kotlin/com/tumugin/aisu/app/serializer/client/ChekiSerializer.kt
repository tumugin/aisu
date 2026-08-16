@file:OptIn(kotlin.time.ExperimentalTime::class)

package com.tumugin.aisu.app.serializer.client

import com.expediagroup.graphql.generator.scalars.ID
import com.expediagroup.graphql.server.extensions.getValueFromDataLoader
import com.tumugin.aisu.app.graphql.dataLoader.IdolDataLoaderName
import com.tumugin.aisu.app.graphql.dataLoader.LimitedUserDataLoaderName
import com.tumugin.aisu.app.graphql.dataLoader.RegulationDataLoaderName
import com.tumugin.aisu.app.serializer.IDSerializer
import com.tumugin.aisu.domain.cheki.Cheki
import graphql.schema.DataFetchingEnvironment
import kotlinx.serialization.Serializable
import java.util.concurrent.CompletableFuture

@Serializable
data class ChekiSerializer(
  @Serializable(with = IDSerializer::class)
  val chekiId: ID,
  @Serializable(with = IDSerializer::class)
  val userId: ID,
  @Serializable(with = IDSerializer::class)
  val idolId: ID?,
  @Serializable(with = IDSerializer::class)
  val regulationId: ID?,
  val chekiQuantity: Int,
  val chekiShotAt: String,
  val chekiCreatedAt: String,
  val chekiUpdatedAt: String
) {
  fun idol(dataFetchingEnvironment: DataFetchingEnvironment): CompletableFuture<IdolSerializer?> {
    val targetId = idolId ?: return CompletableFuture.completedFuture(null)
    return dataFetchingEnvironment.getValueFromDataLoader(IdolDataLoaderName, targetId)
  }

  fun user(dataFetchingEnvironment: DataFetchingEnvironment): CompletableFuture<LimitedUserSerializer?> {
    return dataFetchingEnvironment.getValueFromDataLoader(LimitedUserDataLoaderName, userId)
  }

  fun regulation(dataFetchingEnvironment: DataFetchingEnvironment): CompletableFuture<RegulationSerializer?> {
    val targetId = regulationId ?: return CompletableFuture.completedFuture(null)
    return dataFetchingEnvironment.getValueFromDataLoader(RegulationDataLoaderName, targetId)
  }

  companion object {
    fun from(cheki: Cheki): ChekiSerializer {
      return ChekiSerializer(
        chekiId = ID(cheki.chekiId.value.toString()),
        userId = ID(cheki.userId.value.toString()),
        idolId = cheki.idolId?.let { ID(it.value.toString()) },
        regulationId = cheki.regulationId?.let { ID(it.value.toString()) },
        chekiQuantity = cheki.chekiQuantity.value,
        chekiShotAt = cheki.chekiShotAt.value.toString(),
        chekiCreatedAt = cheki.chekiCreatedAt.value.toString(),
        chekiUpdatedAt = cheki.chekiUpdatedAt.value.toString()
      )
    }
  }
}
