package com.tumugin.aisu.infra.repository.exposed.user

import com.tumugin.aisu.infra.repository.exposed.ExposedTimestampIdEntity
import com.tumugin.aisu.infra.repository.exposed.ExposedTimestampIdEntityClass
import com.tumugin.aisu.infra.repository.exposed.ExposedTimestampIdTable
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.javatime.datetime

object Users : ExposedTimestampIdTable() {
  val name = varchar("name", 255)
  val email = varchar("email", 255)
  val password = varchar("password", 255)
  val emailVerifiedAt = datetime("email_verified_at")
  val rememberToken = varchar("remember_token", 255)
}

class User(id: EntityID<Int>) : ExposedTimestampIdEntity(id, Users) {
  companion object : ExposedTimestampIdEntityClass<User>(Users)
}
