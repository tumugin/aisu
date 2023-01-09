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
import com.tumugin.aisu.usecase.client.idol.GetIdol
import com.tumugin.aisu.usecase.client.idol.WriteIdol
import graphql.schema.DataFetchingEnvironment

class IdolMutationService : Mutation {
  fun idol(dfe: DataFetchingEnvironment): IdolMutationServices {
    // ログインしないと使えない機能
    val aisuGraphQLContext = AisuGraphQLContext.createFromDataFetchingEnvironment(dfe)
    if (aisuGraphQLContext.userAuthSession?.userId == null) {
      throw NotAuthorizedException()
    }

    return IdolMutationServices()
  }

  class IdolMutationServices {
    private val writeIdol = WriteIdol()
    private val getIdol = GetIdol()

    suspend fun addIdol(dfe: DataFetchingEnvironment, params: AddOrUpdateIdolParams): IdolSerializer {
      val aisuGraphQLContext = AisuGraphQLContext.createFromDataFetchingEnvironment(dfe)

      val idol = writeIdol.addIdol(
        aisuGraphQLContext.userAuthSession!!.castedUserId, params.castedIdolName, params.idolStatus
      )
      val groupIds = getIdol.getGroupIdsOfIdols(
        aisuGraphQLContext.userAuthSession.castedUserId, listOf(idol.idolId)
      )[idol.idolId] ?: emptyList()

      return IdolSerializer.from(idol, groupIds)
    }

    suspend fun updateIdol(dfe: DataFetchingEnvironment, idolId: ID, params: AddOrUpdateIdolParams): IdolSerializer {
      val aisuGraphQLContext = AisuGraphQLContext.createFromDataFetchingEnvironment(dfe)
      assertValidationResult(aisuIdsValidator.validate(idolId))

      val idol = writeIdol.updateIdol(
        IdolId(idolId.value.toLong()),
        aisuGraphQLContext.userAuthSession!!.castedUserId,
        params.castedIdolName,
        params.idolStatus
      )
      val groupIds = getIdol.getGroupIdsOfIdols(
        aisuGraphQLContext.userAuthSession.castedUserId, listOf(idol.idolId)
      )[idol.idolId] ?: emptyList()

      return IdolSerializer.from(idol, groupIds)
    }

    suspend fun deleteIdol(dfe: DataFetchingEnvironment, idolId: ID): String {
      val aisuGraphQLContext = AisuGraphQLContext.createFromDataFetchingEnvironment(dfe)
      assertValidationResult(aisuIdsValidator.validate(idolId))

      writeIdol.deleteIdol(aisuGraphQLContext.userAuthSession!!.castedUserId, IdolId(idolId.value.toLong()))

      return "idol deleted."
    }
  }
}
