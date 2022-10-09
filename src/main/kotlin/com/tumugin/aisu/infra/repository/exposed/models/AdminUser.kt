package com.tumugin.aisu.infra.repository.exposed.models

import com.tumugin.aisu.domain.adminUser.*
import com.tumugin.aisu.infra.repository.exposed.ExposedTimestampIdEntity
import com.tumugin.aisu.infra.repository.exposed.ExposedTimestampIdEntityClass
import com.tumugin.aisu.infra.repository.exposed.ExposedTimestampIdTable
import org.jetbrains.exposed.dao.id.EntityID

object AdminUsers : ExposedTimestampIdTable("admin_users") {
  val name = varchar("name", 255)
  val email = varchar("email", 255)
  val password = varchar("password", 255).nullable()
  val forceLogoutGeneration = integer("force_logout_generation").default(0)
}

class AdminUser(id: EntityID<Long>) : ExposedTimestampIdEntity(id, AdminUsers) {
  companion object : ExposedTimestampIdEntityClass<AdminUser>(AdminUsers)

  var name by AdminUsers.name
  var email by AdminUsers.email
  var password by AdminUsers.password
  var forceLogoutGeneration by AdminUsers.forceLogoutGeneration

  fun toDomain(): com.tumugin.aisu.domain.adminUser.AdminUser {
    return com.tumugin.aisu.domain.adminUser.AdminUser(
      AdminUserId(this.id.value),
      AdminUserName(this.name),
      AdminUserEmail(this.email),
      this.password?.let { AdminUserPassword(it) },
      AdminUserForceLogoutGeneration(this.forceLogoutGeneration),
      AdminUserCreatedAt(this.createdAt),
      AdminUserUpdatedAt(this.updatedAt)
    )
  }
}
