package com.tumugin.aisu.infra.repository.exposed.models.user

import com.tumugin.aisu.infra.repository.exposed.ExposedTimestampIdEntity
import com.tumugin.aisu.infra.repository.exposed.ExposedTimestampIdEntityClass
import com.tumugin.aisu.infra.repository.exposed.ExposedTimestampIdTable
import com.tumugin.aisu.infra.repository.exposed.datetimeWithTZ
import org.jetbrains.exposed.dao.id.EntityID

object Users : ExposedTimestampIdTable() {
  val name = varchar("name", 255)
  val email = varchar("email", 255).nullable()
  val password = varchar("password", 255).nullable()
  val emailVerifiedAt = datetimeWithTZ("email_verified_at").nullable()
  val userForceLogoutGeneration = integer("user_force_logout_generation").default(0)
}

class User(id: EntityID<Int>) : ExposedTimestampIdEntity(id, Users) {
  companion object : ExposedTimestampIdEntityClass<User>(Users)

  val name by Users.name
  val email by Users.email
  val password by Users.password
  val emailVerifiedAt by Users.emailVerifiedAt
  val userForceLogoutGeneration by Users.userForceLogoutGeneration
}
