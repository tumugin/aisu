package com.tumugin.aisu.infra.repository.exposed.models

import com.tumugin.aisu.domain.group.*
import com.tumugin.aisu.domain.user.UserId
import com.tumugin.aisu.infra.repository.exposed.ExposedTimestampIdEntity
import com.tumugin.aisu.infra.repository.exposed.ExposedTimestampIdEntityClass
import com.tumugin.aisu.infra.repository.exposed.ExposedTimestampIdTable
import com.tumugin.aisu.infra.repository.exposed.models.Regulations.nullable
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.ReferenceOption

object Groups : ExposedTimestampIdTable("groups") {
  val user = reference("user_id", Users, onDelete = ReferenceOption.SET_NULL).nullable()
  val name = varchar("name", 255)
  val status = varchar("status", 255)
}

class Group(id: EntityID<Long>) : ExposedTimestampIdEntity(id, Groups) {
  companion object : ExposedTimestampIdEntityClass<Group>(Groups)

  var user by User optionalReferencedOn Groups.user
  var name by Groups.name
  var status by Groups.status
  var idols by Idol via GroupIdols

  fun toDomain(): com.tumugin.aisu.domain.group.Group {
    return Group(
      groupId = GroupId(this.id.value),
      userId = this.user?.let { UserId(it.id.value) },
      user = this.user?.toDomain(),
      idols = this.idols.map { it.toDomain() },
      groupName = GroupName(this.name),
      groupStatus = GroupStatus.valueOf(this.status),
      groupCreatedAt = GroupCreatedAt(this.createdAt),
      groupUpdatedAt = GroupUpdatedAt(this.updatedAt)
    )
  }
}
