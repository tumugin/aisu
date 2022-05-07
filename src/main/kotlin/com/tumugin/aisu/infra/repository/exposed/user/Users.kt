package com.tumugin.aisu.infra.repository.exposed.user

import com.tumugin.aisu.infra.repository.exposed.ExposedTimestampIdTable
import org.jetbrains.exposed.sql.javatime.datetime

class Users : ExposedTimestampIdTable() {
  val name = varchar("name", 255)
  val email = varchar("email", 255)
  val password = varchar("password", 255)
  val emailVerifiedAt = datetime("email_verifed_at")
  val rememberToken = varchar("remember_token", 255)
}
