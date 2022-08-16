package com.tumugin.aisu.app.graphql.query

import com.expediagroup.graphql.generator.scalars.ID
import com.expediagroup.graphql.server.operations.Query
import com.tumugin.aisu.app.graphql.AisuGraphQLContext
import com.tumugin.aisu.app.graphql.params.aisuIdsValidator
import com.tumugin.aisu.app.graphql.params.assertValidationResult
import com.tumugin.aisu.app.serializer.PaginationSerializer
import com.tumugin.aisu.app.serializer.client.IdolPaginationSerializer
import com.tumugin.aisu.app.serializer.client.IdolSerializer
import com.tumugin.aisu.domain.base.PaginatorParam
import com.tumugin.aisu.domain.exception.NotAuthorizedException
import com.tumugin.aisu.domain.idol.IdolId
import com.tumugin.aisu.usecase.client.idol.GetIdol
import graphql.schema.DataFetchingEnvironment
import io.ktor.server.plugins.*

class IdolQueryService : Query {
  private val getIdol = GetIdol()

  suspend fun getIdol(dfe: DataFetchingEnvironment, idolId: ID): IdolSerializer {
    assertValidationResult(aisuIdsValidator.validate(idolId))
    val aisuGraphQLContext = dfe.graphQlContext.get<AisuGraphQLContext>(AisuGraphQLContext::class)
    val idol = getIdol.getIdol(
      aisuGraphQLContext?.userAuthSession?.castedUserId, IdolId(idolId.value.toLong())
    ) ?: throw NotFoundException()
    return IdolSerializer.from(idol)
  }

  suspend fun getAllIdols(page: Int): IdolPaginationSerializer {
    val idols = getIdol.getAllPublicIdols(PaginatorParam(page.toLong(), 50))
    return IdolPaginationSerializer(page, idols.pages.toInt(), idols.result.map { IdolSerializer.from(it) })
  }

  fun currentUserIdols(dfe: DataFetchingEnvironment): CurrentUserIdols {
    // 全てログインされたユーザに関するデータのため、未ログインの場合はここでエラーにしてしまう
    val aisuGraphQLContext = dfe.graphQlContext.get<AisuGraphQLContext>(AisuGraphQLContext::class)
    if (aisuGraphQLContext.userAuthSession?.userId == null) {
      throw NotAuthorizedException()
    }

    return CurrentUserIdols()
  }

  class CurrentUserIdols : Query {
    private val getIdol = GetIdol()

    suspend fun getIdolsCreatedByUser(
      dfe: DataFetchingEnvironment, page: Int
    ): IdolPaginationSerializer {
      val aisuGraphQLContext = dfe.graphQlContext.get<AisuGraphQLContext>(AisuGraphQLContext::class)
      val idols = getIdol.getAllUserCreatedIdols(
        aisuGraphQLContext.userAuthSession!!.castedUserId, PaginatorParam(page.toLong(), 50)
      )
      return IdolPaginationSerializer(page, idols.pages.toInt(), idols.result.map { IdolSerializer.from(it) })
    }
  }
}
