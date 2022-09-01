package com.tumugin.aisu.app.graphql.query

import com.expediagroup.graphql.generator.scalars.ID
import com.expediagroup.graphql.server.operations.Query
import com.tumugin.aisu.app.graphql.AisuGraphQLContext
import com.tumugin.aisu.app.serializer.client.GroupPaginationSerializer
import com.tumugin.aisu.app.serializer.client.GroupSerializer
import com.tumugin.aisu.domain.base.PaginatorParam
import com.tumugin.aisu.domain.exception.NotAuthorizedException
import com.tumugin.aisu.domain.group.GroupId
import com.tumugin.aisu.usecase.client.group.GetGroup
import graphql.schema.DataFetchingEnvironment
import io.ktor.server.plugins.*

class GroupQueryService : Query {
  private val getGroup = GetGroup()

  suspend fun getGroup(dfe: DataFetchingEnvironment, groupId: ID): GroupSerializer {
    val aisuGraphQLContext = AisuGraphQLContext.createFromDataFetchingEnvironment(dfe)
    return getGroup.getGroup(
      aisuGraphQLContext.userAuthSession?.castedUserId, GroupId(groupId.value.toLong())
    )?.let { GroupSerializer.from(it) } ?: throw NotFoundException()
  }

  fun currentUserGroups(dfe: DataFetchingEnvironment): CurrentUserGroups {
    // 全てログインされたユーザに関するデータのため、未ログインの場合はここでエラーにしてしまう
    val aisuGraphQLContext = AisuGraphQLContext.createFromDataFetchingEnvironment(dfe)
    if (aisuGraphQLContext.userAuthSession?.userId == null) {
      throw NotAuthorizedException()
    }

    return CurrentUserGroups()
  }

  class CurrentUserGroups() {
    private val getGroup = GetGroup()

    suspend fun getGroupsCreatedByUser(dfe: DataFetchingEnvironment, page: Int): GroupPaginationSerializer {
      val aisuGraphQLContext = AisuGraphQLContext.createFromDataFetchingEnvironment(dfe)
      val groups = getGroup.getAllUserCreatedGroups(
        aisuGraphQLContext.userAuthSession!!.castedUserId, PaginatorParam(page.toLong(), 20)
      )

      return GroupPaginationSerializer(page, groups.pages.toInt(), groups.result.map { GroupSerializer.from(it) })
    }
  }
}
