package com.tumugin.aisu.infra.repository.exposed.models

import com.tumugin.aisu.domain.user.*
import com.tumugin.aisu.infra.repository.exposed.ExposedTimestampIdEntity
import com.tumugin.aisu.infra.repository.exposed.ExposedTimestampIdEntityClass
import com.tumugin.aisu.infra.repository.exposed.ExposedTimestampIdTable
import com.tumugin.aisu.infra.repository.exposed.datetimeWithTZ
import org.jetbrains.exposed.dao.id.EntityID

object Users : ExposedTimestampIdTable("users") {
  val name = varchar("name", 255)
  val email = varchar("email", 255).nullable()
  val password = varchar("password", 255).nullable()
  val emailVerifiedAt = datetimeWithTZ("email_verified_at").nullable()
  val forceLogoutGeneration = integer("force_logout_generation").default(0)
}

class User(id: EntityID<Long>) : ExposedTimestampIdEntity(id, Users) {
  companion object : ExposedTimestampIdEntityClass<User>(Users)

  var name by Users.name
  var email by Users.email
  var password by Users.password
  var emailVerifiedAt by Users.emailVerifiedAt
  var forceLogoutGeneration by Users.forceLogoutGeneration

  fun toDomain(): com.tumugin.aisu.domain.user.User {
    return User(
      UserId(this.id.value),
      UserName(this.name),
      this.email?.let { UserEmail(it) },
      this.password?.let { UserPassword(it) },
      this.emailVerifiedAt?.let { UserEmailVerifiedAt(it) },
      UserForceLogoutGeneration(this.forceLogoutGeneration),
      UserCreatedAt(this.createdAt),
      UserUpdatedAt(this.updatedAt)
    )
  }
}
