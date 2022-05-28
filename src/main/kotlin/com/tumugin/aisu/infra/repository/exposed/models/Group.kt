package com.tumugin.aisu.infra.repository.exposed.models

import com.tumugin.aisu.infra.repository.exposed.ExposedTimestampIdEntity
import com.tumugin.aisu.infra.repository.exposed.ExposedTimestampIdEntityClass
import com.tumugin.aisu.infra.repository.exposed.ExposedTimestampIdTable
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.ReferenceOption

object Groups : ExposedTimestampIdTable("groups") {
  val userId = long("user_id").references(Users.id, onDelete = ReferenceOption.SET_NULL).nullable()
  val name = varchar("name", 255)
  val status = varchar("status", 255)
}

class Group(id: EntityID<Long>) : ExposedTimestampIdEntity(id, Groups) {
  companion object : ExposedTimestampIdEntityClass<User>(Groups)

  var userId by Groups.userId
  var name by Groups.name
  var status by Groups.status
}