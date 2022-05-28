package com.tumugin.aisu.infra.repository.exposed.models

import com.tumugin.aisu.infra.repository.exposed.ExposedTimestampIdEntity
import com.tumugin.aisu.infra.repository.exposed.ExposedTimestampIdEntityClass
import com.tumugin.aisu.infra.repository.exposed.ExposedTimestampIdTable
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.ReferenceOption

object Idols : ExposedTimestampIdTable("idols") {
  val groupId = long("group_id").references(Groups.id, onDelete = ReferenceOption.SET_NULL).nullable()
  val userId = long("user_id").references(Users.id, onDelete = ReferenceOption.SET_NULL).nullable()
  val name = varchar("name", 255)
  val status = varchar("status", 255)
}

class Idol(id: EntityID<Long>) : ExposedTimestampIdEntity(id, Groups) {
  companion object : ExposedTimestampIdEntityClass<User>(Groups)

  var groupId by Idols.groupId
  var userId by Idols.userId
  var name by Idols.name
  var status by Idols.status
}