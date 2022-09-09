package com.tumugin.aisu.app.graphql.mutation

import com.expediagroup.graphql.generator.scalars.ID
import com.expediagroup.graphql.server.operations.Mutation
import com.tumugin.aisu.app.graphql.AisuGraphQLContext
import com.tumugin.aisu.app.graphql.params.AddOrUpdateRegulationParams
import com.tumugin.aisu.app.graphql.params.aisuIdsValidator
import com.tumugin.aisu.app.graphql.params.assertValidationResult
import com.tumugin.aisu.app.serializer.client.RegulationSerializer
import com.tumugin.aisu.domain.exception.NotAuthorizedException
import com.tumugin.aisu.domain.regulation.RegulationId
import com.tumugin.aisu.usecase.client.regulation.WriteRegulation
import graphql.schema.DataFetchingEnvironment

class RegulationMutationService : Mutation {
  fun regulation(dfe: DataFetchingEnvironment): RegulationMutationServices {
    // ログインしないと使えない機能
    val aisuGraphQLContext = AisuGraphQLContext.createFromDataFetchingEnvironment(dfe)
    if (aisuGraphQLContext.userAuthSession?.userId == null) {
      throw NotAuthorizedException()
    }

    return RegulationMutationServices()
  }

  class RegulationMutationServices {
    private val writeRegulation = WriteRegulation()

    suspend fun addRegulation(dfe: DataFetchingEnvironment, params: AddOrUpdateRegulationParams): RegulationSerializer {
      val aisuGraphQLContext = AisuGraphQLContext.createFromDataFetchingEnvironment(dfe)
      val regulation = writeRegulation.addRegulation(
        params.castedGroupId,
        aisuGraphQLContext.userAuthSession!!.castedUserId,
        params.castedRegulationName,
        params.castedRegulationComment,
        params.castedRegulationUnitPrice,
        params.regulationStatus
      )
      return RegulationSerializer.from(regulation)
    }

    suspend fun updateRegulation(
      dfe: DataFetchingEnvironment, regulationId: ID, params: AddOrUpdateRegulationParams
    ): RegulationSerializer {
      val aisuGraphQLContext = AisuGraphQLContext.createFromDataFetchingEnvironment(dfe)
      assertValidationResult(aisuIdsValidator.validate(regulationId))
      val regulation = writeRegulation.updateRegulation(
        RegulationId(regulationId.value.toLong()),
        aisuGraphQLContext.userAuthSession!!.castedUserId,
        params.castedRegulationName,
        params.castedRegulationComment,
        params.castedRegulationUnitPrice,
        params.regulationStatus
      )
      return RegulationSerializer.from(regulation)
    }

    suspend fun deleteRegulation(dfe: DataFetchingEnvironment, regulationId: ID): String {
      val aisuGraphQLContext = AisuGraphQLContext.createFromDataFetchingEnvironment(dfe)
      assertValidationResult(aisuIdsValidator.validate(regulationId))
      writeRegulation.deleteRegulation(
        aisuGraphQLContext.userAuthSession!!.castedUserId, RegulationId(regulationId.value.toLong())
      )

      return "regulation deleted."
    }
  }
}
