package com.tumugin.aisu.infra.repository.exposed.models

import com.tumugin.aisu.domain.auth0.Auth0UserId
import com.tumugin.aisu.infra.repository.exposed.ExposedTimestampIdEntity
import com.tumugin.aisu.infra.repository.exposed.ExposedTimestampIdEntityClass
import com.tumugin.aisu.infra.repository.exposed.ExposedTimestampIdTable
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.ReferenceOption

object Auth0Users : ExposedTimestampIdTable("auth0_users") {
  val user = reference("user_id", Users, onDelete = ReferenceOption.CASCADE)
  val auth0UserId = varchar("auth0_user_id", 255)
}

class Auth0User(id: EntityID<Long>) : ExposedTimestampIdEntity(id, Auth0Users) {
  companion object : ExposedTimestampIdEntityClass<Auth0User>(Auth0Users)

  var user by User referencedOn Auth0Users.user
  var auth0UserId by Auth0Users.auth0UserId

  fun toDomain(): com.tumugin.aisu.domain.auth0.Auth0User {
    return com.tumugin.aisu.domain.auth0.Auth0User(
      user = user.toDomain(), auth0UserId = Auth0UserId(auth0UserId)
    )
  }
}
