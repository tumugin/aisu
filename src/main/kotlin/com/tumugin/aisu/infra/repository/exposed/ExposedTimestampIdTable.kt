package com.tumugin.aisu.infra.repository.exposed

import kotlinx.datetime.Clock
import org.jetbrains.exposed.dao.id.IntIdTable

open class ExposedTimestampIdTable : IntIdTable() {
  val createdAt = datetimeWithTZ("created_at").clientDefault { Clock.System.now() }
  val updatedAt = datetimeWithTZ("updated_at").clientDefault { Clock.System.now() }
}
