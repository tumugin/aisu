package com.tumugin.aisu.infra.repository.exposed.models

import com.tumugin.aisu.domain.group.GroupId
import com.tumugin.aisu.domain.idol.*
import com.tumugin.aisu.domain.user.UserId
import com.tumugin.aisu.infra.repository.exposed.ExposedTimestampIdEntity
import com.tumugin.aisu.infra.repository.exposed.ExposedTimestampIdEntityClass
import com.tumugin.aisu.infra.repository.exposed.ExposedTimestampIdTable
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.ReferenceOption

object Idols : ExposedTimestampIdTable("idols") {
  val group = reference("group_id", Groups, onDelete = ReferenceOption.SET_NULL).nullable()
  val user = reference("user_id", Users, onDelete = ReferenceOption.SET_NULL).nullable()
  val name = varchar("name", 255)
  val status = varchar("status", 255)
}

class Idol(id: EntityID<Long>) : ExposedTimestampIdEntity(id, Idols) {
  companion object : ExposedTimestampIdEntityClass<Idol>(Idols)

  var group by Group optionalReferencedOn Idols.group
  var user by User optionalReferencedOn Idols.user
  var name by Idols.name
  var status by Idols.status

  fun toDomain(): com.tumugin.aisu.domain.idol.Idol {
    return Idol(
      idolId = IdolId(this.id.value),
      groupId = this.group?.id?.value?.let { GroupId(it) },
      group = this.group?.toDomain(),
      userId = this.user?.id?.value?.let { UserId(it) },
      user = this.user?.toDomain(),
      idolName = IdolName(this.name),
      idolStatus = IdolStatus.valueOf(this.status),
      idolCreatedAt = IdolCreatedAt(this.createdAt),
      idolUpdatedAt = IdolUpdatedAt(this.updatedAt)
    )
  }
}
