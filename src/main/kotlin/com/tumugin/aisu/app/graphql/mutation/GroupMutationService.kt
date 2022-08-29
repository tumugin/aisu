package com.tumugin.aisu.app.graphql.mutation

import com.expediagroup.graphql.generator.scalars.ID
import com.expediagroup.graphql.server.operations.Mutation
import com.tumugin.aisu.app.graphql.AisuGraphQLContext
import com.tumugin.aisu.app.graphql.params.AddOrUpdateGroupParams
import com.tumugin.aisu.app.serializer.client.GroupSerializer
import com.tumugin.aisu.domain.exception.NotAuthorizedException
import com.tumugin.aisu.domain.group.GroupId
import com.tumugin.aisu.domain.idol.IdolId
import com.tumugin.aisu.usecase.client.group.GetGroup
import com.tumugin.aisu.usecase.client.group.WriteGroup
import com.tumugin.aisu.usecase.client.idol.GetIdol
import graphql.schema.DataFetchingEnvironment
import io.ktor.server.plugins.*

class GroupMutationService : Mutation {
  fun group(dfe: DataFetchingEnvironment): GroupMutationServices {
    // ログインしないと使えない機能
    val aisuGraphQLContext = dfe.graphQlContext.get<AisuGraphQLContext>(AisuGraphQLContext::class)
    if (aisuGraphQLContext.userAuthSession?.userId == null) {
      throw NotAuthorizedException()
    }

    return GroupMutationServices()
  }

  class GroupMutationServices {
    private val writeGroup = WriteGroup()
    private val getGroup = GetGroup()
    private val getIdol = GetIdol()

    suspend fun addGroup(dfe: DataFetchingEnvironment, params: AddOrUpdateGroupParams): GroupSerializer {
      val aisuGraphQLContext = dfe.graphQlContext.get<AisuGraphQLContext>(AisuGraphQLContext::class)

      val group = writeGroup.addGroup(
        aisuGraphQLContext.userAuthSession!!.castedUserId, params.castedGroupName, params.groupStatus
      )

      return GroupSerializer.from(group)
    }

    suspend fun updateGroup(
      dfe: DataFetchingEnvironment, groupId: ID, params: AddOrUpdateGroupParams
    ): GroupSerializer {
      val aisuGraphQLContext = dfe.graphQlContext.get<AisuGraphQLContext>(AisuGraphQLContext::class)
      val targetGroup =
        getGroup.getGroup(aisuGraphQLContext.userAuthSession!!.castedUserId, GroupId(groupId.value.toLong()))
          ?: throw NotFoundException()

      val group = writeGroup.updateGroup(
        aisuGraphQLContext.userAuthSession.castedUserId, targetGroup, params.castedGroupName, params.groupStatus
      )

      return GroupSerializer.from(group)
    }

    suspend fun deleteGroup(dfe: DataFetchingEnvironment, groupId: ID): String {
      val aisuGraphQLContext = dfe.graphQlContext.get<AisuGraphQLContext>(AisuGraphQLContext::class)
      val targetGroup = getGroup.getGroup(
        aisuGraphQLContext.userAuthSession!!.castedUserId, GroupId(groupId.value.toLong())
      ) ?: throw NotFoundException()

      writeGroup.deleteGroup(aisuGraphQLContext.userAuthSession.castedUserId, targetGroup)

      return "group deleted."
    }

    suspend fun addIdolToGroup(dfe: DataFetchingEnvironment, groupId: ID, idolId: ID): String {
      val aisuGraphQLContext = dfe.graphQlContext.get<AisuGraphQLContext>(AisuGraphQLContext::class)
      val targetGroup = getGroup.getGroup(
        aisuGraphQLContext.userAuthSession!!.castedUserId, GroupId(groupId.value.toLong())
      ) ?: throw NotFoundException()
      val targetIdol = getIdol.getIdol(aisuGraphQLContext.userAuthSession.castedUserId, IdolId(idolId.value.toLong()))
        ?: throw NotFoundException()

      writeGroup.addIdolToGroup(aisuGraphQLContext.userAuthSession.castedUserId, targetGroup, targetIdol)
      return "idol added to group."
    }

    suspend fun removeIdolFromGroup(dfe: DataFetchingEnvironment, groupId: ID, idolId: ID): String {
      val aisuGraphQLContext = dfe.graphQlContext.get<AisuGraphQLContext>(AisuGraphQLContext::class)
      val targetGroup = getGroup.getGroup(
        aisuGraphQLContext.userAuthSession!!.castedUserId, GroupId(groupId.value.toLong())
      ) ?: throw NotFoundException()
      val targetIdol = getIdol.getIdol(aisuGraphQLContext.userAuthSession.castedUserId, IdolId(idolId.value.toLong()))
        ?: throw NotFoundException()

      writeGroup.removeIdolFromGroup(aisuGraphQLContext.userAuthSession.castedUserId, targetGroup, targetIdol)
      return "idol removed from group."
    }
  }
}
