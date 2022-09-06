package com.tumugin.aisu.app.graphql.query

import com.expediagroup.graphql.generator.scalars.ID
import com.expediagroup.graphql.server.operations.Query
import com.tumugin.aisu.app.graphql.AisuGraphQLContext
import com.tumugin.aisu.app.serializer.client.RegulationSerializer
import com.tumugin.aisu.domain.regulation.RegulationId
import com.tumugin.aisu.usecase.client.regulation.GetRegulation
import graphql.schema.DataFetchingEnvironment
import io.ktor.server.plugins.*

class RegulationQueryService : Query {
  private val getRegulation = GetRegulation()

  suspend fun getRegulation(dfe: DataFetchingEnvironment, regulationId: ID): RegulationSerializer {
    val aisuGraphQLContext = AisuGraphQLContext.createFromDataFetchingEnvironment(dfe)
    val regulation = getRegulation.getRegulation(
      aisuGraphQLContext.userAuthSession?.castedUserId, RegulationId(regulationId.value.toLong())
    ) ?: throw NotFoundException()

    return RegulationSerializer.from(regulation)
  }
}
