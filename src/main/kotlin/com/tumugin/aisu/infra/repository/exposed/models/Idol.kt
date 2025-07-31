@file:OptIn(kotlin.time.ExperimentalTime::class)

package com.tumugin.aisu.infra.repository.exposed.models

import com.tumugin.aisu.domain.idol.*
import com.tumugin.aisu.domain.user.UserId
import com.tumugin.aisu.infra.repository.exposed.ExposedTimestampIdEntity
import com.tumugin.aisu.infra.repository.exposed.ExposedTimestampIdEntityClass
import com.tumugin.aisu.infra.repository.exposed.ExposedTimestampIdTable
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.ReferenceOption
import kotlin.time.toKotlinInstant

object Idols : ExposedTimestampIdTable("idols") {
  val user = reference("user_id", Users, onDelete = ReferenceOption.SET_NULL).nullable()
  val name = varchar("name", 255)
  val status = varchar("status", 255)
}

class Idol(id: EntityID<Long>) : ExposedTimestampIdEntity(id, Idols) {
  companion object : ExposedTimestampIdEntityClass<Idol>(Idols)

  var user by User optionalReferencedOn Idols.user
  var name by Idols.name
  var status by Idols.status
  var groups by Group via GroupIdols

  fun toDomain(): com.tumugin.aisu.domain.idol.Idol {
    return Idol(
      idolId = IdolId(this.id.value),
      userId = this.user?.id?.value?.let { UserId(it) },
      idolName = IdolName(this.name),
      idolStatus = IdolStatus.valueOf(this.status),
      idolCreatedAt = IdolCreatedAt(this.createdAt.toInstant().toKotlinInstant()),
      idolUpdatedAt = IdolUpdatedAt(this.updatedAt.toInstant().toKotlinInstant())
    )
  }
}
