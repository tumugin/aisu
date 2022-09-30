package com.tumugin.aisu.app.graphql.mutation.admin

import com.expediagroup.graphql.generator.scalars.ID
import com.expediagroup.graphql.server.operations.Mutation
import com.tumugin.aisu.app.graphql.params.admin.AddAdminUserParams
import com.tumugin.aisu.app.graphql.params.admin.UpdateAdminUserParams
import com.tumugin.aisu.app.graphql.params.admin.UpdateAdminUserPasswordParams
import com.tumugin.aisu.app.serializer.admin.AdminUserSerializer
import com.tumugin.aisu.domain.adminUser.AdminUserId
import com.tumugin.aisu.usecase.admin.adminUser.CreateAdminUser
import com.tumugin.aisu.usecase.admin.adminUser.UpdateAdminUser

class AdminUserMutationService : Mutation {
  private val createAdminUser = CreateAdminUser()
  private val updateAdminUser = UpdateAdminUser()

  suspend fun addAdminUser(params: AddAdminUserParams): AdminUserSerializer {
    val adminUser = createAdminUser.createAdminUser(
      params.castedAdminUserName, params.castedAdminUserEmail, params.castedAdminUserRawPassword.toHashedPassword()
    )

    return AdminUserSerializer.from(adminUser)
  }

  suspend fun updateAdminUser(adminUserId: ID, params: UpdateAdminUserParams): AdminUserSerializer {
    val adminUser = updateAdminUser.updateAdminUser(
      AdminUserId(adminUserId.value.toLong()),
      params.castedAdminUserName,
      params.castedAdminUserEmail,
    )

    return AdminUserSerializer.from(adminUser)
  }

  suspend fun updateAdminUserPassword(adminUserId: ID, params: UpdateAdminUserPasswordParams): AdminUserSerializer {
    val adminUser = updateAdminUser.updateAdminUserPassword(
      AdminUserId(adminUserId.value.toLong()),
      params.castedAdminUserRawPassword,
    )

    return AdminUserSerializer.from(adminUser)
  }
}
