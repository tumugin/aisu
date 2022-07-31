package com.tumugin.aisu.app.graphql.query

import com.expediagroup.graphql.generator.scalars.ID
import com.expediagroup.graphql.server.operations.Query
import com.tumugin.aisu.app.graphql.AisuGraphQLContext
import com.tumugin.aisu.app.graphql.params.GetChekiMonthIdolCountParams
import com.tumugin.aisu.app.graphql.params.GetUserChekiIdolCountParams
import com.tumugin.aisu.app.graphql.params.GetUserChekisParams
import com.tumugin.aisu.app.serializer.client.ChekiIdolCountSerializer
import com.tumugin.aisu.app.serializer.client.ChekiMonthIdolCountSerializer
import com.tumugin.aisu.app.serializer.client.ChekiSerializer
import com.tumugin.aisu.domain.cheki.ChekiId
import com.tumugin.aisu.domain.exception.NotAuthorizedException
import com.tumugin.aisu.domain.exception.NotFoundException
import com.tumugin.aisu.usecase.client.cheki.GetCheki
import graphql.schema.DataFetchingEnvironment

class ChekiQueryService : Query {
  private val getCheki = GetCheki()

  suspend fun getCheki(dfe: DataFetchingEnvironment, chekiId: ID): ChekiSerializer {
    val aisuGraphQLContext = dfe.graphQlContext.get<AisuGraphQLContext>(AisuGraphQLContext::class)
    return getCheki.getCheki(
      aisuGraphQLContext.userAuthSession?.castedUserId,
      ChekiId(chekiId.value.toLong())
    )?.let { ChekiSerializer.from(it) } ?: throw NotFoundException()
  }

  fun currentUserChekis(dfe: DataFetchingEnvironment): UserChekis {
    // 全てログインされたユーザに関するデータのため、未ログインの場合はここでエラーにしてしまう
    val aisuGraphQLContext = dfe.graphQlContext.get<AisuGraphQLContext>(AisuGraphQLContext::class)
    if (aisuGraphQLContext.userAuthSession?.userId == null) {
      throw NotAuthorizedException()
    }

    return UserChekis()
  }

  class UserChekis : Query {
    private val getCheki = GetCheki()

    suspend fun getUserChekis(dfe: DataFetchingEnvironment, params: GetUserChekisParams): List<ChekiSerializer> {
      val aisuGraphQLContext = dfe.graphQlContext.get<AisuGraphQLContext>(AisuGraphQLContext::class)
      val userId = aisuGraphQLContext.userAuthSession?.castedUserId ?: throw NotAuthorizedException()
      return if (params.idolIdCasted != null) {
        getCheki.getChekiByUserIdAndShotDateTimeRangeAndIdolId(
          userId,
          params.idolIdCasted!!,
          params.chekiShotAtStartCasted,
          params.chekiShotAtEndCasted
        ).map { ChekiSerializer.from(it) }
      } else {
        getCheki.getChekiByUserIdAndShotDateTimeRange(
          userId,
          params.chekiShotAtStartCasted,
          params.chekiShotAtEndCasted
        ).map { ChekiSerializer.from(it) }
      }
    }

    suspend fun getUserChekiIdolCount(
      dfe: DataFetchingEnvironment,
      params: GetUserChekiIdolCountParams
    ): List<ChekiIdolCountSerializer> {
      val aisuGraphQLContext = dfe.graphQlContext.get<AisuGraphQLContext>(AisuGraphQLContext::class)
      val userId = aisuGraphQLContext.userAuthSession?.castedUserId ?: throw NotAuthorizedException()
      return getCheki.getChekiIdolCountByUserId(userId, params.chekiShotAtStartCasted, params.chekiShotAtEndCasted)
        .map { ChekiIdolCountSerializer.from(it) }
    }

    suspend fun getChekiMonthIdolCount(
      dfe: DataFetchingEnvironment,
      params: GetChekiMonthIdolCountParams
    ): List<ChekiMonthIdolCountSerializer> {
      val aisuGraphQLContext = dfe.graphQlContext.get<AisuGraphQLContext>(AisuGraphQLContext::class)
      val userId = aisuGraphQLContext.userAuthSession?.castedUserId ?: throw NotAuthorizedException()
      return getCheki.getChekiMonthIdolCountByUserIdAndIdol(
        userId,
        params.castedBaseTimezone
      ).map { ChekiMonthIdolCountSerializer.from(it) }
    }
  }
}
