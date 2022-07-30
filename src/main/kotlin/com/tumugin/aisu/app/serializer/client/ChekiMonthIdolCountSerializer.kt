package com.tumugin.aisu.app.serializer.client

import com.expediagroup.graphql.generator.scalars.ID
import com.expediagroup.graphql.server.extensions.getValueFromDataLoader
import com.tumugin.aisu.app.graphql.dataLoader.IdolDataLoaderName
import com.tumugin.aisu.app.serializer.IDSerializer
import com.tumugin.aisu.domain.cheki.ChekiMonthIdolCount
import graphql.schema.DataFetchingEnvironment
import kotlinx.serialization.Serializable
import java.util.concurrent.CompletableFuture

@Serializable
class ChekiMonthIdolCountSerializer(
  @Serializable(with = IDSerializer::class)
  val idolId: ID,
  val chekiCount: Int,
  val chekiShotAtMonth: ChekiShotAtMonthSerializer
) {
  fun idol(dataFetchingEnvironment: DataFetchingEnvironment): CompletableFuture<IdolSerializer?> {
    return dataFetchingEnvironment.getValueFromDataLoader(IdolDataLoaderName, idolId)
  }

  companion object {
    fun from(chekiMonthIdolCount: ChekiMonthIdolCount): ChekiMonthIdolCountSerializer {
      return ChekiMonthIdolCountSerializer(
        ID(chekiMonthIdolCount.idolId.toString()),
        chekiMonthIdolCount.chekiCount.value,
        ChekiShotAtMonthSerializer.from(chekiMonthIdolCount.chekiShotAtMonth)
      )
    }
  }
}
