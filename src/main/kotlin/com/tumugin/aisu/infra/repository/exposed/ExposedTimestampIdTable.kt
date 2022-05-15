package com.tumugin.aisu.infra.repository.exposed

import kotlinx.datetime.Clock
import org.jetbrains.exposed.dao.*
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

abstract class ExposedTimestampIdTable(name: String = "", columnName: String = "id") : IntIdTable(name, columnName) {
  val createdAt = datetimeWithTZ("created_at").clientDefault { Clock.System.now() }
  val updatedAt = datetimeWithTZ("updated_at").clientDefault { Clock.System.now() }
}

abstract class ExposedTimestampIdEntity(id: EntityID<Int>, table: ExposedTimestampIdTable) : IntEntity(id) {
  val createdAt by table.createdAt
  var updatedAt by table.updatedAt
}

abstract class ExposedTimestampIdEntityClass<E : ExposedTimestampIdEntity>(table: ExposedTimestampIdTable) :
  IntEntityClass<E>(table) {
  init {
    EntityHook.subscribe { action ->
      if (action.changeType == EntityChangeType.Updated) {
        action.toEntity(this)?.updatedAt = Clock.System.now()
      }
    }
  }
}
