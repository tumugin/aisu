package com.tumugin.aisu.app.serializer.admin

import com.expediagroup.graphql.generator.scalars.ID
import com.tumugin.aisu.app.serializer.IDSerializer
import com.tumugin.aisu.domain.adminUser.AdminUser
import kotlinx.serialization.Serializable

@Serializable
class AdminUserSerializer(
  @Serializable(with = IDSerializer::class) val adminUserId: ID,
  val adminUserName: String,
  val adminUserEmail: String,
  val adminUserCreatedAt: String,
  val adminUserUpdatedAt: String
) {
  companion object {
    fun from(adminUser: AdminUser): AdminUserSerializer {
      return AdminUserSerializer(
        ID(adminUser.adminUserId.value.toString()),
        adminUser.adminUserName.value,
        adminUser.adminUserEmail.value,
        adminUser.adminUserCreatedAt.value.toString(),
        adminUser.adminUserUpdatedAt.value.toString()
      )
    }
  }
}
