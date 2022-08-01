package com.tumugin.aisu.app.serializer.client

import com.expediagroup.graphql.generator.scalars.ID
import com.expediagroup.graphql.server.extensions.getValueFromDataLoader
import com.tumugin.aisu.app.graphql.dataLoader.IdolDataLoaderName
import com.tumugin.aisu.app.serializer.IDSerializer
import com.tumugin.aisu.domain.cheki.ChekiIdolCount
import graphql.schema.DataFetchingEnvironment
import kotlinx.serialization.Serializable
import java.util.concurrent.CompletableFuture

@Serializable
class ChekiIdolCountSerializer(
  @Serializable(with = IDSerializer::class)
  val idolId: ID,
  val chekiCount: Int
) {
  fun idol(dataFetchingEnvironment: DataFetchingEnvironment): CompletableFuture<IdolSerializer?> {
    return dataFetchingEnvironment.getValueFromDataLoader(IdolDataLoaderName, idolId)
  }

  companion object {
    fun from(chekiIdolCount: ChekiIdolCount): ChekiIdolCountSerializer {
      return ChekiIdolCountSerializer(
        ID(chekiIdolCount.idolId.value.toString()),
        chekiIdolCount.chekiCount.value
      )
    }
  }
}
