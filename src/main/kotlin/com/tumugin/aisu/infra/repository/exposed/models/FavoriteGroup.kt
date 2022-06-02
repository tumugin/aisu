package com.tumugin.aisu.infra.repository.exposed.models

import com.tumugin.aisu.domain.favoritegroup.FavoriteGroupId
import com.tumugin.aisu.domain.group.GroupId
import com.tumugin.aisu.domain.user.UserId
import com.tumugin.aisu.infra.repository.exposed.ExposedTimestampIdEntity
import com.tumugin.aisu.infra.repository.exposed.ExposedTimestampIdEntityClass
import com.tumugin.aisu.infra.repository.exposed.ExposedTimestampIdTable
import com.tumugin.aisu.infra.repository.exposed.models.Regulations.nullable
import com.tumugin.aisu.infra.repository.exposed.repository.GroupRepositoryImpl
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.ReferenceOption

object FavoriteGroups : ExposedTimestampIdTable("favorite_groups") {
  val user = reference("user_id", Users, onDelete = ReferenceOption.CASCADE)
  val group = reference("group_id", Groups, onDelete = ReferenceOption.CASCADE)
}

class FavoriteGroup(id: EntityID<Long>) : ExposedTimestampIdEntity(id, FavoriteGroups) {
  companion object : ExposedTimestampIdEntityClass<FavoriteGroup>(FavoriteGroups)

  var user by User referencedOn FavoriteGroups.user
  var group by Group referencedOn FavoriteGroups.group

  fun toDomain(): com.tumugin.aisu.domain.favoritegroup.FavoriteGroup {
    return com.tumugin.aisu.domain.favoritegroup.FavoriteGroup(
      favoriteGroupId = FavoriteGroupId(this.id.value),
      userId = UserId(this.user.id.value),
      user = this.user.toDomain(),
      groupId = GroupId(this.group.id.value),
      group = this.group.toDomain()
    )
  }
}
