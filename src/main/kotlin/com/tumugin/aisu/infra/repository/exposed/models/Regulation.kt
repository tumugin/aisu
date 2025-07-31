package com.tumugin.aisu.infra.repository.exposed.models

import com.tumugin.aisu.domain.group.GroupId
import com.tumugin.aisu.domain.regulation.*
import com.tumugin.aisu.domain.user.UserId
import com.tumugin.aisu.infra.repository.exposed.ExposedTimestampIdEntity
import com.tumugin.aisu.infra.repository.exposed.ExposedTimestampIdEntityClass
import com.tumugin.aisu.infra.repository.exposed.ExposedTimestampIdTable
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.ReferenceOption
import kotlin.time.toKotlinInstant

object Regulations : ExposedTimestampIdTable("regulations") {
  val group = reference("group_id", Groups, onDelete = ReferenceOption.CASCADE)
  val user = reference("user_id", Users, onDelete = ReferenceOption.SET_NULL).nullable()
  val name = varchar("name", 255)
  val comment = text("comment")
  val unitPrice = integer("unit_price")
  val status = varchar("status", 255)
}

class Regulation(id: EntityID<Long>) : ExposedTimestampIdEntity(id, Regulations) {
  companion object : ExposedTimestampIdEntityClass<Regulation>(Regulations)

  var group by Group referencedOn Regulations.group
  var user by User optionalReferencedOn Regulations.user
  var name by Regulations.name
  var comment by Regulations.comment
  var unitPrice by Regulations.unitPrice
  var status by Regulations.status

  fun toDomain(): com.tumugin.aisu.domain.regulation.Regulation {
    return Regulation(
      regulationId = RegulationId(this.id.value),
      groupId = GroupId(this.group.id.value),
      group = this.group.toDomain(),
      userId = this.user?.id?.value?.let { UserId(it) },
      regulationName = RegulationName(this.name),
      regulationComment = RegulationComment(this.comment),
      regulationUnitPrice = RegulationUnitPrice(this.unitPrice),
      regulationStatus = RegulationStatus.valueOf(this.status),
      regulationCreatedAt = RegulationCreatedAt(this.createdAt.toInstant().toKotlinInstant()),
      regulationUpdatedAt = RegulationUpdatedAt(this.updatedAt.toInstant().toKotlinInstant())
    )
  }
}
