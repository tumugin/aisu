package com.tumugin.aisu.app.graphql.mutation

import com.expediagroup.graphql.generator.scalars.ID
import com.expediagroup.graphql.server.operations.Mutation
import com.tumugin.aisu.app.graphql.AisuGraphQLContext
import com.tumugin.aisu.app.graphql.params.AddOrUpdateIdolParams
import com.tumugin.aisu.app.graphql.params.aisuIdsValidator
import com.tumugin.aisu.app.graphql.params.assertValidationResult
import com.tumugin.aisu.app.serializer.client.IdolSerializer
import com.tumugin.aisu.domain.exception.NotAuthorizedException
import com.tumugin.aisu.domain.idol.IdolId
import com.tumugin.aisu.usecase.client.idol.WriteIdol
import graphql.schema.DataFetchingEnvironment

class IdolMutationService : Mutation {
  fun idol(dfe: DataFetchingEnvironment): IdolMutatuonServices {
    // ログインしないと使えない機能
    val aisuGraphQLContext = dfe.graphQlContext.get<AisuGraphQLContext>(AisuGraphQLContext::class)
    if (aisuGraphQLContext.userAuthSession?.userId == null) {
      throw NotAuthorizedException()
    }

    return IdolMutatuonServices()
  }

  class IdolMutatuonServices {
    val writeIdol = WriteIdol()

    suspend fun addIdol(dfe: DataFetchingEnvironment, params: AddOrUpdateIdolParams): IdolSerializer {
      val aisuGraphQLContext = dfe.graphQlContext.get<AisuGraphQLContext>(AisuGraphQLContext::class)

      val idol = writeIdol.addIdol(
        aisuGraphQLContext.userAuthSession!!.castedUserId, params.castedIdolName, params.idolStatus
      )

      return IdolSerializer.from(idol)
    }

    suspend fun updateIdol(dfe: DataFetchingEnvironment, idolId: ID, params: AddOrUpdateIdolParams): IdolSerializer {
      val aisuGraphQLContext = dfe.graphQlContext.get<AisuGraphQLContext>(AisuGraphQLContext::class)
      assertValidationResult(aisuIdsValidator.validate(idolId))

      val idol = writeIdol.updateIdol(
        IdolId(idolId.value.toLong()),
        aisuGraphQLContext.userAuthSession!!.castedUserId,
        params.castedIdolName,
        params.idolStatus
      )

      return IdolSerializer.from(idol)
    }

    suspend fun deleteIdol(dfe: DataFetchingEnvironment, idolId: ID): String {
      val aisuGraphQLContext = dfe.graphQlContext.get<AisuGraphQLContext>(AisuGraphQLContext::class)
      assertValidationResult(aisuIdsValidator.validate(idolId))

      writeIdol.deleteIdol(aisuGraphQLContext.userAuthSession!!.castedUserId, IdolId(idolId.value.toLong()))

      return "idol deleted."
    }
  }
}
