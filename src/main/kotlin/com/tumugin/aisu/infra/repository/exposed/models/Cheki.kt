package com.tumugin.aisu.infra.repository.exposed.models

import com.tumugin.aisu.infra.repository.exposed.ExposedTimestampIdEntity
import com.tumugin.aisu.infra.repository.exposed.ExposedTimestampIdEntityClass
import com.tumugin.aisu.infra.repository.exposed.ExposedTimestampIdTable
import com.tumugin.aisu.infra.repository.exposed.datetimeWithTZ
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.ReferenceOption

object Chekis : ExposedTimestampIdTable("chekis") {
  val userId = long("user_id").references(Users.id, onDelete = ReferenceOption.CASCADE)
  val idolId = long("idol_id").references(Idols.id, onDelete = ReferenceOption.SET_NULL).nullable()
  val regulationId = long("regulation_id").references(Regulations.id, onDelete = ReferenceOption.SET_NULL).nullable()
  val quantity = integer("quantity")
  val shotAt = datetimeWithTZ("shot_at")
}

class Cheki(id: EntityID<Long>) : ExposedTimestampIdEntity(id, Groups) {
  companion object : ExposedTimestampIdEntityClass<User>(Groups)

  var userId by Chekis.userId
  var idolId by Chekis.idolId
  var regulationId by Chekis.regulationId
  var quantity by Chekis.quantity
  var shotAt by Chekis.shotAt
}
