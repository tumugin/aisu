package com.tumugin.aisu.app.graphql.mutation.admin

import com.expediagroup.graphql.server.operations.Mutation
import com.tumugin.aisu.app.graphql.params.admin.AddAdminUserParams
import com.tumugin.aisu.app.serializer.admin.AdminUserSerializer
import com.tumugin.aisu.usecase.admin.adminUser.CreateAdminUser

class AdminUserMutationService : Mutation {
  private val createAdminUser = CreateAdminUser()

  suspend fun addAdminUser(params: AddAdminUserParams): AdminUserSerializer {
    val adminUser = createAdminUser.createAdminUser(
      params.castedAdminUserName, params.castedAdminUserEmail, params.castedAdminUserRawPassword.toHashedPassword()
    )

    return AdminUserSerializer.from(adminUser)
  }
}
