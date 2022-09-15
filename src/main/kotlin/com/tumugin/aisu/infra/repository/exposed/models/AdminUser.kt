package com.tumugin.aisu.infra.repository.exposed.models

import com.tumugin.aisu.domain.adminUser.*
import com.tumugin.aisu.infra.repository.exposed.ExposedTimestampIdEntity
import com.tumugin.aisu.infra.repository.exposed.ExposedTimestampIdEntityClass
import com.tumugin.aisu.infra.repository.exposed.ExposedTimestampIdTable
import org.jetbrains.exposed.dao.id.EntityID

object AdminUsers : ExposedTimestampIdTable("admin_users") {
  val name = varchar("name", 255)
  val email = varchar("email", 255)
  val password = varchar("password", 255)
}

class AdminUser(id: EntityID<Long>) : ExposedTimestampIdEntity(id, AdminUsers) {
  companion object : ExposedTimestampIdEntityClass<AdminUser>(AdminUsers)

  var name by AdminUsers.name
  var email by AdminUsers.email
  var password by AdminUsers.password

  fun toDomain(): com.tumugin.aisu.domain.adminUser.AdminUser {
    return com.tumugin.aisu.domain.adminUser.AdminUser(
      AdminUserId(this.id.value),
      AdminUserName(this.name),
      AdminUserEmail(this.email),
      AdminUserPassword(this.password),
      AdminUserCreatedAt(this.createdAt),
      AdminUserUpdatedAt(this.updatedAt)
    )
  }
}
