package com.tumugin.aisu.app.graphql.query

import com.expediagroup.graphql.generator.scalars.ID
import com.tumugin.aisu.app.graphql.AisuGraphQLContext
import com.tumugin.aisu.app.serializer.client.ChekiSerializer
import com.tumugin.aisu.domain.cheki.ChekiId
import com.tumugin.aisu.domain.exception.NotFoundException
import com.tumugin.aisu.usecase.client.cheki.GetCheki
import graphql.schema.DataFetchingEnvironment

class ChekiQueryService {
  private val getCheki = GetCheki()

  suspend fun getCheki(dfe: DataFetchingEnvironment, chekiId: ID): ChekiSerializer {
    val aisuGraphQLContext = dfe.graphQlContext.get<AisuGraphQLContext>(AisuGraphQLContext::class)
    return getCheki.getCheki(
      aisuGraphQLContext.userAuthSession?.castedUserId,
      ChekiId(chekiId.value.toLong())
    )?.let { ChekiSerializer.from(it) } ?: throw NotFoundException()
  }
}
