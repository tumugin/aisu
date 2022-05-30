package com.tumugin.aisu.infra.repository.exposed.models

import com.tumugin.aisu.infra.repository.exposed.ExposedTimestampIdEntity
import com.tumugin.aisu.infra.repository.exposed.ExposedTimestampIdEntityClass
import com.tumugin.aisu.infra.repository.exposed.ExposedTimestampIdTable
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.ReferenceOption

object Regulations : ExposedTimestampIdTable("regulations") {
  val groupId = long("group_id").references(Groups.id, onDelete = ReferenceOption.CASCADE)
  val userId = long("user_id").references(Users.id, onDelete = ReferenceOption.SET_NULL).nullable()
  val name = varchar("name", 255)
  val comment = text("comment")
  val unitPrice = integer("unit_price")
  val status = varchar("status", 255)
}

class Regulation(id: EntityID<Long>) : ExposedTimestampIdEntity(id, Regulations) {
  companion object : ExposedTimestampIdEntityClass<Regulation>(Regulations)

  var groupId by Regulations.groupId
  var userId by Regulations.userId
  var name by Regulations.name
  var comment by Regulations.comment
  var unitPrice by Regulations.unitPrice
  var status by Regulations.status
}
