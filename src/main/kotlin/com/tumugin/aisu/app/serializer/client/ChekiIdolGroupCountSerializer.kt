package com.tumugin.aisu.app.serializer.client

import com.expediagroup.graphql.generator.scalars.ID
import com.expediagroup.graphql.server.extensions.getValueFromDataLoader
import com.tumugin.aisu.app.graphql.dataLoader.GroupDataLoaderName
import com.tumugin.aisu.app.graphql.dataLoader.IdolDataLoaderName
import com.tumugin.aisu.app.serializer.IDSerializer
import com.tumugin.aisu.domain.cheki.ChekiIdolGroupCount
import graphql.schema.DataFetchingEnvironment
import kotlinx.serialization.Serializable
import java.util.concurrent.CompletableFuture

@Serializable
class ChekiIdolGroupCountSerializer(
  @Serializable(with = IDSerializer::class)
  val idolId: ID,
  val chekiCount: Int,
  @Serializable(with = IDSerializer::class)
  val groupId: ID?,
  val totalPrice: Int
) {
  fun idol(dataFetchingEnvironment: DataFetchingEnvironment): CompletableFuture<IdolSerializer?> {
    return dataFetchingEnvironment.getValueFromDataLoader(IdolDataLoaderName, idolId)
  }

  fun group(dataFetchingEnvironment: DataFetchingEnvironment): CompletableFuture<GroupSerializer?> {
    val groupId = this.groupId ?: return CompletableFuture.completedFuture(null)
    return dataFetchingEnvironment.getValueFromDataLoader(GroupDataLoaderName, groupId)
  }

  companion object {
    fun from(chekiIdolCount: ChekiIdolGroupCount): ChekiIdolGroupCountSerializer {
      return ChekiIdolGroupCountSerializer(
        ID(chekiIdolCount.idolId.value.toString()),
        chekiIdolCount.chekiCount.value,
        chekiIdolCount.groupId?.let { ID(it.value.toString()) },
        chekiIdolCount.totalPrice.value
      )
    }
  }
}
