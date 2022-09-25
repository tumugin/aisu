package com.tumugin.aisu.app.graphql.query.admin

import com.expediagroup.graphql.server.operations.Query
import com.tumugin.aisu.app.graphql.AisuGraphQLContext
import com.tumugin.aisu.app.serializer.admin.AdminUserSerializer
import com.tumugin.aisu.usecase.admin.adminUser.GetAdminUser
import graphql.schema.DataFetchingEnvironment

class AdminUserAuthQueryService : Query {
  private val getAdminUser = GetAdminUser()

  suspend fun currentAuthAdminUser(dfe: DataFetchingEnvironment): AdminUserSerializer {
    val context = AisuGraphQLContext.createFromDataFetchingEnvironment(dfe)
    val adminUser = getAdminUser.getAdminUserByAdminUserId(context.adminUserAuthSession!!.castedAdminUserId)
      ?: throw IllegalStateException("Admin user not found.")
    return AdminUserSerializer.from(adminUser)
  }
}
