package com.tumugin.aisu.app.graphql.mutation

import com.expediagroup.graphql.generator.scalars.ID
import com.expediagroup.graphql.server.operations.Mutation
import com.tumugin.aisu.app.graphql.AisuGraphQLContext
import com.tumugin.aisu.app.graphql.params.AddOrUpdateChekiParams
import com.tumugin.aisu.app.serializer.client.ChekiSerializer
import com.tumugin.aisu.domain.cheki.ChekiId
import com.tumugin.aisu.domain.exception.NotAuthorizedException
import com.tumugin.aisu.usecase.client.cheki.WriteCheki
import graphql.schema.DataFetchingEnvironment

class ChekiMutationService : Mutation {
  fun cheki(dfe: DataFetchingEnvironment): ChekiMutationServices {
    // ログインしないと使えない機能
    val aisuGraphQLContext = dfe.graphQlContext.get<AisuGraphQLContext>(AisuGraphQLContext::class)
    if (aisuGraphQLContext.userAuthSession?.userId == null) {
      throw NotAuthorizedException()
    }

    return ChekiMutationServices()
  }

  class ChekiMutationServices {
    private val writeCheki = WriteCheki()

    suspend fun addCheki(dfe: DataFetchingEnvironment, params: AddOrUpdateChekiParams): ChekiSerializer {
      val aisuGraphQLContext = dfe.graphQlContext.get<AisuGraphQLContext>(AisuGraphQLContext::class)
      val cheki = writeCheki.addCheki(
        aisuGraphQLContext.userAuthSession!!.castedUserId,
        params.castedIdolId,
        params.castedRegulationId,
        params.castedChekiQuantity,
        params.castedChekiShotAt
      )
      return ChekiSerializer.from(cheki)
    }

    suspend fun updateCheki(
      dfe: DataFetchingEnvironment, chekiId: ID, params: AddOrUpdateChekiParams
    ): ChekiSerializer {
      val aisuGraphQLContext = dfe.graphQlContext.get<AisuGraphQLContext>(AisuGraphQLContext::class)
      val cheki = writeCheki.updateCheki(
        ChekiId(chekiId.value.toLong()),
        aisuGraphQLContext.userAuthSession!!.castedUserId,
        params.castedIdolId,
        params.castedRegulationId,
        params.castedChekiQuantity,
        params.castedChekiShotAt
      )
      return ChekiSerializer.from(cheki)
    }

    suspend fun deleteCheki(dfe: DataFetchingEnvironment, chekiId: ID): Unit? {
      val aisuGraphQLContext = dfe.graphQlContext.get<AisuGraphQLContext>(AisuGraphQLContext::class)
      writeCheki.deleteCheki(aisuGraphQLContext.userAuthSession!!.castedUserId, ChekiId(chekiId.value.toLong()))
      return null
    }
  }
}
