package com.tumugin.aisu.infra.repository.exposed.models

import com.tumugin.aisu.infra.repository.exposed.ExposedTimestampIdEntity
import com.tumugin.aisu.infra.repository.exposed.ExposedTimestampIdEntityClass
import com.tumugin.aisu.infra.repository.exposed.ExposedTimestampIdTable
import com.tumugin.aisu.infra.repository.exposed.models.Regulations.nullable
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.ReferenceOption

object FavoriteGroups : ExposedTimestampIdTable("favorite_groups") {
  val userId = long("user_id").references(Users.id, onDelete = ReferenceOption.CASCADE)
  val user = reference("user_id", Users)
  val groupId = long("group_id").references(Groups.id, onDelete = ReferenceOption.CASCADE)
  val group = reference("group_id", Groups)
}

class FavoriteGroup(id: EntityID<Long>) : ExposedTimestampIdEntity(id, FavoriteGroups) {
  companion object : ExposedTimestampIdEntityClass<FavoriteGroup>(FavoriteGroups)

  var userId by FavoriteGroups.userId
  val user by User referencedOn FavoriteGroups.user
  var groupId by FavoriteGroups.groupId
  val group by Group referencedOn FavoriteGroups.group
}
