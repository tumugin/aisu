package com.tumugin.aisu.infra.repository.exposed.models

import com.tumugin.aisu.infra.repository.exposed.ExposedTimestampIdEntity
import com.tumugin.aisu.infra.repository.exposed.ExposedTimestampIdEntityClass
import com.tumugin.aisu.infra.repository.exposed.ExposedTimestampIdTable
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.ReferenceOption

object Regulations : ExposedTimestampIdTable("regulations") {
  val groupId = long("group_id").references(Groups.id, onDelete = ReferenceOption.CASCADE)
  val group = reference("group_id", Groups)
  val userId = long("user_id").references(Users.id, onDelete = ReferenceOption.SET_NULL).nullable()
  val user = reference("user_id", Users).nullable()
  val name = varchar("name", 255)
  val comment = text("comment")
  val unitPrice = integer("unit_price")
  val status = varchar("status", 255)
}

class Regulation(id: EntityID<Long>) : ExposedTimestampIdEntity(id, Regulations) {
  companion object : ExposedTimestampIdEntityClass<Regulation>(Regulations)

  var groupId by Regulations.groupId
  val group by Group referencedOn Regulations.group
  var userId by Regulations.userId
  val user by User optionalReferencedOn Regulations.user
  var name by Regulations.name
  var comment by Regulations.comment
  var unitPrice by Regulations.unitPrice
  var status by Regulations.status
}
